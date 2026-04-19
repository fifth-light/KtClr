/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun CoffHeader(buffer: ByteBuffer): CoffHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    require(buf.remaining() >= CoffHeader.SIZE) { "Buffer too small for COFF header: ${buf.remaining()} < ${CoffHeader.SIZE}" }
    CoffHeader(
        machine = MachineType(buf.ushort),
        numberOfSections = buf.ushort,
        timeDateStamp = buf.uint,
        pointerToSymbolTable = buf.uint,
        numberOfSymbols = buf.uint,
        sizeOfOptionalHeader = buf.ushort,
        characteristics = ImageCharacteristics(buf.ushort),
    )
}
