package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.DataDirectory
import top.fifthlight.asmnet.binary.DllCharacteristics
import top.fifthlight.asmnet.binary.OptionalHeader
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.ubyte
import top.fifthlight.asmnet.binary.uint
import top.fifthlight.asmnet.binary.ulong
import top.fifthlight.asmnet.binary.ushort
import java.nio.ByteBuffer
import java.nio.ByteOrder

private fun readDataDirectories(buf: ByteBuffer, count: UInt): List<DataDirectory> = buildList {
    repeat(count.toInt()) {
        add(DataDirectory(rva = buf.uint, size = buf.uint))
    }
}

private fun readPE32(buf: ByteBuffer): OptionalHeader.PE32 {
    val majorLinkerVersion = buf.ubyte
    val minorLinkerVersion = buf.ubyte
    val sizeOfCode = buf.uint
    val sizeOfInitializedData = buf.uint
    val sizeOfUninitializedData = buf.uint
    val addressOfEntryPoint = buf.uint
    val baseOfCode = buf.uint
    val baseOfData = buf.uint
    val imageBase = buf.uint.toULong()
    val sectionAlignment = buf.uint
    val fileAlignment = buf.uint
    val majorOperatingSystemVersion = buf.ushort
    val minorOperatingSystemVersion = buf.ushort
    val majorImageVersion = buf.ushort
    val minorImageVersion = buf.ushort
    val majorSubsystemVersion = buf.ushort
    val minorSubsystemVersion = buf.ushort
    val win32VersionValue = buf.uint
    val sizeOfImage = buf.uint
    val sizeOfHeaders = buf.uint
    val checksum = buf.uint
    val subsystem = Subsystem(buf.short)
    val dllCharacteristics = DllCharacteristics(buf.ushort)
    val sizeOfStackReserve = buf.uint.toULong()
    val sizeOfStackCommit = buf.uint.toULong()
    val sizeOfHeapReserve = buf.uint.toULong()
    val sizeOfHeapCommit = buf.uint.toULong()
    val loaderFlags = buf.uint
    val numberOfRvaAndSizes = buf.uint
    val dataDirectories = readDataDirectories(buf, numberOfRvaAndSizes)
    return OptionalHeader.PE32(
        majorLinkerVersion = majorLinkerVersion,
        minorLinkerVersion = minorLinkerVersion,
        sizeOfCode = sizeOfCode,
        sizeOfInitializedData = sizeOfInitializedData,
        sizeOfUninitializedData = sizeOfUninitializedData,
        addressOfEntryPoint = addressOfEntryPoint,
        baseOfCode = baseOfCode,
        baseOfData = baseOfData,
        imageBase = imageBase,
        sectionAlignment = sectionAlignment,
        fileAlignment = fileAlignment,
        majorOperatingSystemVersion = majorOperatingSystemVersion,
        minorOperatingSystemVersion = minorOperatingSystemVersion,
        majorImageVersion = majorImageVersion,
        minorImageVersion = minorImageVersion,
        majorSubsystemVersion = majorSubsystemVersion,
        minorSubsystemVersion = minorSubsystemVersion,
        win32VersionValue = win32VersionValue,
        sizeOfImage = sizeOfImage,
        sizeOfHeaders = sizeOfHeaders,
        checksum = checksum,
        subsystem = subsystem,
        dllCharacteristics = dllCharacteristics,
        sizeOfStackReserve = sizeOfStackReserve,
        sizeOfStackCommit = sizeOfStackCommit,
        sizeOfHeapReserve = sizeOfHeapReserve,
        sizeOfHeapCommit = sizeOfHeapCommit,
        loaderFlags = loaderFlags,
        numberOfRvaAndSizes = numberOfRvaAndSizes,
        dataDirectories = dataDirectories,
    )
}

private fun readPE32Plus(buf: ByteBuffer): OptionalHeader.PE32Plus {
    val majorLinkerVersion = buf.ubyte
    val minorLinkerVersion = buf.ubyte
    val sizeOfCode = buf.uint
    val sizeOfInitializedData = buf.uint
    val sizeOfUninitializedData = buf.uint
    val addressOfEntryPoint = buf.uint
    val baseOfCode = buf.uint
    val imageBase = buf.ulong
    val sectionAlignment = buf.uint
    val fileAlignment = buf.uint
    val majorOperatingSystemVersion = buf.ushort
    val minorOperatingSystemVersion = buf.ushort
    val majorImageVersion = buf.ushort
    val minorImageVersion = buf.ushort
    val majorSubsystemVersion = buf.ushort
    val minorSubsystemVersion = buf.ushort
    val win32VersionValue = buf.uint
    val sizeOfImage = buf.uint
    val sizeOfHeaders = buf.uint
    val checksum = buf.uint
    val subsystem = Subsystem(buf.short)
    val dllCharacteristics = DllCharacteristics(buf.ushort)
    val sizeOfStackReserve = buf.ulong
    val sizeOfStackCommit = buf.ulong
    val sizeOfHeapReserve = buf.ulong
    val sizeOfHeapCommit = buf.ulong
    val loaderFlags = buf.uint
    val numberOfRvaAndSizes = buf.uint
    val dataDirectories = readDataDirectories(buf, numberOfRvaAndSizes)
    return OptionalHeader.PE32Plus(
        majorLinkerVersion = majorLinkerVersion,
        minorLinkerVersion = minorLinkerVersion,
        sizeOfCode = sizeOfCode,
        sizeOfInitializedData = sizeOfInitializedData,
        sizeOfUninitializedData = sizeOfUninitializedData,
        addressOfEntryPoint = addressOfEntryPoint,
        baseOfCode = baseOfCode,
        imageBase = imageBase,
        sectionAlignment = sectionAlignment,
        fileAlignment = fileAlignment,
        majorOperatingSystemVersion = majorOperatingSystemVersion,
        minorOperatingSystemVersion = minorOperatingSystemVersion,
        majorImageVersion = majorImageVersion,
        minorImageVersion = minorImageVersion,
        majorSubsystemVersion = majorSubsystemVersion,
        minorSubsystemVersion = minorSubsystemVersion,
        win32VersionValue = win32VersionValue,
        sizeOfImage = sizeOfImage,
        sizeOfHeaders = sizeOfHeaders,
        checksum = checksum,
        subsystem = subsystem,
        dllCharacteristics = dllCharacteristics,
        sizeOfStackReserve = sizeOfStackReserve,
        sizeOfStackCommit = sizeOfStackCommit,
        sizeOfHeapReserve = sizeOfHeapReserve,
        sizeOfHeapCommit = sizeOfHeapCommit,
        loaderFlags = loaderFlags,
        numberOfRvaAndSizes = numberOfRvaAndSizes,
        dataDirectories = dataDirectories,
    )
}

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
