package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.RuntimeFlags
import top.fifthlight.asmnet.Subsystem
import java.util.UUID

class ModuleTest {
    @Test
    fun testModule() {
        assertContentEquals(
            expected = """
                .module CountDown.exe
            """.trimIndent(),
            actual = generateText {
                visitModule(name = "CountDown.exe", mvid = null)
            }
        )
    }

    @Test
    fun testModuleWithMvid() {
        assertContentEquals(
            expected = """
                .module CountDown.exe
                // MVID: {4102e4b1-c2da-4c1d-945e-a41530b5efab}
            """.trimIndent(),
            actual = generateText {
                visitModule(
                    name = "CountDown.exe",
                    mvid = UUID.fromString("4102e4b1-c2da-4c1d-945e-a41530b5efab"),
                )
            }
        )
    }

    @Test
    fun testModuleWithSpecialName() {
        assertContentEquals(
            expected = """
                .module 'my-module.exe'
            """.trimIndent(),
            actual = generateText {
                visitModule(name = "my-module.exe", mvid = null)
            }
        )
    }

    @Test
    fun testExternModule() {
        assertContentEquals(
            expected = """
                .module extern Counter.dll
            """.trimIndent(),
            actual = generateText {
                visitExternModule(fileName = "Counter.dll")
            }
        )
    }

    @Test
    fun testExternModuleWithSpecialName() {
        assertContentEquals(
            expected = """
                .module extern 'my-module.dll'
            """.trimIndent(),
            actual = generateText {
                visitExternModule(fileName = "my-module.dll")
            }
        )
    }

    @Test
    fun testImageBase() {
        assertContentEquals(
            expected = """
                .imagebase 0x10000000
            """.trimIndent(),
            actual = generateText {
                visitImageBase(0x10000000)
            }
        )
    }

    @Test
    fun testFileAlignment() {
        assertContentEquals(
            expected = """
                .file alignment 0x00000200
            """.trimIndent(),
            actual = generateText {
                visitFileAlignment(0x00000200)
            }
        )
    }

    @Test
    fun testStackReserve() {
        assertContentEquals(
            expected = """
                .stackreserve 0x00100000
            """.trimIndent(),
            actual = generateText {
                visitStackReserve(0x00100000)
            }
        )
    }

    @Test
    fun testSubsystem() {
        assertContentEquals(
            expected = """
                .subsystem 0x0003 // WINDOWS_CUI
            """.trimIndent(),
            actual = generateText {
                visitSubsystem(Subsystem.WINDOWS_CUI)
            }
        )
    }

    @Test
    fun testCorFlagsIlOnly() {
        assertContentEquals(
            expected = """
                .corflags 0x00000001 // ILONLY
            """.trimIndent(),
            actual = generateText {
                visitCorFlags(RuntimeFlags(RuntimeFlags.ILONLY))
            }
        )
    }

    @Test
    fun testCorFlagsMultiple() {
        assertContentEquals(
            expected = """
                .corflags 0x00010009 // ILONLY | STRONGNAMESIGNED | TRACKDEBUGDATA
            """.trimIndent(),
            actual = generateText {
                visitCorFlags(RuntimeFlags(RuntimeFlags.ILONLY or RuntimeFlags.STRONGNAMESIGNED or RuntimeFlags.TRACKDEBUGDATA))
            }
        )
    }
}
