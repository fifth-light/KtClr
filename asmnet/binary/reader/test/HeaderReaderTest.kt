/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.test

import org.junit.Test
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.test.*

class HeaderReaderTest {
    @Test
    fun testReadDosHeader() {
        val buf = ByteBuffer.allocate(64).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x5A4D)      // e_magic
        buf.putShort(0x0090)      // e_cblp
        buf.putShort(0x0003)      // e_cp
        buf.putShort(0x0000)      // e_crlc
        buf.putShort(0x0004)      // e_cparhdr
        buf.putShort(0x0000)      // e_minalloc
        buf.putShort(0xFFFF.toShort()) // e_maxalloc
        buf.putShort(0x0000)      // e_ss
        buf.putShort(0x00B8)      // e_sp
        buf.putShort(0x0000)      // e_csum
        buf.putShort(0x0000)      // e_ip
        buf.putShort(0x0000)      // e_cs
        buf.putShort(0x0040)      // e_lfarlc
        buf.putShort(0x0000)      // e_ovno
        buf.putShort(0x0000)      // e_res[0]
        buf.putShort(0x0000)      // e_res[1]
        buf.putShort(0x0000)      // e_res[2]
        buf.putShort(0x0000)      // e_res[3]
        buf.putShort(0x0000)      // e_oemid
        buf.putShort(0x0000)      // e_oeminfo
        buf.putShort(0x0000)      // e_res2[0]
        buf.putShort(0x0000)      // e_res2[1]
        buf.putShort(0x0000)      // e_res2[2]
        buf.putShort(0x0000)      // e_res2[3]
        buf.putShort(0x0000)      // e_res2[4]
        buf.putShort(0x0000)      // e_res2[5]
        buf.putShort(0x0000)      // e_res2[6]
        buf.putShort(0x0000)      // e_res2[7]
        buf.putShort(0x0000)      // e_res2[8]
        buf.putShort(0x0000)      // e_res2[9]
        buf.putInt(0x00000080)    // e_lfanew
        buf.flip()

