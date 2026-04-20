/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class ExportedTypeTest {
    @Test
    fun testExportedTypePublic() {
        assertContentEquals(
            expected = """
                .class extern public MyType {
                } // end of class extern MyType
            """.trimIndent(),
            actual = generateText {
                exportedType("MyType")
            }
        )
    }

    @Test
    fun testExportedTypeNestedPublic() {
        assertContentEquals(
            expected = """
                .class extern nested public Inner {
                } // end of class extern Inner
            """.trimIndent(),
            actual = generateText {
                exportedType(
                    "Inner",
                    flags = TypeAttributes(TypeAttributes.NestedPublic),
                )
            }
        )
    }

    @Test
    fun testExportedTypeWithFile() {
        assertContentEquals(
            expected = """
                .class extern public MyType {
                  .file OtherModule.netmodule
                } // end of class extern MyType
            """.trimIndent(),
            actual = generateText {
                exportedType("MyType") {
                    file("OtherModule.netmodule")
                }
            }
        )
    }

    @Test
    fun testExportedTypeWithParentType() {
        assertContentEquals(
            expected = """
                .class extern nested public Inner {
                  .class extern Outer
                } // end of class extern Inner
            """.trimIndent(),
            actual = generateText {
                exportedType(
                    "Inner",
                    flags = TypeAttributes(TypeAttributes.NestedPublic),
                ) {
                    parentType("Outer")
                }
            }
        )
    }

    @Test
    fun testExportedTypeWithSpecialName() {
        assertContentEquals(
            expected = """
                .class extern public 'my-type' {
                } // end of class extern my-type
            """.trimIndent(),
            actual = generateText {
                exportedType("my-type")
            }
        )
    }

    @Test
    fun testExportedTypeWithCustomAttribute() {
        assertContentEquals(
            expected = """
                .class extern public MyType {
                  .custom instance void [System.Runtime]System.Reflection.AssemblyTitleAttribute::.ctor(string) = ( 01 00 04 54 65 73 74 00 00 )
                } // end of class extern MyType
            """.trimIndent(),
            actual = generateText {
                exportedType("MyType") {
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
}
