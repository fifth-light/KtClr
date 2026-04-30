/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf

internal fun readBlobLength(buffer: ByteBuf): Int {
    val b0 = buffer.readUnsignedByte().toInt()
    return when {
        (b0 and 0x80) == 0 -> b0
        (b0 and 0xC0) == 0x80 -> {
            val b1 = buffer.readUnsignedByte().toInt()
            ((b0 and 0x3F) shl 8) or b1
        }
        else -> {
            val b1 = buffer.readUnsignedByte().toInt()
            val b2 = buffer.readUnsignedByte().toInt()
            val b3 = buffer.readUnsignedByte().toInt()
            ((b0 and 0x1F) shl 24) or (b1 shl 16) or (b2 shl 8) or b3
        }
    }
}

class BlobHeap(private val buffer: ByteBuf) {
    fun get(index: Int): ByteBuf {
        require(index >= 0) { "Blob index must be non-negative: $index" }
        buffer.readerIndex(index)
        val length = readBlobLength(buffer)
        val dataStart = buffer.readerIndex()
        return buffer.slice(dataStart, length)
    }
}
