package top.fifthlight.asmnet.binary.reader.test

import org.junit.Test
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.MachineType
import top.fifthlight.asmnet.binary.OptionalHeader
import top.fifthlight.asmnet.binary.reader.CoffHeader
import top.fifthlight.asmnet.binary.reader.DosHeader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue
import top.fifthlight.asmnet.binary.reader.OptionalHeader as readOptionalHeader

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
            top.fifthlight.asmnet.binary.reader.CoffHeader(buf)
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

        val header = readOptionalHeader(buf)
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

        val header = readOptionalHeader(buf)
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
            readOptionalHeader(buf)
        }
    }
}
