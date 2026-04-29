/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.*

internal fun CoffHeader(buffer: ByteBuf): CoffHeader = buffer.slice().let { buf ->
    require(buf.readableBytes() >= CoffHeader.SIZE) { "Buffer too small for COFF header: ${buf.readableBytes()} < ${CoffHeader.SIZE}" }
    CoffHeader(
        machine = MachineType(buf.readUShortLE()),
        numberOfSections = buf.readUShortLE(),
        timeDateStamp = buf.readUIntLE(),
        pointerToSymbolTable = buf.readUIntLE(),
        numberOfSymbols = buf.readUIntLE(),
        sizeOfOptionalHeader = buf.readUShortLE(),
        characteristics = ImageCharacteristics(buf.readUShortLE()),
    )
}
