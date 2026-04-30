/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.util

import io.netty.buffer.AbstractReferenceCountedByteBuf
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ReadOnlyBufferException
import java.nio.channels.FileChannel
import java.nio.channels.GatheringByteChannel

class MappedByteBuf(
    chunks: Array<Chunk>,
    private val fallback: ByteBuf? = null,
    private val allocValue: ByteBufAllocator = ByteBufAllocator.DEFAULT,
) : AbstractReferenceCountedByteBuf(Int.MAX_VALUE) {
    data class Chunk(
        val offset: Int,
        val length: Int,
        val buf: ByteBuf,
    )

    private class SortedChunk(
        val offset: Int,
        val endOffset: Int,
        val buf: ByteBuf,
    )

    constructor(
        chunks: List<Chunk>,
        fallback: ByteBuf? = null,
        alloc: ByteBufAllocator = ByteBufAllocator.DEFAULT
    ) : this(chunks.toTypedArray(), fallback, alloc)

    private val sortedChunks: Array<SortedChunk>
    private val capacityValue: Int
    private var cachedChunkIndex: Int = 0

    init {
        for (chunk in chunks) {
            require(chunk.offset >= 0) { "Chunk offset must be >= 0: ${chunk.offset}" }
            require(chunk.length >= 0) { "Chunk length must be >= 0: ${chunk.length}" }
            require(chunk.offset.toLong() + chunk.length.toLong() <= Int.MAX_VALUE) {
                "Chunk end offset overflow: ${chunk.offset} + ${chunk.length} > ${Int.MAX_VALUE}"
            }
        }
        val sorted = chunks.sortedBy { it.offset }
        for (i in 0 until sorted.lastIndex) {
            val a = sorted[i]
            val b = sorted[i + 1]
            require(a.offset + a.length <= b.offset) {
                "Overlapping chunks: [${a.offset}, ${a.offset + a.length}) and [${b.offset}, ${b.offset + b.length})"
            }
        }
        sortedChunks = Array(sorted.size) { i ->
            val c = sorted[i]
            SortedChunk(offset = c.offset, endOffset = c.offset + c.length, buf = c.buf)
        }
        val maxChunkEnd = sortedChunks.lastOrNull()?.endOffset ?: 0
        capacityValue = maxOf(fallback?.capacity() ?: 0, maxChunkEnd)
        for (c in sortedChunks) {
            c.buf.retain()
        }
        fallback?.retain()
    }

    private fun findChunkIndex(index: Int): Int {
        val cached = sortedChunks.getOrNull(cachedChunkIndex)
        if (cached != null && index >= cached.offset && index < cached.endOffset) {
            return cachedChunkIndex
        }
        var low = 0
        var high = sortedChunks.lastIndex
        while (low <= high) {
            val mid = (low + high) ushr 1
            val c = sortedChunks[mid]
            if (index >= c.endOffset) {
                low = mid + 1
            } else if (index < c.offset) {
                high = mid - 1
            } else {
                cachedChunkIndex = mid
                return mid
            }
        }
        return -1
    }

    private fun fallbackLength(fromIndex: Int, remaining: Int): Int {
        if (fallback == null || fromIndex >= fallback.capacity()) return 0
        val nextChunkStart = findNextChunkStart(fromIndex)
        val fallbackEnd = minOf(fallback.capacity(), nextChunkStart)
        return minOf(remaining, fallbackEnd - fromIndex)
    }

    private fun findNextChunkStart(fromIndex: Int): Int {
        var low = 0
        var high = sortedChunks.lastIndex
        var result = Int.MAX_VALUE
        while (low <= high) {
            val mid = (low + high) ushr 1
            val c = sortedChunks[mid]
            if (c.offset > fromIndex) {
                result = c.offset
                high = mid - 1
            } else {
                low = mid + 1
            }
        }
        return result
    }

    override fun capacity() = capacityValue

    override fun capacity(newCapacity: Int): ByteBuf = throw ReadOnlyBufferException()

    override fun alloc() = allocValue

    @Deprecated("Deprecated in Java")
    override fun order(): ByteOrder = ByteOrder.BIG_ENDIAN

    override fun unwrap() = null

    override fun isDirect() = false

    override fun isReadOnly() = true

    override fun _getByte(index: Int): Byte {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            return c.buf.getByte(index - c.offset)
        }
        if (fallback != null && index < fallback.capacity()) {
            return fallback.getByte(index)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getShort(index: Int): Short {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 2 <= c.endOffset) {
                return c.buf.getShort(index - c.offset)
            }
            return (((
                    _getByte(index).toInt() and 0xFF) shl 8) or
                    (_getByte(index + 1).toInt() and 0xFF)
                    ).toShort()
        }
        if (fallback != null && index < fallback.capacity()) {
            return (((
                    _getByte(index).toInt() and 0xFF) shl 8) or
                    (_getByte(index + 1).toInt() and 0xFF)
                    ).toShort()
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getShortLE(index: Int): Short {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 2 <= c.endOffset) {
                return c.buf.getShortLE(index - c.offset)
            }
            return ((
                    _getByte(index).toInt() and 0xFF) or
                    ((_getByte(index + 1).toInt() and 0xFF) shl 8)
                    ).toShort()
        }
        if (fallback != null && index < fallback.capacity()) {
            return ((
                    _getByte(index).toInt() and 0xFF) or
                    ((_getByte(index + 1).toInt() and 0xFF) shl 8)
                    ).toShort()
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getUnsignedMedium(index: Int): Int {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 3 <= c.endOffset) {
                return c.buf.getUnsignedMedium(index - c.offset)
            }
            return ((
                    _getShort(index).toInt() and 0xFFFF) shl 8) or
                    (_getByte(index + 2).toInt() and 0xFF)
        }
        if (fallback != null && index < fallback.capacity()) {
            return ((
                    _getShort(index).toInt() and 0xFFFF) shl 8) or
                    (_getByte(index + 2).toInt() and 0xFF)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getUnsignedMediumLE(index: Int): Int {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 3 <= c.endOffset) {
                return c.buf.getUnsignedMediumLE(index - c.offset)
            }
            return (_getShortLE(index).toInt() and 0xFFFF) or
                    ((_getByte(index + 2).toInt() and 0xFF) shl 16)
        }
        if (fallback != null && index < fallback.capacity()) {
            return (_getShortLE(index).toInt() and 0xFFFF) or
                    ((_getByte(index + 2).toInt() and 0xFF) shl 16)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getInt(index: Int): Int {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 4 <= c.endOffset) {
                return c.buf.getInt(index - c.offset)
            }
            return ((
                    _getShort(index).toInt() and 0xFFFF) shl 16) or
                    (_getShort(index + 2).toInt() and 0xFFFF)
        }
        if (fallback != null && index < fallback.capacity()) {
            return ((
                    _getShort(index).toInt() and 0xFFFF) shl 16) or
                    (_getShort(index + 2).toInt() and 0xFFFF)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getIntLE(index: Int): Int {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 4 <= c.endOffset) {
                return c.buf.getIntLE(index - c.offset)
            }
            return (_getShortLE(index).toInt() and 0xFFFF) or
                    ((_getShortLE(index + 2).toInt() and 0xFFFF) shl 16)
        }
        if (fallback != null && index < fallback.capacity()) {
            return (_getShortLE(index).toInt() and 0xFFFF) or
                    ((_getShortLE(index + 2).toInt() and 0xFFFF) shl 16)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getLong(index: Int): Long {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 8 <= c.endOffset) {
                return c.buf.getLong(index - c.offset)
            }
            return ((
                    _getInt(index).toLong() and 0xFFFFFFFFL) shl 32) or
                    (_getInt(index + 4).toLong() and 0xFFFFFFFFL)
        }
        if (fallback != null && index < fallback.capacity()) {
            return ((
                    _getInt(index).toLong() and 0xFFFFFFFFL) shl 32) or
                    (_getInt(index + 4).toLong() and 0xFFFFFFFFL)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _getLongLE(index: Int): Long {
        val ci = findChunkIndex(index)
        if (ci >= 0) {
            val c = sortedChunks[ci]
            if (index + 8 <= c.endOffset) {
                return c.buf.getLongLE(index - c.offset)
            }
            return (_getIntLE(index).toLong() and 0xFFFFFFFFL) or
                    ((_getIntLE(index + 4).toLong() and 0xFFFFFFFFL) shl 32)
        }
        if (fallback != null && index < fallback.capacity()) {
            return (_getIntLE(index).toLong() and 0xFFFFFFFFL) or
                    ((_getIntLE(index + 4).toLong() and 0xFFFFFFFFL) shl 32)
        }
        throw IndexOutOfBoundsException("Index $index not mapped and no fallback available")
    }

    override fun _setByte(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setShort(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setShortLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setMedium(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setMediumLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setInt(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setIntLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setLong(index: Int, value: Long) = throw ReadOnlyBufferException()

    override fun _setLongLE(index: Int, value: Long) = throw ReadOnlyBufferException()

    override fun getBytes(index: Int, dst: ByteBuf, dstIndex: Int, length: Int): ByteBuf = also {
        checkDstIndex(index, length, dstIndex, dst.capacity())
        if (length == 0) return this
        var srcIdx = index
        var dstIdx = dstIndex
        var remaining = length
        while (remaining > 0) {
            val ci = findChunkIndex(srcIdx)
            if (ci >= 0) {
                val c = sortedChunks[ci]
                val localLen = minOf(remaining, c.endOffset - srcIdx)
                c.buf.getBytes(srcIdx - c.offset, dst, dstIdx, localLen)
                srcIdx += localLen
                dstIdx += localLen
                remaining -= localLen
            } else {
                val localLen = fallbackLength(srcIdx, remaining)
                if (localLen <= 0) {
                    throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                }
                fallback!!.getBytes(srcIdx, dst, dstIdx, localLen)
                srcIdx += localLen
                dstIdx += localLen
                remaining -= localLen
            }
        }
    }

    override fun getBytes(index: Int, dst: ByteArray, dstIndex: Int, length: Int): ByteBuf = also {
        checkDstIndex(index, length, dstIndex, dst.size)
        if (length == 0) return this
        var srcIdx = index
        var dstIdx = dstIndex
        var remaining = length
        while (remaining > 0) {
            val ci = findChunkIndex(srcIdx)
            if (ci >= 0) {
                val c = sortedChunks[ci]
                val localLen = minOf(remaining, c.endOffset - srcIdx)
                c.buf.getBytes(srcIdx - c.offset, dst, dstIdx, localLen)
                srcIdx += localLen
                dstIdx += localLen
                remaining -= localLen
            } else {
                val localLen = fallbackLength(srcIdx, remaining)
                if (localLen <= 0) {
                    throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                }
                fallback!!.getBytes(srcIdx, dst, dstIdx, localLen)
                srcIdx += localLen
                dstIdx += localLen
                remaining -= localLen
            }
        }
    }

    override fun getBytes(index: Int, dst: ByteBuffer): ByteBuf = also {
        val length = dst.remaining()
        checkIndex(index, length)
        if (length == 0) return this
        val savedLimit = dst.limit()
        var srcIdx = index
        var remaining = length
        try {
            while (remaining > 0) {
                val ci = findChunkIndex(srcIdx)
                if (ci >= 0) {
                    val c = sortedChunks[ci]
                    val localLen = minOf(remaining, c.endOffset - srcIdx)
                    dst.limit(dst.position() + localLen)
                    c.buf.getBytes(srcIdx - c.offset, dst)
                    srcIdx += localLen
                    remaining -= localLen
                } else {
                    val localLen = fallbackLength(srcIdx, remaining)
                    if (localLen <= 0) {
                        throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                    }
                    dst.limit(dst.position() + localLen)
                    fallback!!.getBytes(srcIdx, dst)
                    srcIdx += localLen
                    remaining -= localLen
                }
            }
        } finally {
            dst.limit(savedLimit)
        }
    }

    override fun getBytes(index: Int, out: OutputStream, length: Int): ByteBuf = also {
        checkIndex(index, length)
        if (length == 0) return this
        var srcIdx = index
        var remaining = length
        while (remaining > 0) {
            val ci = findChunkIndex(srcIdx)
            if (ci >= 0) {
                val c = sortedChunks[ci]
                val localLen = minOf(remaining, c.endOffset - srcIdx)
                c.buf.getBytes(srcIdx - c.offset, out, localLen)
                srcIdx += localLen
                remaining -= localLen
            } else {
                val localLen = fallbackLength(srcIdx, remaining)
                if (localLen <= 0) {
                    throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                }
                fallback!!.getBytes(srcIdx, out, localLen)
                srcIdx += localLen
                remaining -= localLen
            }
        }
    }

    override fun getBytes(index: Int, out: GatheringByteChannel, length: Int): Int {
        checkIndex(index, length)
        val buffers = nioBuffers(index, length)
        val written = out.write(buffers)
        return if (written > Int.MAX_VALUE) Int.MAX_VALUE else written.toInt()
    }

    override fun getBytes(index: Int, out: FileChannel, position: Long, length: Int): Int {
        checkIndex(index, length)
        val buffers = nioBuffers(index, length)
        var writtenBytes = 0L
        for (buf in buffers) {
            writtenBytes += out.write(buf, position + writtenBytes)
        }
        return if (writtenBytes > Int.MAX_VALUE) Int.MAX_VALUE else writtenBytes.toInt()
    }

    override fun setBytes(index: Int, src: ByteBuf, srcIndex: Int, length: Int): ByteBuf =
        throw ReadOnlyBufferException()

    override fun setBytes(index: Int, src: ByteArray, srcIndex: Int, length: Int): ByteBuf =
        throw ReadOnlyBufferException()

    override fun setBytes(index: Int, src: ByteBuffer): ByteBuf = throw ReadOnlyBufferException()

    override fun setBytes(index: Int, `in`: java.io.InputStream, length: Int): Int =
        throw ReadOnlyBufferException()

    override fun setBytes(index: Int, `in`: java.nio.channels.ScatteringByteChannel, length: Int): Int =
        throw ReadOnlyBufferException()

    override fun setBytes(index: Int, `in`: FileChannel, position: Long, length: Int): Int =
        throw ReadOnlyBufferException()

    override fun copy(index: Int, length: Int): ByteBuf {
        checkIndex(index, length)
        val dst = Unpooled.buffer(length)
        if (length == 0) return dst
        var srcIdx = index
        var remaining = length
        while (remaining > 0) {
            val ci = findChunkIndex(srcIdx)
            if (ci >= 0) {
                val c = sortedChunks[ci]
                val localLen = minOf(remaining, c.endOffset - srcIdx)
                c.buf.getBytes(srcIdx - c.offset, dst, localLen)
                srcIdx += localLen
                remaining -= localLen
            } else {
                val localLen = fallbackLength(srcIdx, remaining)
                if (localLen <= 0) {
                    throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                }
                fallback!!.getBytes(srcIdx, dst, localLen)
                srcIdx += localLen
                remaining -= localLen
            }
        }
        dst.writerIndex(length)
        return dst
    }

    override fun nioBufferCount() = -1

    override fun nioBuffer(index: Int, length: Int): ByteBuffer = ByteBuffer.allocate(length).apply {
        getBytes(index, this)
        flip()
    }

    override fun internalNioBuffer(index: Int, length: Int): ByteBuffer = throw UnsupportedOperationException()

    override fun nioBuffers(index: Int, length: Int): Array<ByteBuffer> {
        checkIndex(index, length)
        if (length == 0) return arrayOf(ByteBuffer.allocate(0))
        val result = mutableListOf<ByteBuffer>()
        var srcIdx = index
        var remaining = length
        while (remaining > 0) {
            val ci = findChunkIndex(srcIdx)
            if (ci >= 0) {
                val c = sortedChunks[ci]
                val localLen = minOf(remaining, c.endOffset - srcIdx)
                val count = c.buf.nioBufferCount()
                if (count == -1 || count == 0) {
                    throw UnsupportedOperationException()
                }
                if (count == 1) {
                    result.add(c.buf.nioBuffer(srcIdx - c.offset, localLen))
                } else {
                    result.addAll(c.buf.nioBuffers(srcIdx - c.offset, localLen))
                }
                srcIdx += localLen
                remaining -= localLen
            } else {
                val localLen = fallbackLength(srcIdx, remaining)
                if (localLen <= 0) {
                    throw IndexOutOfBoundsException("Index $srcIdx not mapped and no fallback available")
                }
                val fb = fallback!!
                val count = fb.nioBufferCount()
                if (count == -1 || count == 0) {
                    throw UnsupportedOperationException()
                }
                if (count == 1) {
                    result.add(fb.nioBuffer(srcIdx, localLen))
                } else {
                    result.addAll(fb.nioBuffers(srcIdx, localLen))
                }
                srcIdx += localLen
                remaining -= localLen
            }
        }
        return result.toTypedArray()
    }

    override fun hasArray() = false

    override fun array(): ByteArray = throw UnsupportedOperationException()

    override fun arrayOffset(): Int = throw UnsupportedOperationException()

    override fun hasMemoryAddress() = false

    override fun memoryAddress(): Long = throw UnsupportedOperationException()

    override fun retain(increment: Int): ByteBuf = super.retain(increment)

    override fun retain(): ByteBuf = super.retain()

    override fun touch(): ByteBuf = this

    override fun touch(hint: Any?): ByteBuf = this

    override fun deallocate() {
        for (c in sortedChunks) {
            c.buf.release()
        }
        fallback?.release()
    }
}
