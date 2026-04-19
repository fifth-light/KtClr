/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

private val propertyType = TypeReference(name = "MyClass")

class PropertyTest {
    @Test
    fun testPropertyGetSet() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyGetOnly() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyWithOther() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                    .other instance void MyClass::ResetMyProp()
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                        other(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "ResetMyProp",
                            returnType = Type.Void,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyWithAllAccessors() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                    .other instance void MyClass::ResetMyProp()
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                        other(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "ResetMyProp",
                            returnType = Type.Void,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyWithSpecialName() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property specialname instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                        attributes = PropertyAttributes(PropertyAttributes.SpecialName),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyWithRTSpecialName() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property rtspecialname instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                        attributes = PropertyAttributes(PropertyAttributes.RTSpecialName),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyNoAttributes() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .get instance int32 MyClass::get_MyProp()
                    .set instance void MyClass::set_MyProp(int32)
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_MyProp",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyWithParameters() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 Item(int32 index)
                  {
                    .get instance int32 MyClass::get_Item(int32)
                    .set instance void MyClass::set_Item(int32, int32)
                  } // end of property Item
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("Item", Type.Int32,
                        callConv = CallConv(instance = true),
                        parameters = listOf(Parameter(Type.Int32, "index")),
                    ) {
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_Item",
                            returnType = Type.Int32,
                            parameterTypes = listOf(Type.Int32),
                        ))
                        set(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "set_Item",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.Int32, Type.Int32),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyCustomAttributeWithoutBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .custom instance void [System.Runtime]System.CLSCompliantAttribute::.ctor()
                    .get instance int32 MyClass::get_MyProp()
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.CLSCompliantAttribute",
                                ),
                            ),
                            blob = null,
                        )
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testPropertyCustomAttributeWithBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .property instance int32 MyProp()
                  {
                    .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor(string) = ( 01 00 05 68 65 6C 6C 6F 00 00 )
                    .get instance int32 MyClass::get_MyProp()
                  } // end of property MyProp
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    property("MyProp", Type.Int32,
                        callConv = CallConv(instance = true),
                    ) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.ObsoleteAttribute",
                                ),
                                parameterTypes = listOf(Type.String),
                            ),
                            blob = byteArrayOf(0x01, 0x00, 0x05, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x00, 0x00),
                        )
                        get(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = propertyType,
                            name = "get_MyProp",
                            returnType = Type.Int32,
                        ))
                    }
                }
            }
        )
    }
}
