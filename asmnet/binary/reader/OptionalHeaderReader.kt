package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.DataDirectory
import top.fifthlight.asmnet.binary.DllCharacteristics
import top.fifthlight.asmnet.binary.OptionalHeader
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.uint
import top.fifthlight.asmnet.binary.ulong
import top.fifthlight.asmnet.binary.ubyte
import top.fifthlight.asmnet.binary.ushort
import java.nio.ByteBuffer
import java.nio.ByteOrder

private fun readDataDirectories(buf: ByteBuffer, count: UInt): List<DataDirectory> = buildList {
    repeat(count.toInt()) {
        add(DataDirectory(rva = buf.uint, size = buf.uint))
    }
}

private fun readPE32(buf: ByteBuffer): OptionalHeader.PE32 = OptionalHeader.PE32(
    majorLinkerVersion = buf.ubyte,
    minorLinkerVersion = buf.ubyte,
    sizeOfCode = buf.uint,
    sizeOfInitializedData = buf.uint,
    sizeOfUninitializedData = buf.uint,
    addressOfEntryPoint = buf.uint,
    baseOfCode = buf.uint,
    baseOfData = buf.uint,
    imageBase = buf.uint.toULong(),
    sectionAlignment = buf.uint,
    fileAlignment = buf.uint,
    majorOperatingSystemVersion = buf.ushort,
    minorOperatingSystemVersion = buf.ushort,
    majorImageVersion = buf.ushort,
    minorImageVersion = buf.ushort,
    majorSubsystemVersion = buf.ushort,
    minorSubsystemVersion = buf.ushort,
    win32VersionValue = buf.uint,
    sizeOfImage = buf.uint,
    sizeOfHeaders = buf.uint,
    checksum = buf.uint,
    subsystem = Subsystem(buf.short),
    dllCharacteristics = DllCharacteristics(buf.ushort),
    sizeOfStackReserve = buf.uint.toULong(),
    sizeOfStackCommit = buf.uint.toULong(),
    sizeOfHeapReserve = buf.uint.toULong(),
    sizeOfHeapCommit = buf.uint.toULong(),
    loaderFlags = buf.uint,
    dataDirectories = readDataDirectories(buf, buf.uint),
)

private fun readPE32Plus(buf: ByteBuffer): OptionalHeader.PE32Plus = OptionalHeader.PE32Plus(
    majorLinkerVersion = buf.ubyte,
    minorLinkerVersion = buf.ubyte,
    sizeOfCode = buf.uint,
    sizeOfInitializedData = buf.uint,
    sizeOfUninitializedData = buf.uint,
    addressOfEntryPoint = buf.uint,
    baseOfCode = buf.uint,
    imageBase = buf.ulong,
    sectionAlignment = buf.uint,
    fileAlignment = buf.uint,
    majorOperatingSystemVersion = buf.ushort,
    minorOperatingSystemVersion = buf.ushort,
    majorImageVersion = buf.ushort,
    minorImageVersion = buf.ushort,
    majorSubsystemVersion = buf.ushort,
    minorSubsystemVersion = buf.ushort,
    win32VersionValue = buf.uint,
    sizeOfImage = buf.uint,
    sizeOfHeaders = buf.uint,
    checksum = buf.uint,
    subsystem = Subsystem(buf.short),
    dllCharacteristics = DllCharacteristics(buf.ushort),
    sizeOfStackReserve = buf.ulong,
    sizeOfStackCommit = buf.ulong,
    sizeOfHeapReserve = buf.ulong,
    sizeOfHeapCommit = buf.ulong,
    loaderFlags = buf.uint,
    dataDirectories = readDataDirectories(buf, buf.uint),
)

internal fun OptionalHeader(buffer: ByteBuffer): OptionalHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    val magic = buf.ushort
    when (magic) {
        OptionalHeader.MAGIC_PE32 -> {
            require(buf.remaining() >= 94) { "Buffer too small for PE32 optional header: ${buf.remaining()} < 94" }
            readPE32(buf)
        }
        OptionalHeader.MAGIC_PE32_PLUS -> {
            require(buf.remaining() >= 110) { "Buffer too small for PE32+ optional header: ${buf.remaining()} < 110" }
            readPE32Plus(buf)
        }
        else -> throw IllegalArgumentException("Invalid optional header magic: 0x${magic.toString(16)}")
    }
}
