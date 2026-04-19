/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun SectionHeader(buffer: ByteBuffer): SectionHeader =
    buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
        require(buf.remaining() >= SectionHeader.SIZE) { "Buffer too small for section header: ${buf.remaining()} < ${SectionHeader.SIZE}" }
        val pos = buf.position()
        val name = buf.readString(8, requireNullTerminator = false)
        buf.position(pos + 8)
        SectionHeader(
            name = name,
            virtualSize = buf.uint,
            virtualAddress = buf.uint,
            sizeOfRawData = buf.uint,
            pointerToRawData = buf.uint,
            pointerToRelocations = buf.uint,
            pointerToLinenumbers = buf.uint,
            numberOfRelocations = buf.ushort,
            numberOfLinenumbers = buf.ushort,
            characteristics = SectionFlags(buf.uint),
        )
    }

internal fun rvaToFileOffset(sections: List<SectionHeader>, rva: UInt): Int {
    val section = sections.find {
        rva in it.virtualAddress until it.virtualAddress + it.virtualSize
    } ?: throw IllegalArgumentException("RVA 0x${rva.toString(16)} not found in any section")
    val fileOffset = (rva - section.virtualAddress).toInt() + section.pointerToRawData.toInt()
    require(fileOffset < section.pointerToRawData.toInt() + section.sizeOfRawData.toInt()) {
        "RVA 0x${rva.toString(16)} maps to file offset 0x${fileOffset.toString(16)} beyond section raw data"
    }
    return fileOffset
}
