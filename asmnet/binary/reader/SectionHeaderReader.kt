/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.*

internal fun SectionHeader(buffer: ByteBuf): SectionHeader =
    buffer.slice().let { buf ->
        require(buf.readableBytes() >= SectionHeader.SIZE) { "Buffer too small for section header: ${buf.readableBytes()} < ${SectionHeader.SIZE}" }
        val pos = buf.readerIndex()
        val name = buf.readString(8, requireNullTerminator = false)
        buf.readerIndex(pos + 8)
        SectionHeader(
            name = name,
            virtualSize = buf.readUIntLE(),
            virtualAddress = buf.readUIntLE(),
            sizeOfRawData = buf.readUIntLE(),
            pointerToRawData = buf.readUIntLE(),
            pointerToRelocations = buf.readUIntLE(),
            pointerToLinenumbers = buf.readUIntLE(),
            numberOfRelocations = buf.readUShortLE(),
            numberOfLinenumbers = buf.readUShortLE(),
            characteristics = SectionFlags(buf.readUIntLE()),
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
