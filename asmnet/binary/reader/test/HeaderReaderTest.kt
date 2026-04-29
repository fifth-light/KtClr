/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader.test

import io.netty.buffer.Unpooled
import org.junit.Test
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.*
import kotlin.test.*

class HeaderReaderTest {
    @Test
    fun testReadDosHeader() {
        val buf = Unpooled.buffer(64)
        buf.writeShortLE(0x5A4D)      // e_magic
        buf.writeShortLE(0x0090)      // e_cblp
        buf.writeShortLE(0x0003)      // e_cp
        buf.writeShortLE(0x0000)      // e_crlc
        buf.writeShortLE(0x0004)      // e_cparhdr
        buf.writeShortLE(0x0000)      // e_minalloc
        buf.writeShortLE(0xFFFF)      // e_maxalloc
        buf.writeShortLE(0x0000)      // e_ss
        buf.writeShortLE(0x00B8)      // e_sp
        buf.writeShortLE(0x0000)      // e_csum
        buf.writeShortLE(0x0000)      // e_ip
        buf.writeShortLE(0x0000)      // e_cs
        buf.writeShortLE(0x0040)      // e_lfarlc
        buf.writeShortLE(0x0000)      // e_ovno
        buf.writeShortLE(0x0000)      // e_res[0]
        buf.writeShortLE(0x0000)      // e_res[1]
        buf.writeShortLE(0x0000)      // e_res[2]
        buf.writeShortLE(0x0000)      // e_res[3]
        buf.writeShortLE(0x0000)      // e_oemid
        buf.writeShortLE(0x0000)      // e_oeminfo
        buf.writeShortLE(0x0000)      // e_res2[0]
        buf.writeShortLE(0x0000)      // e_res2[1]
        buf.writeShortLE(0x0000)      // e_res2[2]
        buf.writeShortLE(0x0000)      // e_res2[3]
        buf.writeShortLE(0x0000)      // e_res2[4]
        buf.writeShortLE(0x0000)      // e_res2[5]
        buf.writeShortLE(0x0000)      // e_res2[6]
        buf.writeShortLE(0x0000)      // e_res2[7]
        buf.writeShortLE(0x0000)      // e_res2[8]
        buf.writeShortLE(0x0000)      // e_res2[9]
        buf.writeIntLE(0x00000080)    // e_lfanew

        val header = DosHeader(buf)
        assertEquals(DosHeader.MAGIC_MZ, header.e_magic)
        assertEquals(0x80u, header.e_lfanew)
    }

    @Test
    fun testReadDosHeaderInvalidMagic() {
        val buf = Unpooled.buffer(64)
        buf.writeShortLE(0x0000)

        assertFailsWith<IllegalArgumentException> {
            DosHeader(buf)
        }
    }

    @Test
    fun testReadDosHeaderBufferTooSmall() {
        val buf = Unpooled.buffer(32)
        buf.writeShortLE(0x5A4D)

        assertFailsWith<IllegalArgumentException> {
            DosHeader(buf)
        }
    }

    @Test
    fun testReadCoffHeader() {
        val buf = Unpooled.buffer(20)
        buf.writeShortLE(0x8664)
        buf.writeShortLE(3)
        buf.writeIntLE(0x12345678)
        buf.writeIntLE(0)
        buf.writeIntLE(0)
        buf.writeShortLE(0xF0)
        buf.writeShortLE(0x0022)

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
        val buf = Unpooled.buffer(10)

        assertFailsWith<IllegalArgumentException> {
            CoffHeader(buf)
        }
    }

