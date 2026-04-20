/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class TypeForwarderTest {
    @Test
    fun testTypeForwarder() {
        assertContentEquals(
            expected = """
                .class extern forwarder MyType {
                  .assembly extern SomeAssembly
                } // end of class extern forwarder MyType
            """.trimIndent(),
            actual = generateText {
                typeForwarder("MyType") {
                    externAssembly("SomeAssembly")
                }
            }
        )
    }

    @Test
    fun testTypeForwarderWithSpecialName() {
        assertContentEquals(
            expected = """
                .class extern forwarder 'my-type' {
                  .assembly extern SomeAssembly
                } // end of class extern forwarder my-type
            """.trimIndent(),
            actual = generateText {
                typeForwarder("my-type") {
                    externAssembly("SomeAssembly")
                }
            }
        )
    }

    @Test
    fun testTypeForwarderWithCustomAttribute() {
        assertContentEquals(
            expected = """
                .class extern forwarder MyType {
                  .custom instance void [System.Runtime]System.Reflection.AssemblyTitleAttribute::.ctor(string) = ( 01 00 04 54 65 73 74 00 00 )
                  .assembly extern SomeAssembly
                } // end of class extern forwarder MyType
            """.trimIndent(),
            actual = generateText {
                typeForwarder("MyType") {
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
                    externAssembly("SomeAssembly")
                }
            }
        )
    }
}
