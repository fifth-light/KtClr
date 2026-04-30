/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.test

import io.netty.buffer.Unpooled
import org.junit.Test
import top.fifthlight.asmnet.binary.SectionFlags
import top.fifthlight.asmnet.binary.SectionHeader
import top.fifthlight.asmnet.binary.reader.createRvaMappedBuf
import top.fifthlight.asmnet.binary.reader.util.MappedByteBuf
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ReadOnlyBufferException
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MappedByteBufTest {
    private fun makeBuf(vararg bytes: Int): io.netty.buffer.ByteBuf =
        Unpooled.wrappedBuffer(ByteArray(bytes.size) { bytes[it].toByte() })

    @Test
    fun testSingleChunkGetByte() {
        val chunk = MappedByteBuf.Chunk(offset = 10, length = 4, buf = makeBuf(0xAA, 0xBB, 0xCC, 0xDD))
        val mapped = MappedByteBuf(listOf(chunk))
        assertEquals(14, mapped.capacity())
        assertEquals(0xAA.toByte(), mapped.getByte(10))
        assertEquals(0xBB.toByte(), mapped.getByte(11))
        assertEquals(0xCC.toByte(), mapped.getByte(12))
        assertEquals(0xDD.toByte(), mapped.getByte(13))
        mapped.release()
    }

    @Test
    fun testMultipleChunksGetByte() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0x11, 0x22))
        val c2 = MappedByteBuf.Chunk(offset = 10, length = 2, buf = makeBuf(0x33, 0x44))
        val mapped = MappedByteBuf(listOf(c2, c1))
        assertEquals(0x11.toByte(), mapped.getByte(0))
        assertEquals(0x22.toByte(), mapped.getByte(1))
        assertEquals(0x33.toByte(), mapped.getByte(10))
        assertEquals(0x44.toByte(), mapped.getByte(11))
        assertEquals(12, mapped.capacity())
        mapped.release()
    }

    @Test
    fun testUnmappedThrows() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0x11, 0x22))
        val mapped = MappedByteBuf(listOf(c1))
        assertFailsWith<IndexOutOfBoundsException> { mapped.getByte(5) }
        mapped.release()
    }

    @Test
    fun testFallbackGetByte() {
        val fallback = Unpooled.buffer(20)
        repeat(20) { fallback.writeByte(0xFF) }
        fallback.readerIndex(0)
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0x11, 0x22))
        val mapped = MappedByteBuf(listOf(c1), fallback = fallback)
        assertEquals(20, mapped.capacity())
        assertEquals(0x11.toByte(), mapped.getByte(0))
        assertEquals(0x22.toByte(), mapped.getByte(1))
        assertEquals(0xFF.toByte(), mapped.getByte(5))
        mapped.release()
    }

    @Test
    fun testCapacityFromFallback() {
        val fallback = Unpooled.buffer(100)
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 10, buf = makeBuf())
        val mapped = MappedByteBuf(listOf(c1), fallback = fallback)
        assertEquals(100, mapped.capacity())
        mapped.release()
    }

    @Test
    fun testCapacityFromChunks() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 10, buf = makeBuf())
        val c2 = MappedByteBuf.Chunk(offset = 50, length = 20, buf = makeBuf())
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(70, mapped.capacity())
        mapped.release()
    }

    @Test
    fun testGetShortWithinChunk() {
        val buf = Unpooled.buffer(4)
        buf.writeShortLE(0x1234)
        buf.writeShortLE(0x5678)
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 4, buf = buf)
        val mapped = MappedByteBuf(listOf(c1))
        assertEquals(0x1234.toShort(), mapped.getShortLE(0))
        assertEquals(0x5678.toShort(), mapped.getShortLE(2))
        mapped.release()
    }

    @Test
    fun testGetShortCrossChunkBoundary() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 1, buf = makeBuf(0x34))
        val c2 = MappedByteBuf.Chunk(offset = 1, length = 1, buf = makeBuf(0x12))
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(0x1234.toShort(), mapped.getShortLE(0))
        mapped.release()
    }

    @Test
    fun testGetIntCrossChunkBoundary() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0x78, 0x56))
        val c2 = MappedByteBuf.Chunk(offset = 2, length = 2, buf = makeBuf(0x34, 0x12))
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(0x12345678, mapped.getIntLE(0))
        mapped.release()
    }

    @Test
    fun testGetLongCrossChunkBoundary() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 4, buf = makeBuf(0xEF, 0xCD, 0xAB, 0x89))
        val c2 = MappedByteBuf.Chunk(offset = 4, length = 4, buf = makeBuf(0x67, 0x45, 0x23, 0x01))
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(0x0123456789ABCDEFL, mapped.getLongLE(0))
        mapped.release()
    }

    @Test
    fun testGetUnsignedMediumCrossBoundary() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0xBC, 0x9A))
        val c2 = MappedByteBuf.Chunk(offset = 2, length = 1, buf = makeBuf(0x78))
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(0x789ABC, mapped.getUnsignedMediumLE(0))
        mapped.release()
    }

    @Test
    fun testGetBytesToByteArray() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 3, buf = makeBuf(1, 2, 3))
        val c2 = MappedByteBuf.Chunk(offset = 5, length = 2, buf = makeBuf(4, 5))
        val fallback = Unpooled.buffer(10).also { it.writeBytes(ByteArray(10) { 0xFF.toByte() }) }
        val mapped = MappedByteBuf(listOf(c1, c2), fallback = fallback)
        val dst = ByteArray(9)
        mapped.getBytes(0, dst, 0, 9)
        assertContentEquals(byteArrayOf(1, 2, 3, 0xFF.toByte(), 0xFF.toByte(), 4, 5, 0xFF.toByte(), 0xFF.toByte()), dst)
        mapped.release()
    }

    @Test
    fun testGetBytesToByteBuf() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 3, buf = makeBuf(0x10, 0x20, 0x30))
        val c2 = MappedByteBuf.Chunk(offset = 5, length = 2, buf = makeBuf(0x40, 0x50))
        val mapped = MappedByteBuf(listOf(c1, c2))
        val dst = Unpooled.buffer(5)
        mapped.getBytes(0, dst, 0, 3)
        assertEquals(0x10.toByte(), dst.getByte(0))
        assertEquals(0x20.toByte(), dst.getByte(1))
        assertEquals(0x30.toByte(), dst.getByte(2))
        mapped.release()
        dst.release()
    }

    @Test
    fun testGetBytesToByteBuffer() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 3, buf = makeBuf(1, 2, 3))
        val c2 = MappedByteBuf.Chunk(offset = 3, length = 2, buf = makeBuf(4, 5))
        val mapped = MappedByteBuf(listOf(c1, c2))
        val dst = ByteBuffer.allocate(5)
        mapped.getBytes(0, dst)
        dst.flip()
        assertEquals(1.toByte(), dst.get())
        assertEquals(2.toByte(), dst.get())
        assertEquals(3.toByte(), dst.get())
        assertEquals(4.toByte(), dst.get())
        assertEquals(5.toByte(), dst.get())
        mapped.release()
    }

    @Test
    fun testGetBytesToOutputStream() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 3, buf = makeBuf(0xAA, 0xBB, 0xCC))
        val c2 = MappedByteBuf.Chunk(offset = 3, length = 2, buf = makeBuf(0xDD, 0xEE))
        val mapped = MappedByteBuf(listOf(c1, c2))
        val out = ByteArrayOutputStream()
        mapped.getBytes(0, out, 5)
        assertContentEquals(byteArrayOf(0xAA.toByte(), 0xBB.toByte(), 0xCC.toByte(), 0xDD.toByte(), 0xEE.toByte()), out.toByteArray())
        mapped.release()
    }

    @Test
    fun testCopy() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(1, 2))
        val c2 = MappedByteBuf.Chunk(offset = 5, length = 2, buf = makeBuf(3, 4))
        val fallback = Unpooled.buffer(10).also { it.writeBytes(ByteArray(10) { 0x77.toByte() }) }
        val mapped = MappedByteBuf(listOf(c1, c2), fallback = fallback)
        val copy = mapped.copy(0, 7)
        assertEquals(1.toByte(), copy.getByte(0))
        assertEquals(2.toByte(), copy.getByte(1))
        assertEquals(0x77.toByte(), copy.getByte(2))
        assertEquals(0x77.toByte(), copy.getByte(3))
        assertEquals(0x77.toByte(), copy.getByte(4))
        assertEquals(3.toByte(), copy.getByte(5))
        assertEquals(4.toByte(), copy.getByte(6))
        mapped.release()
        copy.release()
    }

    @Test
    fun testOverlappingChunksRejected() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 5, buf = makeBuf())
        val c2 = MappedByteBuf.Chunk(offset = 3, length = 2, buf = makeBuf())
        assertFailsWith<IllegalArgumentException> {
            MappedByteBuf(listOf(c1, c2))
        }
    }

    @Test
    fun testOverlappingChunksAtBoundaryAllowed() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 3, buf = makeBuf(1, 2, 3))
        val c2 = MappedByteBuf.Chunk(offset = 3, length = 2, buf = makeBuf(4, 5))
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(5, mapped.capacity())
        assertEquals(3.toByte(), mapped.getByte(2))
        assertEquals(4.toByte(), mapped.getByte(3))
        mapped.release()
    }

    @Test
    fun testNegativeOffsetRejected() {
        assertFailsWith<IllegalArgumentException> {
            MappedByteBuf(listOf(MappedByteBuf.Chunk(offset = -1, length = 1, buf = makeBuf(0))))
        }
    }

    @Test
    fun testSetByteThrowsReadOnly() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 1, buf = makeBuf(0))
        val mapped = MappedByteBuf(listOf(c1))
        assertFailsWith<ReadOnlyBufferException> { mapped.setByte(0, 1) }
        mapped.release()
    }

    @Test
    fun testIsReadOnly() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 1, buf = makeBuf(0))
        val mapped = MappedByteBuf(listOf(c1))
        assertTrue(mapped.isReadOnly)
        mapped.release()
    }

    @Test
    fun testRetainRelease() {
        val buf1 = makeBuf(1, 2)
        val buf2 = makeBuf(3, 4)
        assertEquals(1, buf1.refCnt())
        assertEquals(1, buf2.refCnt())
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = buf1)
        val c2 = MappedByteBuf.Chunk(offset = 5, length = 2, buf = buf2)
        val mapped = MappedByteBuf(listOf(c1, c2))
        assertEquals(1, mapped.refCnt())
        assertEquals(2, buf1.refCnt())
        assertEquals(2, buf2.refCnt())
        mapped.retain()
        assertEquals(2, mapped.refCnt())
        assertEquals(2, buf1.refCnt())
        assertEquals(2, buf2.refCnt())
        assertFalse(mapped.release())
        assertEquals(1, mapped.refCnt())
        assertEquals(2, buf1.refCnt())
        assertEquals(2, buf2.refCnt())
        assertTrue(mapped.release())
        assertEquals(0, mapped.refCnt())
        assertEquals(1, buf1.refCnt())
        assertEquals(1, buf2.refCnt())
        buf1.release()
        buf2.release()
    }

    @Test
    fun testRetainReleaseWithFallback() {
        val buf = makeBuf(1, 2)
        val fallback = makeBuf(0)
        assertEquals(1, buf.refCnt())
        assertEquals(1, fallback.refCnt())
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = buf)
        val mapped = MappedByteBuf(listOf(c1), fallback = fallback)
        assertEquals(1, mapped.refCnt())
        assertEquals(2, buf.refCnt())
        assertEquals(2, fallback.refCnt())
        mapped.retain(2)
        assertEquals(3, mapped.refCnt())
        assertEquals(2, buf.refCnt())
        assertEquals(2, fallback.refCnt())
        assertFalse(mapped.release(2))
        assertEquals(1, mapped.refCnt())
        assertEquals(2, buf.refCnt())
        assertEquals(2, fallback.refCnt())
        assertTrue(mapped.release())
        assertEquals(0, mapped.refCnt())
        assertEquals(1, buf.refCnt())
        assertEquals(1, fallback.refCnt())
        buf.release()
        fallback.release()
    }

    @Test
    fun testUnsortedChunksGetSorted() {
        val c1 = MappedByteBuf.Chunk(offset = 20, length = 2, buf = makeBuf(0x20, 0x21))
        val c2 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(0x00, 0x01))
        val c3 = MappedByteBuf.Chunk(offset = 10, length = 2, buf = makeBuf(0x10, 0x11))
        val mapped = MappedByteBuf(listOf(c1, c2, c3))
        assertEquals(0x00.toByte(), mapped.getByte(0))
        assertEquals(0x01.toByte(), mapped.getByte(1))
        assertEquals(0x10.toByte(), mapped.getByte(10))
        assertEquals(0x11.toByte(), mapped.getByte(11))
        assertEquals(0x20.toByte(), mapped.getByte(20))
        assertEquals(0x21.toByte(), mapped.getByte(21))
        mapped.release()
    }

    @Test
    fun testBinarySearchCacheHit() {
        val chunks = (0 until 10).map { i ->
            MappedByteBuf.Chunk(offset = i * 100, length = 10, buf = makeBuf(*IntArray(10) { (i * 100 + it).toByte().toInt() }))
        }
        val mapped = MappedByteBuf(chunks)
        assertEquals(0.toByte(), mapped.getByte(0))
        assertEquals(100.toByte(), mapped.getByte(100))
        assertEquals(0.toByte(), mapped.getByte(0))
        assertEquals(505.toByte(), mapped.getByte(505))
        mapped.release()
    }

    @Test
    fun testEmptyChunks() {
        val mapped = MappedByteBuf(emptyList<MappedByteBuf.Chunk>())
        assertEquals(0, mapped.capacity())
        mapped.release()
    }

    @Test
    fun testEmptyChunksWithFallback() {
        val fallback = makeBuf(0x42)
        val mapped = MappedByteBuf(emptyList<MappedByteBuf.Chunk>(), fallback = fallback)
        assertEquals(1, mapped.capacity())
        assertEquals(0x42.toByte(), mapped.getByte(0))
        mapped.release()
    }

    @Test
    fun testGetBytesCrossChunkWithFallback() {
        val c1 = MappedByteBuf.Chunk(offset = 0, length = 2, buf = makeBuf(1, 2))
        val c2 = MappedByteBuf.Chunk(offset = 6, length = 2, buf = makeBuf(7, 8))
        val fallback = Unpooled.buffer(10).also { it.writeBytes(ByteArray(10) { 0xFF.toByte() }) }
        val mapped = MappedByteBuf(listOf(c1, c2), fallback = fallback)
        val dst = ByteArray(10)
        mapped.getBytes(0, dst)
        assertContentEquals(
            byteArrayOf(1, 2, 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 7, 8, 0xFF.toByte(), 0xFF.toByte()),
            dst,
        )
        mapped.release()
    }

    @Test
    fun testCreateRvaMappedBufBasic() {
        val file = Unpooled.buffer(100)
        repeat(100) { file.writeByte(it) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 20u,
                virtualAddress = 0x1000u,
                sizeOfRawData = 16u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        val rvaBuf = createRvaMappedBuf(file, sections)
        assertEquals(0x1014, rvaBuf.capacity())
        assertEquals(0.toByte(), rvaBuf.getByte(0x1000))
        assertEquals(15.toByte(), rvaBuf.getByte(0x100F))
        assertEquals(0.toByte(), rvaBuf.getByte(0x1010))
        assertEquals(0.toByte(), rvaBuf.getByte(0x1013))
        rvaBuf.release()
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufRawDataExceedsFile() {
        val file = Unpooled.buffer(10)
        repeat(10) { file.writeByte(it) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 100u,
                virtualAddress = 0x1000u,
                sizeOfRawData = 50u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            createRvaMappedBuf(file, sections)
        }
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufVirtualAddressOverflow() {
        val file = Unpooled.buffer(100)
        repeat(100) { file.writeByte(0) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 10u,
                virtualAddress = (Int.MAX_VALUE.toUInt() + 1u),
                sizeOfRawData = 0u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            createRvaMappedBuf(file, sections)
        }
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufVirtualSizeOverflow() {
        val file = Unpooled.buffer(100)
        repeat(100) { file.writeByte(0) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = (Int.MAX_VALUE.toUInt() + 1u),
                virtualAddress = 0x1000u,
                sizeOfRawData = 0u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            createRvaMappedBuf(file, sections)
        }
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufPointerToRawDataOverflow() {
        val file = Unpooled.buffer(100)
        repeat(100) { file.writeByte(0) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 10u,
                virtualAddress = 0x1000u,
                sizeOfRawData = 10u,
                pointerToRawData = (Int.MAX_VALUE.toUInt() + 1u),
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            createRvaMappedBuf(file, sections)
        }
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufSizeOfRawDataOverflow() {
        val file = Unpooled.buffer(100)
        repeat(100) { file.writeByte(0) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 10u,
                virtualAddress = 0x1000u,
                sizeOfRawData = (Int.MAX_VALUE.toUInt() + 1u),
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            createRvaMappedBuf(file, sections)
        }
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufEmptySections() {
        val file = Unpooled.buffer(10)
        val rvaBuf = createRvaMappedBuf(file, emptyList())
        assertEquals(0, rvaBuf.capacity())
        rvaBuf.release()
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufNoRawData() {
        val file = Unpooled.buffer(10)
        val sections = listOf(
            SectionHeader(
                name = ".bss",
                virtualSize = 100u,
                virtualAddress = 0x2000u,
                sizeOfRawData = 0u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        val rvaBuf = createRvaMappedBuf(file, sections)
        assertEquals(0x2064, rvaBuf.capacity())
        assertEquals(0.toByte(), rvaBuf.getByte(0x2000))
        assertEquals(0.toByte(), rvaBuf.getByte(0x2063))
        rvaBuf.release()
        file.release()
    }

    @Test
    fun testCreateRvaMappedBufMultipleSections() {
        val file = Unpooled.buffer(200)
        repeat(200) { file.writeByte(it) }
        val sections = listOf(
            SectionHeader(
                name = ".text",
                virtualSize = 100u,
                virtualAddress = 0x1000u,
                sizeOfRawData = 100u,
                pointerToRawData = 0u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
            SectionHeader(
                name = ".rdata",
                virtualSize = 100u,
                virtualAddress = 0x2000u,
                sizeOfRawData = 100u,
                pointerToRawData = 100u,
                pointerToRelocations = 0u,
                pointerToLinenumbers = 0u,
                numberOfRelocations = 0u,
                numberOfLinenumbers = 0u,
                characteristics = SectionFlags(0u),
            ),
        )
        val rvaBuf = createRvaMappedBuf(file, sections)
        assertEquals(0.toByte(), rvaBuf.getByte(0x1000))
        assertEquals(99.toByte(), rvaBuf.getByte(0x1063))
        assertEquals(100.toByte(), rvaBuf.getByte(0x2000))
        assertEquals(199.toByte(), rvaBuf.getByte(0x2063))
        rvaBuf.release()
        file.release()
    }
}
