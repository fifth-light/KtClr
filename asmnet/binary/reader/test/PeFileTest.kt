package top.fifthlight.asmnet.binary.reader.test

import org.junit.Test
import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.MachineType
import top.fifthlight.asmnet.binary.OptionalHeader
import top.fifthlight.asmnet.Subsystem
import top.fifthlight.asmnet.binary.PeSignature
import top.fifthlight.asmnet.binary.reader.DosHeader
import top.fifthlight.asmnet.binary.reader.OptionalHeader
import top.fifthlight.asmnet.binary.reader.PeHeader
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PeFileTest {
    private fun loadFilePath(attr: String): Path {
        val property = System.getProperty(attr + "_path")
        return Path.of(RunfileDummy.getRunfiles().rlocation(property))
    }

    @Test
    fun testPeFile() {
        val path = loadFilePath("hello_world_dll")
        assertTrue(Files.exists(path), "PE file not found: $path")
        val bytes = Files.readAllBytes(path)
        val buf = ByteBuffer.wrap(bytes)
        var offset = 0

        val dosHeader = DosHeader(buf.slice(offset, DosHeader.SIZE))
        assertEquals(DosHeader.MAGIC_MZ, dosHeader.e_magic)
        assertTrue(dosHeader.e_lfanew > 0u, "e_lfanew should be positive")

        offset = dosHeader.e_lfanew.toInt()
        val coffHeader = PeHeader(buf.slice(offset, PeSignature.SIZE + CoffHeader.SIZE))
        assertTrue(
            coffHeader.machine == MachineType.AMD64 || coffHeader.machine == MachineType.I386,
            "Expected AMD64 or I386, got ${coffHeader.machine}"
        )
        assertTrue(coffHeader.characteristics.isDll, "Expected DLL characteristics")
        offset += PeSignature.SIZE + CoffHeader.SIZE

        val optionalHeader = OptionalHeader(buf.slice(offset, buf.limit() - offset))
        assertTrue(
            optionalHeader is OptionalHeader.PE32 || optionalHeader is OptionalHeader.PE32Plus,
            "Expected PE32 or PE32Plus"
        )
        assertTrue(
            optionalHeader.subsystem == Subsystem.WINDOWS_CUI || optionalHeader.subsystem == Subsystem.WINDOWS_GUI,
            "Expected WINDOWS_CUI or WINDOWS_GUI, got ${optionalHeader.subsystem}"
        )
        assertTrue(
            optionalHeader.dataDirectories.size >= 15,
            "Expected at least 15 data directories, got ${optionalHeader.dataDirectories.size}"
        )
        val cliHeader = optionalHeader.dataDirectories[14]
        assertTrue(cliHeader.rva != 0u, "CLI Runtime Header RVA should be non-zero")
        assertTrue(cliHeader.size != 0u, "CLI Runtime Header size should be non-zero")
    }
}