    @Test
    fun testReadOptionalHeaderPE32() {
        // PE32: 96 bytes header + 2 data directories (16 bytes) = 112 bytes
        val size = 96 + 2 * 8
        val buf = Unpooled.buffer(size)
        buf.writeShortLE(0x010B)      // Magic (PE32)
        buf.writeByte(0x0A)           // MajorLinkerVersion
        buf.writeByte(0x00)           // MinorLinkerVersion
        buf.writeIntLE(0x00001000)    // SizeOfCode
        buf.writeIntLE(0x00002000)    // SizeOfInitializedData
        buf.writeIntLE(0x00000000)    // SizeOfUninitializedData
        buf.writeIntLE(0x00001234)    // AddressOfEntryPoint
        buf.writeIntLE(0x00001000)    // BaseOfCode
        buf.writeIntLE(0x00002000)    // BaseOfData (PE32 only)
        buf.writeIntLE(0x00400000)    // ImageBase (4B for PE32)
        buf.writeIntLE(0x00001000)    // SectionAlignment
        buf.writeIntLE(0x00000200)    // FileAlignment
        buf.writeShortLE(6)           // MajorOperatingSystemVersion
        buf.writeShortLE(0)           // MinorOperatingSystemVersion
        buf.writeShortLE(0)           // MajorImageVersion
        buf.writeShortLE(0)           // MinorImageVersion
        buf.writeShortLE(6)           // MajorSubsystemVersion
        buf.writeShortLE(0)           // MinorSubsystemVersion
        buf.writeIntLE(0)             // Win32VersionValue
        buf.writeIntLE(0x00003000)    // SizeOfImage
        buf.writeIntLE(0x00000200)    // SizeOfHeaders
        buf.writeIntLE(0)             // CheckSum
        buf.writeShortLE(3)           // Subsystem (WINDOWS_CUI)
        buf.writeShortLE(0xC160)      // DllCharacteristics (DYNAMIC_BASE | NX_COMPAT | TERMINAL_SERVER_AWARE | GUARD_CF)
        buf.writeIntLE(0x00100000)    // SizeOfStackReserve (4B for PE32)
        buf.writeIntLE(0x00001000)    // SizeOfStackCommit (4B for PE32)
        buf.writeIntLE(0x00100000)    // SizeOfHeapReserve (4B for PE32)
        buf.writeIntLE(0x00001000)    // SizeOfHeapCommit (4B for PE32)
        buf.writeIntLE(0)             // LoaderFlags
        buf.writeIntLE(2)             // NumberOfRvaAndSizes
        // Data Directory 0: Export Table
        buf.writeIntLE(0)             // RVA
        buf.writeIntLE(0)             // Size
        // Data Directory 1: Import Table
        buf.writeIntLE(0x00005000)    // RVA
        buf.writeIntLE(0x00000040)    // Size

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
        val buf = Unpooled.buffer(size)
        buf.writeShortLE(0x020B)      // Magic (PE32+)
        buf.writeByte(0x0A)           // MajorLinkerVersion
        buf.writeByte(0x00)           // MinorLinkerVersion
        buf.writeIntLE(0x00001000)    // SizeOfCode
        buf.writeIntLE(0x00002000)    // SizeOfInitializedData
        buf.writeIntLE(0x00000000)    // SizeOfUninitializedData
        buf.writeIntLE(0x00001234)    // AddressOfEntryPoint
        buf.writeIntLE(0x00001000)    // BaseOfCode
        // No BaseOfData for PE32+
        buf.writeLongLE(0x0000000180000000L) // ImageBase (8B for PE32+)
        buf.writeIntLE(0x00001000)    // SectionAlignment
        buf.writeIntLE(0x00000200)    // FileAlignment
        buf.writeShortLE(6)           // MajorOperatingSystemVersion
        buf.writeShortLE(0)           // MinorOperatingSystemVersion
        buf.writeShortLE(0)           // MajorImageVersion
        buf.writeShortLE(0)           // MinorImageVersion
        buf.writeShortLE(6)           // MajorSubsystemVersion
        buf.writeShortLE(0)           // MinorSubsystemVersion
        buf.writeIntLE(0)             // Win32VersionValue
        buf.writeIntLE(0x00003000)    // SizeOfImage
        buf.writeIntLE(0x00000200)    // SizeOfHeaders
        buf.writeIntLE(0)             // CheckSum
        buf.writeShortLE(3)           // Subsystem (WINDOWS_CUI)
        buf.writeShortLE(0xC160)      // DllCharacteristics (DYNAMIC_BASE | NX_COMPAT | TERMINAL_SERVER_AWARE | GUARD_CF)
        buf.writeLongLE(0x000001000000L) // SizeOfStackReserve (8B for PE32+)
        buf.writeLongLE(0x00000010000L) // SizeOfStackCommit (8B for PE32+)
        buf.writeLongLE(0x000001000000L) // SizeOfHeapReserve (8B for PE32+)
        buf.writeLongLE(0x00000010000L) // SizeOfHeapCommit (8B for PE32+)
        buf.writeIntLE(0)             // LoaderFlags
        buf.writeIntLE(2)             // NumberOfRvaAndSizes
        // Data Directory 0
        buf.writeIntLE(0)             // RVA
        buf.writeIntLE(0)             // Size
        // Data Directory 1
        buf.writeIntLE(0x00005000)    // RVA
        buf.writeIntLE(0x00000040)    // Size

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
        val buf = Unpooled.buffer(96)
        buf.writeShortLE(0x0000)      // Invalid magic

        assertFailsWith<IllegalArgumentException> {
            OptionalHeader(buf)
        }
    }

