/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.PeSignature
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun PeHeader(buffer: ByteBuffer): CoffHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    val signature = buf.getInt().toUInt()
    require(signature == PeSignature.SIGNATURE) { "Invalid PE signature: 0x${signature.toString(16)}" }
    CoffHeader(buf.slice(PeSignature.SIZE, buf.remaining()))
}
