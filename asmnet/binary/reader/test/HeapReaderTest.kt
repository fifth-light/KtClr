/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.test

import io.netty.buffer.Unpooled
import org.junit.Test
import top.fifthlight.asmnet.binary.reader.BlobHeap
import top.fifthlight.asmnet.binary.reader.GuidHeap
import top.fifthlight.asmnet.binary.reader.StringHeap
import top.fifthlight.asmnet.binary.reader.UserStringHeap
import java.util.UUID
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HeapReaderTest {
    // ---- BlobHeap ----

    @Test
    fun testBlobEmpty() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = BlobHeap(buf)
        val result = heap.get(0)
        assertEquals(0, result.readableBytes())
    }

    @Test
    fun testBlobOneBytePrefix() {
        // index 0: empty blob (0x00)
        // index 1: blob of length 5, data = [0x01, 0x02, 0x03, 0x04, 0x05]
        // encoding: [0x05, 0x01, 0x02, 0x03, 0x04, 0x05]
        val buf = Unpooled.buffer(7)
        buf.writeByte(0x00)
        buf.writeByte(0x05)
        buf.writeBytes(byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05))
        val heap = BlobHeap(buf)

        val result = heap.get(1)
        assertEquals(5, result.readableBytes())
        val bytes = ByteArray(5)
        result.readBytes(bytes)
        assertContentEquals(byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05), bytes)
    }

    @Test
    fun testBlobOneBytePrefixMaxLength() {
        // max 1-byte length = 0x7F = 127
        val buf = Unpooled.buffer(128)
        buf.writeByte(0x7F) // length = 127
        val data = ByteArray(127) { it.toByte() }
        buf.writeBytes(data)
        val heap = BlobHeap(buf)

        val result = heap.get(0)
        assertEquals(127, result.readableBytes())
        val bytes = ByteArray(127)
        result.readBytes(bytes)
        assertContentEquals(data, bytes)
    }

    @Test
    fun testBlobTwoBytePrefix() {
        // length = 128 → 2-byte prefix
        // b0 = 0x80 | 0x00 = 0x80, b1 = 0x80
        // check: ((0x80 & 0x3F) << 8) | 0x80 = (0 << 8) | 128 = 128 ✓
        val length = 128
        val buf = Unpooled.buffer(2 + length)
        buf.writeByte(0x80)
        buf.writeByte(0x80)
        val data = ByteArray(length) { (it and 0xFF).toByte() }
        buf.writeBytes(data)
        val heap = BlobHeap(buf)

        val result = heap.get(0)
        assertEquals(length, result.readableBytes())
        val bytes = ByteArray(length)
        result.readBytes(bytes)
        assertContentEquals(data, bytes)
    }

    @Test
    fun testBlobTwoBytePrefixLarge() {
        // length = 10000 = 0x2710
        // b0 = 0x80 | ((0x2710 >> 8) & 0x3F) = 0x80 | 0x27 = 0xA7
        // b1 = 0x10
        val length = 10000
        val buf = Unpooled.buffer(2 + length)
        buf.writeByte(0xA7)
        buf.writeByte(0x10)
        val data = ByteArray(length) { ((it * 7 + 3) and 0xFF).toByte() }
        buf.writeBytes(data)
        val heap = BlobHeap(buf)

        val result = heap.get(0)
        assertEquals(length, result.readableBytes())
        val bytes = ByteArray(length)
        result.readBytes(bytes)
        assertContentEquals(data, bytes)
    }

    @Test
    fun testBlobNegativeIndex() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = BlobHeap(buf)
        assertFailsWith<IllegalArgumentException> {
            heap.get(-1)
        }
    }

    // ---- StringHeap ----

    @Test
    fun testStringEmpty() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = StringHeap(buf)
        assertEquals("", heap.get(0))
    }

    @Test
    fun testStringSingle() {
        val buf = Unpooled.buffer(6)
        buf.writeByte(0x00) // index 0: empty
        buf.writeBytes("Hello".toByteArray(Charsets.UTF_8))
        buf.writeByte(0x00)
        val heap = StringHeap(buf)
        assertEquals("", heap.get(0))
        assertEquals("Hello", heap.get(1))
    }

    @Test
    fun testStringMultiple() {
        val buf = Unpooled.buffer(32)
        buf.writeByte(0x00) // index 0: empty
        buf.writeBytes("foo".toByteArray(Charsets.UTF_8))
        buf.writeByte(0x00) // index 1: "foo"
        buf.writeBytes("bar".toByteArray(Charsets.UTF_8))
        buf.writeByte(0x00) // index 5: "bar"
        buf.writeBytes("longer_string".toByteArray(Charsets.UTF_8))
        buf.writeByte(0x00) // index 9: "longer_string"
        val heap = StringHeap(buf)
        assertEquals("", heap.get(0))
        assertEquals("foo", heap.get(1))
        assertEquals("bar", heap.get(5))
        assertEquals("longer_string", heap.get(9))
    }

    @Test
    fun testStringNegativeIndex() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = StringHeap(buf)
        assertFailsWith<IllegalArgumentException> {
            heap.get(-1)
        }
    }

    // ---- GuidHeap ----

    @Test
    fun testGuidSingle() {
        // UUID "550e8400-e29b-41d4-a716-446655440000"
        // msb = 0x550e8400e29b41d4
        // lsb = 0xa716446655440000
        // Data1 = 0x550e8400 → LE bytes: 00 84 0e 55
        // Data2 = 0xe29b → LE bytes: 9b e2
        // Data3 = 0x41d4 → LE bytes: d4 41
        // Data4 = a7 16 44 66 55 44 00 00 (big-endian)
        val expected = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val buf = Unpooled.buffer(16)
        buf.writeBytes(byteArrayOf(0x00, 0x84.toByte(), 0x0e, 0x55.toByte()))
        buf.writeBytes(byteArrayOf(0x9b.toByte(), 0xe2.toByte()))
        buf.writeBytes(byteArrayOf(0xd4.toByte(), 0x41))
        buf.writeBytes(byteArrayOf(0xa7.toByte(), 0x16, 0x44, 0x66, 0x55, 0x44, 0x00, 0x00))
        val heap = GuidHeap(buf)
        assertEquals(expected, heap.get(1))
    }

    @Test
    fun testGuidMultiple() {
        val uuid1 = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val uuid2 = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")
        val buf = Unpooled.buffer(32)
        // uuid1: all zeros
        buf.writeZero(16)
        // uuid2: all 0xFF
        buf.writeBytes(ByteArray(16) { 0xFF.toByte() })
        val heap = GuidHeap(buf)
        assertEquals(uuid1, heap.get(1))
        assertEquals(uuid2, heap.get(2))
    }

    @Test
    fun testGuidIndexZero() {
        val buf = Unpooled.buffer(16)
        buf.writeZero(16)
        val heap = GuidHeap(buf)
        assertFailsWith<IllegalArgumentException> {
            heap.get(0)
        }
    }

    @Test
    fun testGuidOutOfBounds() {
        val buf = Unpooled.buffer(16)
        buf.writeZero(16)
        val heap = GuidHeap(buf)
        assertFailsWith<IllegalArgumentException> {
            heap.get(2)
        }
    }

    // ---- UserStringHeap ----

    @Test
    fun testUserStringEmpty() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = UserStringHeap(buf)
        assertEquals("", heap.get(0))
    }

    @Test
    fun testUserStringAscii() {
        // "Hi" in UTF-16LE: 0x48 0x00 0x69 0x00
        // 4 bytes string + 1 terminal byte = 5 bytes
        // blob length prefix: 0x05
        val buf = Unpooled.buffer(6)
        buf.writeByte(0x00) // index 0: empty
        buf.writeByte(0x05) // blob length = 5
        buf.writeBytes(byteArrayOf(0x48, 0x00, 0x69, 0x00)) // "Hi"
        buf.writeByte(0x00) // terminal byte
        val heap = UserStringHeap(buf)
        assertEquals("", heap.get(0))
        assertEquals("Hi", heap.get(1))
    }

    @Test
    fun testUserStringUnicode() {
        // "你好" in UTF-16LE: 0x60 0x4F 0x7D 0x59
        // 4 bytes string + 1 terminal byte = 5 bytes
        // blob length prefix: 0x05
        val buf = Unpooled.buffer(6)
        buf.writeByte(0x05) // blob length = 5
        buf.writeBytes(byteArrayOf(0x60, 0x4F.toByte(), 0x7D, 0x59.toByte())) // "你好"
        buf.writeByte(0x01) // terminal byte = 1 (has high-byte characters)
        val heap = UserStringHeap(buf)
        assertEquals("你好", heap.get(0))
    }

    @Test
    fun testUserStringSingleChar() {
        // "A" in UTF-16LE: 0x41 0x00
        // 2 bytes string + 1 terminal byte = 3 bytes
        // blob length prefix: 0x03
        val buf = Unpooled.buffer(4)
        buf.writeByte(0x03)
        buf.writeBytes(byteArrayOf(0x41, 0x00)) // "A"
        buf.writeByte(0x00) // terminal byte
        val heap = UserStringHeap(buf)
        assertEquals("A", heap.get(0))
    }

    @Test
    fun testUserStringNegativeIndex() {
        val buf = Unpooled.buffer(1)
        buf.writeByte(0x00)
        val heap = UserStringHeap(buf)
        assertFailsWith<IllegalArgumentException> {
            heap.get(-1)
        }
    }

    // ---- BlobHeap slice sharing ----

    @Test
    fun testBlobSliceDoesNotAffectOriginal() {
        val buf = Unpooled.buffer(4)
        buf.writeByte(0x02) // length = 2
        buf.writeByte(0xAB)
        buf.writeByte(0xCD)
        val heap = BlobHeap(buf)

        val slice1 = heap.get(0)
        val slice2 = heap.get(0)
        assertEquals(0xAB, slice1.getByte(0).toInt() and 0xFF)
        assertEquals(0xCD, slice1.getByte(1).toInt() and 0xFF)
        assertEquals(0xAB, slice2.getByte(0).toInt() and 0xFF)
        assertEquals(0xCD, slice2.getByte(1).toInt() and 0xFF)
    }

    @Test
    fun testBlobMultipleEntries() {
        // index 0: empty blob
        // index 1: blob length 2, data [0x11, 0x22]
        // index 4: blob length 1, data [0x33]
        val buf = Unpooled.buffer(8)
        buf.writeByte(0x00) // empty blob at index 0
        buf.writeByte(0x02) // length 2 at index 1
        buf.writeByte(0x11)
        buf.writeByte(0x22)
        buf.writeByte(0x01) // length 1 at index 4
        buf.writeByte(0x33)
        val heap = BlobHeap(buf)

        val r0 = heap.get(0)
        assertEquals(0, r0.readableBytes())

        val r1 = heap.get(1)
        assertEquals(2, r1.readableBytes())
        assertEquals(0x11, r1.getByte(0).toInt() and 0xFF)
        assertEquals(0x22, r1.getByte(1).toInt() and 0xFF)

        val r4 = heap.get(4)
        assertEquals(1, r4.readableBytes())
        assertEquals(0x33, r4.getByte(0).toInt() and 0xFF)
    }
}
