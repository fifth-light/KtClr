/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.util

import io.netty.buffer.AbstractByteBuf
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ReadOnlyBufferException
import java.nio.channels.FileChannel
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import java.util.Arrays

class ZeroByteBuf(
    private val alloc: ByteBufAllocator,
    private var capacity: Int,
) : AbstractByteBuf(Int.MAX_VALUE) {
    companion object {
        private val zeroByteBuffer by lazy { ByteBuffer.allocateDirect(1 * 1024 * 1024) }
    }

    override fun capacity() = capacity

    override fun capacity(newCapacity: Int) = also {
        require(newCapacity in 0 until maxCapacity()) { "capacity must be in [0, ${maxCapacity()})" }
        capacity = newCapacity
    }

    override fun alloc() = alloc

    @Deprecated("Deprecated in Java")
    override fun order(): ByteOrder = ByteOrder.BIG_ENDIAN

    override fun unwrap() = null

    override fun isDirect() = false

    override fun isReadOnly() = true

    override fun getBytes(
        index: Int,
        dst: ByteBuf,
        dstIndex: Int,
        length: Int,
    ) = also {
        checkDstIndex(index, length, dstIndex, dst.capacity())
        dst.setZero(dstIndex, length)
    }

    override fun getBytes(
        index: Int,
        dst: ByteArray,
        dstIndex: Int,
        length: Int,
    ) = also {
        checkDstIndex(index, length, dstIndex, dst.size)
        Arrays.fill(dst, dstIndex, dstIndex + length, 0)
    }

    override fun getBytes(index: Int, dst: ByteBuffer) = also {
        checkIndex(index, dst.remaining())
        if (dst.hasArray()) {
            val arrayStart = dst.arrayOffset() + dst.position()
            Arrays.fill(dst.array(), arrayStart, arrayStart + dst.remaining(), 0)
            dst.position(dst.position() + dst.remaining())
        } else {
            while (dst.hasRemaining()) {
                dst.put(0)
            }
        }
    }

    override fun getBytes(index: Int, out: OutputStream, length: Int) = also {
        checkIndex(index, length)
        repeat(length) { out.write(0) }
    }

    override fun getBytes(
        index: Int,
        out: GatheringByteChannel,
        length: Int,
    ): Int {
        checkIndex(index, length)
        val zeroBuffer = zeroByteBuffer.duplicate().clear()
        val bufferSize = zeroBuffer.capacity()
        val bufferCount = length.ceilDiv(bufferSize)
        var remaining = length
        val buffers = Array<ByteBuffer>(bufferCount) {
            val size = remaining.coerceAtMost(bufferSize)
            remaining -= size
            zeroBuffer.slice(0, size)
        }
        return out.write(buffers).toInt()
    }

    override fun getBytes(
        index: Int,
        out: FileChannel,
        position: Long,
        length: Int,
    ): Int {
        checkIndex(index, length)
        val zeroBuffer = zeroByteBuffer.duplicate().clear()
        val bufferSize = zeroBuffer.capacity()
        var currentPos = position
        val target = currentPos + length
        while (currentPos < target) {
            val size = (target - currentPos).coerceAtMost(bufferSize.toLong()).toInt()
            val written = out.write(zeroBuffer.slice(0, size), currentPos)
            currentPos += written
            if (written < size) {
                return (currentPos - position).toInt()
            }
        }
        return length
    }

    override fun setBytes(
        index: Int,
        src: ByteBuf,
        srcIndex: Int,
        length: Int,
    ) = throw ReadOnlyBufferException()

    override fun setBytes(
        index: Int,
        src: ByteArray,
        srcIndex: Int,
        length: Int,
    ) = throw ReadOnlyBufferException()

    override fun setBytes(index: Int, src: ByteBuffer) = throw ReadOnlyBufferException()

    override fun setBytes(index: Int, `in`: InputStream, length: Int) = throw ReadOnlyBufferException()

    override fun setBytes(
        index: Int,
        `in`: ScatteringByteChannel,
        length: Int,
    ) = throw ReadOnlyBufferException()

    override fun setBytes(
        index: Int,
        `in`: FileChannel,
        position: Long,
        length: Int,
    ) = throw ReadOnlyBufferException()

    override fun copy(index: Int, length: Int): ByteBuf = ZeroByteBuf(alloc = alloc, capacity = length)
        .readerIndex(0)
        .writerIndex(length)

    override fun nioBufferCount() = -1

    override fun nioBuffer(index: Int, length: Int) = throw UnsupportedOperationException()

    override fun internalNioBuffer(index: Int, length: Int) = throw UnsupportedOperationException()

    override fun nioBuffers(index: Int, length: Int) = throw UnsupportedOperationException()

    override fun hasArray() = false

    override fun array() = throw UnsupportedOperationException()

    override fun arrayOffset() = throw UnsupportedOperationException()

    override fun hasMemoryAddress() = false

    override fun memoryAddress() = throw UnsupportedOperationException()

    override fun retain(increment: Int) = this

    override fun retain() = this

    override fun touch() = this

    override fun touch(hint: Any?) = this

    override fun _getByte(index: Int) = 0.toByte()

    override fun _getShort(index: Int) = 0.toShort()

    override fun _getShortLE(index: Int) = 0.toShort()

    override fun _getUnsignedMedium(index: Int) = 0

    override fun _getUnsignedMediumLE(index: Int) = 0

    override fun _getInt(index: Int) = 0

    override fun _getIntLE(index: Int) = 0

    override fun _getLong(index: Int) = 0L

    override fun _getLongLE(index: Int) = 0L

    override fun _setByte(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setShort(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setShortLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setMedium(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setMediumLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setInt(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setIntLE(index: Int, value: Int) = throw ReadOnlyBufferException()

    override fun _setLong(index: Int, value: Long) = throw ReadOnlyBufferException()

    override fun _setLongLE(index: Int, value: Long) = throw ReadOnlyBufferException()

    override fun refCnt() = 1

    override fun release() = false

    override fun release(decrement: Int) = false
}
