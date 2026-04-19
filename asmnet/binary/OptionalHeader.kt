package top.fifthlight.asmnet.binary

import top.fifthlight.asmnet.Subsystem

// ECMA-335 II.25.2.3, PE Format § Optional Header
sealed interface OptionalHeader {
    val majorLinkerVersion: UByte
    val minorLinkerVersion: UByte
    val sizeOfCode: UInt
    val sizeOfInitializedData: UInt
    val sizeOfUninitializedData: UInt
    val addressOfEntryPoint: UInt
    val baseOfCode: UInt
    val imageBase: ULong
    val sectionAlignment: UInt
    val fileAlignment: UInt
    val majorOperatingSystemVersion: UShort
    val minorOperatingSystemVersion: UShort
    val majorImageVersion: UShort
    val minorImageVersion: UShort
    val majorSubsystemVersion: UShort
    val minorSubsystemVersion: UShort
    val win32VersionValue: UInt
    val sizeOfImage: UInt
    val sizeOfHeaders: UInt
    val checksum: UInt
    val subsystem: Subsystem
    val dllCharacteristics: DllCharacteristics
    val sizeOfStackReserve: ULong
    val sizeOfStackCommit: ULong
    val sizeOfHeapReserve: ULong
    val sizeOfHeapCommit: ULong
    val loaderFlags: UInt
    val dataDirectories: List<DataDirectory>

    data class PE32(
        override val majorLinkerVersion: UByte,
        override val minorLinkerVersion: UByte,
        override val sizeOfCode: UInt,
        override val sizeOfInitializedData: UInt,
        override val sizeOfUninitializedData: UInt,
        override val addressOfEntryPoint: UInt,
        override val baseOfCode: UInt,
        val baseOfData: UInt,
        override val imageBase: ULong,
        override val sectionAlignment: UInt,
        override val fileAlignment: UInt,
        override val majorOperatingSystemVersion: UShort,
        override val minorOperatingSystemVersion: UShort,
        override val majorImageVersion: UShort,
        override val minorImageVersion: UShort,
        override val majorSubsystemVersion: UShort,
        override val minorSubsystemVersion: UShort,
        override val win32VersionValue: UInt,
        override val sizeOfImage: UInt,
        override val sizeOfHeaders: UInt,
        override val checksum: UInt,
        override val subsystem: Subsystem,
        override val dllCharacteristics: DllCharacteristics,
        override val sizeOfStackReserve: ULong,
        override val sizeOfStackCommit: ULong,
        override val sizeOfHeapReserve: ULong,
        override val sizeOfHeapCommit: ULong,
        override val loaderFlags: UInt,
        override val dataDirectories: List<DataDirectory>,
    ) : OptionalHeader

    data class PE32Plus(
        override val majorLinkerVersion: UByte,
        override val minorLinkerVersion: UByte,
        override val sizeOfCode: UInt,
        override val sizeOfInitializedData: UInt,
        override val sizeOfUninitializedData: UInt,
        override val addressOfEntryPoint: UInt,
        override val baseOfCode: UInt,
        override val imageBase: ULong,
        override val sectionAlignment: UInt,
        override val fileAlignment: UInt,
        override val majorOperatingSystemVersion: UShort,
        override val minorOperatingSystemVersion: UShort,
        override val majorImageVersion: UShort,
        override val minorImageVersion: UShort,
        override val majorSubsystemVersion: UShort,
        override val minorSubsystemVersion: UShort,
        override val win32VersionValue: UInt,
        override val sizeOfImage: UInt,
        override val sizeOfHeaders: UInt,
        override val checksum: UInt,
        override val subsystem: Subsystem,
        override val dllCharacteristics: DllCharacteristics,
        override val sizeOfStackReserve: ULong,
        override val sizeOfStackCommit: ULong,
        override val sizeOfHeapReserve: ULong,
        override val sizeOfHeapCommit: ULong,
        override val loaderFlags: UInt,
        override val dataDirectories: List<DataDirectory>,
    ) : OptionalHeader

    companion object {
        const val MAGIC_PE32: UShort = 0x010Bu
        const val MAGIC_PE32_PLUS: UShort = 0x020Bu
    }
}