        val header = DosHeader(buf)
        assertEquals(DosHeader.MAGIC_MZ, header.e_magic)
        assertEquals(0x80u, header.e_lfanew)
    }

    @Test
    fun testReadDosHeaderInvalidMagic() {
        val buf = ByteBuffer.allocate(64).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x0000)
        buf.flip()

        assertFailsWith<IllegalArgumentException> {
            DosHeader(buf)
        }
    }

    @Test
    fun testReadDosHeaderBufferTooSmall() {
        val buf = ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x5A4D)
        buf.flip()

        assertFailsWith<IllegalArgumentException> {
            DosHeader(buf)
        }
    }

    @Test
    fun testReadCoffHeader() {
        val buf = ByteBuffer.allocate(20).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x8664.toShort())
        buf.putShort(3)
        buf.putInt(0x12345678)
        buf.putInt(0)
        buf.putInt(0)
        buf.putShort(0xF0.toShort())
        buf.putShort(0x0022)
        buf.flip()

        val header = CoffHeader(buf)
        assertEquals(MachineType.AMD64, header.machine)
        assertEquals(3u.toUShort(), header.numberOfSections)
        assertEquals(0x12345678u, header.timeDateStamp)
        assertEquals(0xF0u.toUShort(), header.sizeOfOptionalHeader)
        assertEquals(true, header.characteristics.isExecutableImage)
        assertEquals(false, header.characteristics.isDll)
    }

    @Test
    fun testReadCoffHeaderBufferTooSmall() {
        val buf = ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN)

        assertFailsWith<IllegalArgumentException> {
            CoffHeader(buf)
        }
    }

    @Test
    fun testReadOptionalHeaderPE32() {
        // PE32: 96 bytes header + 2 data directories (16 bytes) = 112 bytes
        val size = 96 + 2 * 8
        val buf = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x010B)      // Magic (PE32)
        buf.put(0x0A)             // MajorLinkerVersion
        buf.put(0x00)             // MinorLinkerVersion
        buf.putInt(0x00001000)    // SizeOfCode
        buf.putInt(0x00002000)    // SizeOfInitializedData
        buf.putInt(0x00000000)    // SizeOfUninitializedData
        buf.putInt(0x00001234)    // AddressOfEntryPoint
        buf.putInt(0x00001000)    // BaseOfCode
        buf.putInt(0x00002000)    // BaseOfData (PE32 only)
        buf.putInt(0x00400000)    // ImageBase (4B for PE32)
        buf.putInt(0x00001000)    // SectionAlignment
        buf.putInt(0x00000200)    // FileAlignment
        buf.putShort(6)           // MajorOperatingSystemVersion
        buf.putShort(0)           // MinorOperatingSystemVersion
        buf.putShort(0)           // MajorImageVersion
        buf.putShort(0)           // MinorImageVersion
        buf.putShort(6)           // MajorSubsystemVersion
        buf.putShort(0)           // MinorSubsystemVersion
        buf.putInt(0)             // Win32VersionValue
        buf.putInt(0x00003000)    // SizeOfImage
        buf.putInt(0x00000200)    // SizeOfHeaders
        buf.putInt(0)             // CheckSum
        buf.putShort(3)           // Subsystem (WINDOWS_CUI)
        buf.putShort(0xC160.toShort()) // DllCharacteristics (DYNAMIC_BASE | NX_COMPAT | TERMINAL_SERVER_AWARE | GUARD_CF)
        buf.putInt(0x00100000)    // SizeOfStackReserve (4B for PE32)
        buf.putInt(0x00001000)    // SizeOfStackCommit (4B for PE32)
        buf.putInt(0x00100000)    // SizeOfHeapReserve (4B for PE32)
        buf.putInt(0x00001000)    // SizeOfHeapCommit (4B for PE32)
        buf.putInt(0)             // LoaderFlags
        buf.putInt(2)             // NumberOfRvaAndSizes
        // Data Directory 0: Export Table
        buf.putInt(0)             // RVA
        buf.putInt(0)             // Size
        // Data Directory 1: Import Table
        buf.putInt(0x00005000)    // RVA
        buf.putInt(0x00000040)    // Size
        buf.flip()

        val header = OptionalHeader(buf)
        assertIs<OptionalHeader.PE32>(header)
        assertEquals(0x0Au.toUByte(), header.majorLinkerVersion)
        assertEquals(0x00400000uL, header.imageBase)
        assertEquals(0x00001000u, header.sectionAlignment)
        assertEquals(0x00000200u, header.fileAlignment)
        assertEquals(Subsystem.WINDOWS_CUI, header.subsystem)
        assertTrue(header.dllCharacteristics.isDynamicBase)
        assertTrue(header.dllCharacteristics.isNxCompat)
        assertTrue(header.dllCharacteristics.isGuardCf)
        assertTrue(header.dllCharacteristics.isTerminalServerAware)
        assertEquals(2, header.dataDirectories.size)
        assertEquals(0x00005000u, header.dataDirectories[1].rva)
        assertEquals(0x00000040u, header.dataDirectories[1].size)
    }

    @Test
    fun testReadOptionalHeaderPE32Plus() {
        // PE32+: 112 bytes header + 2 data directories (16 bytes) = 128 bytes
        val size = 112 + 2 * 8
        val buf = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x020B)      // Magic (PE32+)
        buf.put(0x0A)             // MajorLinkerVersion
        buf.put(0x00)             // MinorLinkerVersion
        buf.putInt(0x00001000)    // SizeOfCode
        buf.putInt(0x00002000)    // SizeOfInitializedData
        buf.putInt(0x00000000)    // SizeOfUninitializedData
        buf.putInt(0x00001234)    // AddressOfEntryPoint
        buf.putInt(0x00001000)    // BaseOfCode
        // No BaseOfData for PE32+
        buf.putLong(0x0000000180000000L) // ImageBase (8B for PE32+)
        buf.putInt(0x00001000)    // SectionAlignment
        buf.putInt(0x00000200)    // FileAlignment
        buf.putShort(6)           // MajorOperatingSystemVersion
        buf.putShort(0)           // MinorOperatingSystemVersion
        buf.putShort(0)           // MajorImageVersion
        buf.putShort(0)           // MinorImageVersion
        buf.putShort(6)           // MajorSubsystemVersion
        buf.putShort(0)           // MinorSubsystemVersion
        buf.putInt(0)             // Win32VersionValue
        buf.putInt(0x00003000)    // SizeOfImage
        buf.putInt(0x00000200)    // SizeOfHeaders
        buf.putInt(0)             // CheckSum
        buf.putShort(3)           // Subsystem (WINDOWS_CUI)
        buf.putShort(0xC160.toShort()) // DllCharacteristics (DYNAMIC_BASE | NX_COMPAT | TERMINAL_SERVER_AWARE | GUARD_CF)
        buf.putLong(0x000001000000L) // SizeOfStackReserve (8B for PE32+)
        buf.putLong(0x00000010000L) // SizeOfStackCommit (8B for PE32+)
        buf.putLong(0x000001000000L) // SizeOfHeapReserve (8B for PE32+)
        buf.putLong(0x00000010000L) // SizeOfHeapCommit (8B for PE32+)
        buf.putInt(0)             // LoaderFlags
        buf.putInt(2)             // NumberOfRvaAndSizes
        // Data Directory 0
        buf.putInt(0)             // RVA
        buf.putInt(0)             // Size
        // Data Directory 1
        buf.putInt(0x00005000)    // RVA
        buf.putInt(0x00000040)    // Size
        buf.flip()

        val header = OptionalHeader(buf)
        assertIs<OptionalHeader.PE32Plus>(header)
        assertEquals(0x0000000180000000uL, header.imageBase)
        assertEquals(0x000001000000uL, header.sizeOfStackReserve)
        assertTrue(header.dllCharacteristics.isDynamicBase)
        assertTrue(header.dllCharacteristics.isNxCompat)
        assertTrue(header.dllCharacteristics.isGuardCf)
        assertTrue(header.dllCharacteristics.isTerminalServerAware)
        assertEquals(2, header.dataDirectories.size)
        assertEquals(0x00005000u, header.dataDirectories[1].rva)
    }

    @Test
    fun testReadOptionalHeaderInvalidMagic() {
        val buf = ByteBuffer.allocate(96).order(ByteOrder.LITTLE_ENDIAN)
        buf.putShort(0x0000)      // Invalid magic
        buf.flip()

        assertFailsWith<IllegalArgumentException> {
            OptionalHeader(buf)
        }
    }

    @Test
    fun testReadSectionHeader() {
        val buf = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN)
        val nameBytes = byteArrayOf(0x2E, 0x74, 0x65, 0x78, 0x74, 0, 0, 0) // ".text\0\0\0"
        buf.put(nameBytes)
        buf.putInt(0x00000100)    // VirtualSize
        buf.putInt(0x00002000)    // VirtualAddress
        buf.putInt(0x00000200)    // SizeOfRawData
        buf.putInt(0x00000400)    // PointerToRawData
        buf.putInt(0)             // PointerToRelocations
        buf.putInt(0)             // PointerToLinenumbers
        buf.putShort(0)           // NumberOfRelocations
        buf.putShort(0)           // NumberOfLinenumbers
        buf.putInt(0x60000020)  // Characteristics: CNT_CODE | MEM_EXECUTE | MEM_READ
        buf.flip()

        val header = SectionHeader(buf)
        assertEquals(".text", header.name)
        assertEquals(0x00000100u, header.virtualSize)
        assertEquals(0x00002000u, header.virtualAddress)
        assertEquals(0x00000200u, header.sizeOfRawData)
        assertEquals(0x00000400u, header.pointerToRawData)
        assertEquals(0u, header.pointerToRelocations)
        assertEquals(0u, header.pointerToLinenumbers)
        assertEquals(0u.toUShort(), header.numberOfRelocations)
        assertEquals(0u.toUShort(), header.numberOfLinenumbers)
        assertEquals(SectionFlags(0x60000020u), header.characteristics)
        assertTrue(header.characteristics.containsCode)
        assertTrue(header.characteristics.isExecute)
        assertTrue(header.characteristics.isRead)
        assertFalse(header.characteristics.isWrite)
        assertFalse(header.characteristics.isDiscardable)
    }

    @Test
    fun testReadSectionHeaderFull8CharName() {
        val buf = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN)
        buf.put(".textx00".toByteArray(Charsets.UTF_8))  // Name (exactly 8 chars, no null terminator)
        buf.putInt(0x00000100)
        buf.putInt(0x00002000)
        buf.putInt(0x00000200)
        buf.putInt(0x00000400)
        buf.putInt(0)
        buf.putInt(0)
        buf.putShort(0)
        buf.putShort(0)
        buf.putInt(0x40000040)
        buf.flip()

        val header = SectionHeader(buf)
        assertEquals(".textx00", header.name)
        assertTrue(header.characteristics.containsInitializedData)
        assertTrue(header.characteristics.isRead)
    }

    @Test
    fun testReadSectionHeaderBufferTooSmall() {
        val buf = ByteBuffer.allocate(20).order(ByteOrder.LITTLE_ENDIAN)

        assertFailsWith<IllegalArgumentException> {
            SectionHeader(buf)
        }
    }

    @Test
    fun testReadCliHeader() {
        val buf = ByteBuffer.allocate(72).order(ByteOrder.LITTLE_ENDIAN)
        buf.putInt(0x48)          // cb = 72
        buf.putShort(2)           // majorRuntimeVersion
        buf.putShort(5)           // minorRuntimeVersion
        buf.putInt(0x00002008)    // metaData.rva
        buf.putInt(0x00000100)    // metaData.size
        buf.putInt(0x00000001)    // flags = ILONLY
        buf.putInt(0x06000001)    // entryPointToken
        buf.putInt(0x00003000)    // resources.rva
        buf.putInt(0x00000200)    // resources.size
        buf.putInt(0x00004000)    // strongNameSignature.rva
        buf.putInt(0x00000100)    // strongNameSignature.size
        buf.putInt(0)             // codeManagerTable.rva
        buf.putInt(0)             // codeManagerTable.size
        buf.putInt(0x00005000)    // vTableFixups.rva
        buf.putInt(0x00000030)    // vTableFixups.size
        buf.putInt(0)             // exportAddressTableJumps.rva
        buf.putInt(0)             // exportAddressTableJumps.size
        buf.putInt(0)             // managedNativeHeader.rva
        buf.putInt(0)             // managedNativeHeader.size
        buf.flip()

        val header = CliHeader(buf)
        assertEquals(0x48u, header.cb)
        assertEquals(2u.toUShort(), header.majorRuntimeVersion)
        assertEquals(5u.toUShort(), header.minorRuntimeVersion)
        assertEquals(0x00002008u, header.metaData.rva)
        assertEquals(0x00000100u, header.metaData.size)
        assertEquals(0x00000001u, header.flags)
        assertEquals(0x06000001u, header.entryPointToken)
        assertEquals(0x00003000u, header.resources.rva)
        assertEquals(0x00000200u, header.resources.size)
        assertEquals(0x00004000u, header.strongNameSignature.rva)
        assertEquals(0x00000100u, header.strongNameSignature.size)
        assertEquals(0u, header.codeManagerTable.rva)
        assertEquals(0u, header.codeManagerTable.size)
        assertEquals(0x00005000u, header.vTableFixups.rva)
        assertEquals(0x00000030u, header.vTableFixups.size)
        assertEquals(0u, header.exportAddressTableJumps.rva)
        assertEquals(0u, header.exportAddressTableJumps.size)
        assertEquals(0u, header.managedNativeHeader.rva)
        assertEquals(0u, header.managedNativeHeader.size)
    }

    @Test
    fun testReadCliHeaderBufferTooSmall() {
        val buf = ByteBuffer.allocate(71).order(ByteOrder.LITTLE_ENDIAN)

        assertFailsWith<IllegalArgumentException> {
            CliHeader(buf)
        }
    }

    @Test
    fun testReadMetadataRoot() {
        val versionString = "Standard"
        val versionBytes = versionString.toByteArray(Charsets.UTF_8)
        val versionWithNull = versionBytes.size + 1
        val versionPadded = (versionWithNull + 3) / 4 * 4

        val streamNames = listOf("#~", "#Strings", "#Blob", "#GUID", "#US")
        val streamData = streamNames.mapIndexed { i, name ->
            StreamHeader(
                offset = (0x100u + i.toUInt() * 0x50u),
                size = (0x40u + i.toUInt() * 0x10u),
                name = name,
            )
        }

        val streamHeadersSize = streamData.sumOf { header ->
            val nameWithNull = header.name.toByteArray(Charsets.US_ASCII).size + 1
            val padded = (nameWithNull + 3) / 4 * 4
            8 + padded
        }

        val totalSize = 12 + 4 + versionPadded + 4 + streamHeadersSize
        val buf = ByteBuffer.allocate(totalSize).order(ByteOrder.LITTLE_ENDIAN)

        buf.putInt(0x424A5342)       // Signature "BSJB"
        buf.putShort(1)               // MajorVersion
        buf.putShort(1)               // MinorVersion
        buf.putInt(0)                  // Reserved
        buf.putInt(versionPadded)      // Length (x), padded
        buf.put(versionBytes)         // Version string
        buf.put(0.toByte())           // Null terminator
        repeat(versionPadded - versionWithNull) {
            buf.put(0.toByte())       // Padding to 4-byte boundary
        }
        buf.putShort(0)               // Flags
        buf.putShort(streamNames.size.toShort()) // Streams count

        for (header in streamData) {
            buf.putInt(header.offset.toInt())
            buf.putInt(header.size.toInt())
            val nameBytes = header.name.toByteArray(Charsets.US_ASCII)
            buf.put(nameBytes)
            buf.put(0.toByte())
            val nameWithNull = nameBytes.size + 1
            val padded = (nameWithNull + 3) / 4 * 4
            repeat(padded - nameWithNull) {
                buf.put(0.toByte())
            }
        }
        buf.flip()

        val root = MetadataRoot(buf)
        assertEquals(MetadataRoot.SIGNATURE, root.signature)
        assertEquals(1u.toUShort(), root.majorVersion)
        assertEquals(1u.toUShort(), root.minorVersion)
        assertEquals(versionString, root.version)
        assertEquals(0u.toUShort(), root.flags)
        assertEquals(5, root.streams.size)

        assertEquals("#~", root.streams[0].name)
        assertEquals(0x100u, root.streams[0].offset)
        assertEquals(0x40u, root.streams[0].size)

        assertEquals("#Strings", root.streams[1].name)
        assertEquals(0x150u, root.streams[1].offset)
        assertEquals(0x50u, root.streams[1].size)

        assertEquals("#Blob", root.streams[2].name)
        assertEquals(0x1A0u, root.streams[2].offset)
        assertEquals(0x60u, root.streams[2].size)

        assertEquals("#GUID", root.streams[3].name)
        assertEquals(0x1F0u, root.streams[3].offset)
        assertEquals(0x70u, root.streams[3].size)

        assertEquals("#US", root.streams[4].name)
        assertEquals(0x240u, root.streams[4].offset)
        assertEquals(0x80u, root.streams[4].size)
    }

    @Test
    fun testReadMetadataRootInvalidSignature() {
        val buf = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
        buf.putInt(0xDEADBEEF.toInt())
        buf.putShort(1)
        buf.putShort(1)
        buf.putInt(0)
        buf.flip()

        assertFailsWith<IllegalArgumentException> {
            MetadataRoot(buf)
        }
    }

    @Test
    fun testReadMetadataRootBufferTooSmall() {
        val buf = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
        buf.putInt(0x424A5342)
        buf.putInt(0)
        buf.flip()

        assertFailsWith<IllegalArgumentException> {
            MetadataRoot(buf)
        }
    }
}
