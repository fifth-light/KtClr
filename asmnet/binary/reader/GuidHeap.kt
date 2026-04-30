/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import java.util.UUID

class GuidHeap(private val buffer: ByteBuf) {
    fun get(index: Int): UUID {
        require(index >= 1) { "GUID index must be >= 1: $index" }
        val offset = (index - 1) * 16
        require(offset + 16 <= buffer.capacity()) {
            "GUID index $index out of bounds: need offset $offset, buffer capacity ${buffer.capacity()}"
        }
        val data1 = buffer.getIntLE(offset)
        val data2 = buffer.getShortLE(offset + 4)
        val data3 = buffer.getShortLE(offset + 6)
        val msb = (data1.toLong() and 0xFFFFFFFFL) shl 32 or
                ((data2.toInt() and 0xFFFF).toLong() shl 16) or
                (data3.toInt() and 0xFFFF).toLong()
        val lsb = buffer.getLong(offset + 8)
        return UUID(msb, lsb)
    }
}
