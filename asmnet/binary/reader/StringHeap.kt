/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.reader.util.readString

class StringHeap(private val buffer: ByteBuf) {
    fun get(index: Int): String {
        require(index >= 0) { "String index must be non-negative: $index" }
        buffer.readerIndex(index)
        return buffer.readString(Int.MAX_VALUE, charset = Charsets.UTF_8, forceMaxLength = false, requireNullTerminator = true)
    }
}
