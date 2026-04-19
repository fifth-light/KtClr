/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*
import java.util.*

class ModuleTest {
    @Test
    fun testModule() {
        assertContentEquals(
            expected = """
                .module CountDown.exe
            """.trimIndent(),
            actual = generateText {
                module("CountDown.exe")
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
                module("CountDown.exe", UUID.fromString("4102e4b1-c2da-4c1d-945e-a41530b5efab"))
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
                module("my-module.exe")
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
                externModule("Counter.dll")
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
                externModule("my-module.dll")
            }
        )
    }

    @Test
    fun testImageBase() {
        assertContentEquals(
            expected = """
                .imagebase 0x0000000010000000
            """.trimIndent(),
            actual = generateText {
                imageBase(0x10000000u)
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
                fileAlignment(0x00000200u)
            }
        )
    }

    @Test
    fun testStackReserve() {
        assertContentEquals(
            expected = """
                .stackreserve 0x0000000000100000
            """.trimIndent(),
            actual = generateText {
                stackReserve(0x00100000u)
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
                subsystem(Subsystem.WINDOWS_CUI)
            }
        )
    }

    @Test
    fun testSubsystemUnknown() {
        assertContentEquals(
            expected = """
                .subsystem 0x00FF
            """.trimIndent(),
            actual = generateText {
                subsystem(Subsystem(0x00FF))
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
                corFlags(RuntimeFlags(RuntimeFlags.ILONLY))
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
                corFlags(RuntimeFlags(
                    RuntimeFlags.ILONLY,
                    RuntimeFlags.STRONGNAMESIGNED,
                    RuntimeFlags.TRACKDEBUGDATA,
                ))
            }
        )
    }

    @Test
    fun testManifestResourcePublic() {
        assertContentEquals(
            expected = """
                .mresource public MyResource.res {
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("MyResource.res")
            }
        )
    }

    @Test
    fun testManifestResourcePrivate() {
        assertContentEquals(
            expected = """
                .mresource private MyResource.res {
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource(
                    "MyResource.res",
                    attributes = ManifestResourceAttributes(ManifestResourceAttributes.Private),
                )
            }
        )
    }

    @Test
    fun testManifestResourceWithSpecialName() {
        assertContentEquals(
            expected = """
                .mresource public 'my-resource.res' {
                } // end of mresource my-resource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("my-resource.res")
            }
        )
    }

    @Test
    fun testManifestResourceWithFile() {
        assertContentEquals(
            expected = """
                .mresource public MyResource.res {
                  .file data.txt at 0x00000000
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("MyResource.res") {
                    file("data.txt", 0)
                }
            }
        )
    }

    @Test
    fun testManifestResourceWithExternAssembly() {
        assertContentEquals(
            expected = """
                .mresource public MyResource.res {
                  .assembly extern SomeAssembly
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("MyResource.res") {
                    externAssembly("SomeAssembly")
                }
            }
        )
    }

    @Test
    fun testManifestResourceWithCustomAttribute() {
        assertContentEquals(
            expected = """
                .mresource public MyResource.res {
                  .custom instance void [System.Runtime]System.Reflection.AssemblyTitleAttribute::.ctor(string) = ( 01 00 04 54 65 73 74 00 00 )
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("MyResource.res") {
                    custom(
                        CustomAttributeReference(
                            attributeType = TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.Reflection.AssemblyTitleAttribute",
                            ),
                            parameterTypes = listOf(Type.String),
                        ),
                        blob = byteArrayOf(0x01, 0x00, 0x04, 0x54, 0x65, 0x73, 0x74, 0x00, 0x00),
                    )
                }
            }
        )
    }

    @Test
    fun testManifestResourceWithFileOffset() {
        assertContentEquals(
            expected = """
                .mresource public MyResource.res {
                  .file data.bin at 0x00001000
                } // end of mresource MyResource.res
            """.trimIndent(),
            actual = generateText {
                manifestResource("MyResource.res") {
                    file("data.bin", 0x1000)
                }
            }
        )
    }
}
