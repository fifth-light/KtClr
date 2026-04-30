/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import java.nio.ByteBuffer
import java.nio.charset.Charset

class UserStringHeap(private val buffer: ByteBuf) {
    fun get(index: Int): String {
        require(index >= 0) { "User string index must be non-negative: $index" }
        buffer.readerIndex(index)
        val byteLength = readBlobLength(buffer)
        if (byteLength == 0) {
            return ""
        }
        val stringBytes = byteLength - 1
        val dataStart = buffer.readerIndex()
        val bytes = ByteArray(stringBytes)
        buffer.getBytes(dataStart, bytes)
        return String(bytes, Charsets.UTF_16LE)
    }
}