    @Test
    fun testReadSectionHeader() {
        val buf = Unpooled.buffer(40)
        val nameBytes = byteArrayOf(0x2E, 0x74, 0x65, 0x78, 0x74, 0, 0, 0) // ".text\0\0\0"
        buf.writeBytes(nameBytes)
        buf.writeIntLE(0x00000100)    // VirtualSize
        buf.writeIntLE(0x00002000)    // VirtualAddress
        buf.writeIntLE(0x00000200)    // SizeOfRawData
        buf.writeIntLE(0x00000400)    // PointerToRawData
        buf.writeIntLE(0)             // PointerToRelocations
        buf.writeIntLE(0)             // PointerToLinenumbers
        buf.writeShortLE(0)           // NumberOfRelocations
        buf.writeShortLE(0)           // NumberOfLinenumbers
        buf.writeIntLE(0x60000020)    // Characteristics: CNT_CODE | MEM_EXECUTE | MEM_READ

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
        val buf = Unpooled.buffer(40)
        buf.writeBytes(".textx00".toByteArray(Charsets.UTF_8))  // Name (exactly 8 chars, no null terminator)
        buf.writeIntLE(0x00000100)
        buf.writeIntLE(0x00002000)
        buf.writeIntLE(0x00000200)
        buf.writeIntLE(0x00000400)
        buf.writeIntLE(0)
        buf.writeIntLE(0)
        buf.writeShortLE(0)
        buf.writeShortLE(0)
        buf.writeIntLE(0x40000040)

        val header = SectionHeader(buf)
        assertEquals(".textx00", header.name)
        assertTrue(header.characteristics.containsInitializedData)
        assertTrue(header.characteristics.isRead)
    }

    @Test
    fun testReadSectionHeaderBufferTooSmall() {
        val buf = Unpooled.buffer(20)

        assertFailsWith<IllegalArgumentException> {
            SectionHeader(buf)
        }
    }

    @Test
    fun testReadCliHeader() {
        val buf = Unpooled.buffer(72)
        buf.writeIntLE(0x48)          // cb = 72
        buf.writeShortLE(2)           // majorRuntimeVersion
        buf.writeShortLE(5)           // minorRuntimeVersion
        buf.writeIntLE(0x00002008)    // metaData.rva
        buf.writeIntLE(0x00000100)    // metaData.size
        buf.writeIntLE(0x00000001)    // flags = ILONLY
        buf.writeIntLE(0x06000001)    // entryPointToken
        buf.writeIntLE(0x00003000)    // resources.rva
        buf.writeIntLE(0x00000200)    // resources.size
        buf.writeIntLE(0x00004000)    // strongNameSignature.rva
        buf.writeIntLE(0x00000100)    // strongNameSignature.size
        buf.writeIntLE(0)             // codeManagerTable.rva
        buf.writeIntLE(0)             // codeManagerTable.size
        buf.writeIntLE(0x00005000)    // vTableFixups.rva
        buf.writeIntLE(0x00000030)    // vTableFixups.size
        buf.writeIntLE(0)             // exportAddressTableJumps.rva
        buf.writeIntLE(0)             // exportAddressTableJumps.size
        buf.writeIntLE(0)             // managedNativeHeader.rva
        buf.writeIntLE(0)             // managedNativeHeader.size

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
        val buf = Unpooled.buffer(71)

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
        val buf = Unpooled.buffer(totalSize)

        buf.writeIntLE(0x424A5342)       // Signature "BSJB"
        buf.writeShortLE(1)               // MajorVersion
        buf.writeShortLE(1)               // MinorVersion
        buf.writeIntLE(0)                  // Reserved
        buf.writeIntLE(versionPadded)      // Length (x), padded
        buf.writeBytes(versionBytes)       // Version string
        buf.writeByte(0)                   // Null terminator
        repeat(versionPadded - versionWithNull) {
            buf.writeByte(0)               // Padding to 4-byte boundary
        }
        buf.writeShortLE(0)               // Flags
        buf.writeShortLE(streamNames.size) // Streams count

        for (header in streamData) {
            buf.writeIntLE(header.offset.toInt())
            buf.writeIntLE(header.size.toInt())
            val nameBytes = header.name.toByteArray(Charsets.US_ASCII)
            buf.writeBytes(nameBytes)
            buf.writeByte(0)
            val nameWithNull = nameBytes.size + 1
            val padded = (nameWithNull + 3) / 4 * 4
            repeat(padded - nameWithNull) {
                buf.writeByte(0)
            }
        }

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
        val buf = Unpooled.buffer(12)
        buf.writeIntLE(0xDEADBEEF.toInt())
        buf.writeShortLE(1)
        buf.writeShortLE(1)
        buf.writeIntLE(0)

        assertFailsWith<IllegalArgumentException> {
            MetadataRoot(buf)
        }
    }

    @Test
    fun testReadMetadataRootBufferTooSmall() {
        val buf = Unpooled.buffer(8)
        buf.writeIntLE(0x424A5342)
        buf.writeIntLE(0)

        assertFailsWith<IllegalArgumentException> {
            MetadataRoot(buf)
        }
    }
}
