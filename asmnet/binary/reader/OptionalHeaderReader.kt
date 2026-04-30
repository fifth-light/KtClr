/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.util.readUByteLE
import top.fifthlight.asmnet.binary.reader.util.readUIntLE
import top.fifthlight.asmnet.binary.reader.util.readULongLE
import top.fifthlight.asmnet.binary.reader.util.readUShortLE

private fun readDataDirectories(buf: ByteBuf, count: UInt): List<DataDirectory> = buildList {
    require(count < 128u) { "Too many data directories: $count" }
    require(buf.readableBytes() >= count.toInt() * 8) { "Buffer size ${buf.readableBytes()} too small for $count data directories" }
    repeat(count.toInt()) {
        add(DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()))
    }
}

private fun readPE32(buf: ByteBuf): OptionalHeader.PE32 = OptionalHeader.PE32(
    majorLinkerVersion = buf.readUByteLE(),
    minorLinkerVersion = buf.readUByteLE(),
    sizeOfCode = buf.readUIntLE(),
    sizeOfInitializedData = buf.readUIntLE(),
    sizeOfUninitializedData = buf.readUIntLE(),
    addressOfEntryPoint = buf.readUIntLE(),
    baseOfCode = buf.readUIntLE(),
    baseOfData = buf.readUIntLE(),
    imageBase = buf.readUIntLE().toULong(),
    sectionAlignment = buf.readUIntLE(),
    fileAlignment = buf.readUIntLE(),
    majorOperatingSystemVersion = buf.readUShortLE(),
    minorOperatingSystemVersion = buf.readUShortLE(),
    majorImageVersion = buf.readUShortLE(),
    minorImageVersion = buf.readUShortLE(),
    majorSubsystemVersion = buf.readUShortLE(),
    minorSubsystemVersion = buf.readUShortLE(),
    win32VersionValue = buf.readUIntLE(),
    sizeOfImage = buf.readUIntLE(),
    sizeOfHeaders = buf.readUIntLE(),
    checksum = buf.readUIntLE(),
    subsystem = Subsystem(buf.readShortLE()),
    dllCharacteristics = DllCharacteristics(buf.readUShortLE()),
    sizeOfStackReserve = buf.readUIntLE().toULong(),
    sizeOfStackCommit = buf.readUIntLE().toULong(),
    sizeOfHeapReserve = buf.readUIntLE().toULong(),
    sizeOfHeapCommit = buf.readUIntLE().toULong(),
    loaderFlags = buf.readUIntLE(),
    dataDirectories = readDataDirectories(buf, buf.readUIntLE()),
)

private fun readPE32Plus(buf: ByteBuf): OptionalHeader.PE32Plus = OptionalHeader.PE32Plus(
    majorLinkerVersion = buf.readUByteLE(),
    minorLinkerVersion = buf.readUByteLE(),
    sizeOfCode = buf.readUIntLE(),
    sizeOfInitializedData = buf.readUIntLE(),
    sizeOfUninitializedData = buf.readUIntLE(),
    addressOfEntryPoint = buf.readUIntLE(),
    baseOfCode = buf.readUIntLE(),
    imageBase = buf.readULongLE(),
    sectionAlignment = buf.readUIntLE(),
    fileAlignment = buf.readUIntLE(),
    majorOperatingSystemVersion = buf.readUShortLE(),
    minorOperatingSystemVersion = buf.readUShortLE(),
    majorImageVersion = buf.readUShortLE(),
    minorImageVersion = buf.readUShortLE(),
    majorSubsystemVersion = buf.readUShortLE(),
    minorSubsystemVersion = buf.readUShortLE(),
    win32VersionValue = buf.readUIntLE(),
    sizeOfImage = buf.readUIntLE(),
    sizeOfHeaders = buf.readUIntLE(),
    checksum = buf.readUIntLE(),
    subsystem = Subsystem(buf.readShortLE()),
    dllCharacteristics = DllCharacteristics(buf.readUShortLE()),
    sizeOfStackReserve = buf.readULongLE(),
    sizeOfStackCommit = buf.readULongLE(),
    sizeOfHeapReserve = buf.readULongLE(),
    sizeOfHeapCommit = buf.readULongLE(),
    loaderFlags = buf.readUIntLE(),
    dataDirectories = readDataDirectories(buf, buf.readUIntLE()),
)

internal fun OptionalHeader(buffer: ByteBuf): OptionalHeader = buffer.slice().let { buf ->
    when (val magic = buf.readUShortLE()) {
        OptionalHeader.MAGIC_PE32 -> {
            require(buf.readableBytes() >= 94) { "Buffer too small for PE32 optional header: ${buf.readableBytes()} < 94" }
            readPE32(buf)
        }
        OptionalHeader.MAGIC_PE32_PLUS -> {
            require(buf.readableBytes() >= 110) { "Buffer too small for PE32+ optional header: ${buf.readableBytes()} < 110" }
            readPE32Plus(buf)
        }
        else -> throw IllegalArgumentException("Invalid optional header magic: 0x${magic.toString(16)}")
    }
}
