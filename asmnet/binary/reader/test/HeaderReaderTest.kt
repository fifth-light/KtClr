package top.fifthlight.asmnet.binary.reader.test

import org.junit.Test
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.MachineType
import top.fifthlight.asmnet.binary.reader.CoffHeader
import top.fifthlight.asmnet.binary.reader.DosHeader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
}
