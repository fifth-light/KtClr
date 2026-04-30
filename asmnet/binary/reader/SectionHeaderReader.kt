/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.util.MappedByteBuf
import top.fifthlight.asmnet.binary.reader.util.ZeroByteBuf
import top.fifthlight.asmnet.binary.reader.util.readString
import top.fifthlight.asmnet.binary.reader.util.readUIntLE
import top.fifthlight.asmnet.binary.reader.util.readUShortLE

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

internal fun createRvaMappedBuf(
    bytes: ByteBuf,
    sections: List<SectionHeader>,
    alloc: ByteBufAllocator = ByteBufAllocator.DEFAULT,
): MappedByteBuf {
    val chunks = mutableListOf<MappedByteBuf.Chunk>()
    for (section in sections) {
        require(section.virtualAddress <= Int.MAX_VALUE.toUInt()) {
            "Section '${section.name}' virtual address 0x${section.virtualAddress.toString(16)} exceeds Int.MAX_VALUE"
        }
        require(section.virtualSize <= Int.MAX_VALUE.toUInt()) {
            "Section '${section.name}' virtual size 0x${section.virtualSize.toString(16)} exceeds Int.MAX_VALUE"
        }
        require(section.pointerToRawData <= Int.MAX_VALUE.toUInt()) {
            "Section '${section.name}' pointer to raw data 0x${section.pointerToRawData.toString(16)} exceeds Int.MAX_VALUE"
        }
        require(section.sizeOfRawData <= Int.MAX_VALUE.toUInt()) {
            "Section '${section.name}' size of raw data 0x${section.sizeOfRawData.toString(16)} exceeds Int.MAX_VALUE"
        }

        val virtualAddress = section.virtualAddress.toInt()
        val virtualSize = section.virtualSize.toInt()
        val rawSize = section.sizeOfRawData.toInt()
        val rawDataStart = section.pointerToRawData.toInt()

        val mappedDataLength = minOf(virtualSize, rawSize)
        if (mappedDataLength > 0) {
            require(rawDataStart + mappedDataLength <= bytes.readableBytes()) {
                "Section '${section.name}' raw data [0x${rawDataStart.toString(16)}, 0x${(rawDataStart + mappedDataLength).toString(16)}) exceeds file size ${bytes.readableBytes()}"
            }
            chunks.add(MappedByteBuf.Chunk(
                offset = virtualAddress,
                length = mappedDataLength,
                buf = bytes.slice(rawDataStart, mappedDataLength),
            ))
        }

        val zeroFillLength = virtualSize - rawSize
        if (zeroFillLength > 0) {
            require(virtualAddress.toLong() + rawSize.toLong() + zeroFillLength.toLong() <= Int.MAX_VALUE) {
                "Section '${section.name}' zero-fill region overflow"
            }
            chunks.add(MappedByteBuf.Chunk(
                offset = virtualAddress + rawSize,
                length = zeroFillLength,
                buf = ZeroByteBuf(alloc, zeroFillLength),
            ))
        }
    }
    return MappedByteBuf(chunks)
}
