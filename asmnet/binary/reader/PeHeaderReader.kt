/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.PeSignature

internal fun PeHeader(buffer: ByteBuf): CoffHeader = buffer.slice().let { buf ->
    val signature = buf.readUIntLE()
    require(signature == PeSignature.SIGNATURE) { "Invalid PE signature: 0x${signature.toString(16)}" }
    CoffHeader(buf.slice(PeSignature.SIZE, buf.readableBytes()))
}
