package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class FieldTest {
    @Test
    fun testSimpleInstanceField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))
                }
            }
        )
    }

    @Test
    fun testPublicStaticField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field public static int32 count
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("count", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                    ))
                }
            }
        )
    }

    @Test
    fun testInitOnlyField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field public static initonly int32 pointCount
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("pointCount", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                        FieldAttributes.InitOnly,
                    ))
                }
            }
        )
    }

    @Test
    fun testLiteralFieldWithInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field public static literal int8 no_error = int8(0x00)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("no_error", Type.Int8, attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                        FieldAttributes.Literal,
                    ), initValue = FieldInitValue.Int8(0.toByte()))
                }
            }
        )
    }

    @Test
    fun testFieldWithOffset() {
        assertContentEquals(
            expected = """
                .class public explicit ansi MyClass
                {
                  .field [0] public int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.ExplicitLayout,
                )) {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Public), offset = 0)
                }
            }
        )
    }

    @Test
    fun testNullRefInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private object ref = nullref
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("ref", Type.Object, attributes = FieldAttributes(FieldAttributes.Private), initValue = FieldInitValue.NullRef)
                }
            }
        )
    }

    @Test
    fun testStringInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static string name = "hello"
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("name", Type.String, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.String("hello"))
                }
            }
        )
    }

    @Test
    fun testFloat32Init() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static float32 pi = float32(3.14)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("pi", Type.Float32, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.Float32(3.14f))
                }
            }
        )
    }

    @Test
    fun testFloat64Init() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static float64 pi = float64(3.141592653589793)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("pi", Type.Float64, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.Float64(3.141592653589793))
                }
            }
        )
    }

    @Test
    fun testInt64Init() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static int64 big = int64(0x7FFFFFFFFFFFFFFF)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("big", Type.Int64, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.Int64(Long.MAX_VALUE))
                }
            }
        )
    }

    @Test
    fun testUnsignedInt32Init() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static unsigned int32 max = unsigned int32(0xFFFFFFFF)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("max", Type.UnsignedInt32, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.UnsignedInt32(0xFFFFFFFFu))
                }
            }
        )
    }

    @Test
    fun testBooleanInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static bool flag = bool(true)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("flag", Type.Bool, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.Boolean(true))
                }
            }
        )
    }

    @Test
    fun testCharInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private static char ch = char(65)
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("ch", Type.Char, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.Static,
                    ), initValue = FieldInitValue.Char('A'))
                }
            }
        )
    }

    @Test
    fun testMultipleFields() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                  .field private int32 y
                  .field public static int32 count
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))
                    field("y", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))
                    field("count", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                    ))
                }
            }
        )
    }

    @Test
    fun testFieldWithMethod() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                  .method public static hidebysig void Main() cil managed
                  {
                  } // end of method MyClass::Main
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))
                    method("Main", attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )) {}
                }
            }
        )
    }

    @Test
    fun testGlobalField() {
        assertContentEquals(
            expected = """
                .field public static int32 globalCount
            """.trimIndent(),
            actual = generateText {
                field("globalCount", Type.Int32, attributes = FieldAttributes(
                    FieldAttributes.Public,
                    FieldAttributes.Static,
                ))
            }
        )
    }

    @Test
    fun testDefaultAttributes() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field compilercontrolled int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32)
                }
            }
        )
    }

    @Test
    fun testFamilyField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field family int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Family))
                }
            }
        )
    }

    @Test
    fun testFamORAssemField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field famorassem int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.FamORAssem))
                }
            }
        )
    }

    @Test
    fun testAssemblyField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field assembly int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Assembly))
                }
            }
        )
    }

    @Test
    fun testNotSerializedField() {
        assertContentEquals(
            expected = """
                .class public auto ansi serializable MyClass
                {
                  .field private notserialized int32 x
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.Serializable,
                )) {
                    field("x", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.NotSerialized,
                    ))
                }
            }
        )
    }

    @Test
    fun testSpecialNameField() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private specialname rtspecialname int32 value__
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("value__", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Private,
                        FieldAttributes.SpecialName,
                        FieldAttributes.RTSpecialName,
                    ))
                }
            }
        )
    }

    @Test
    fun testFieldCustomAttributeWithoutBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                  .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor()
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private)) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.ObsoleteAttribute",
                                ),
                            ),
                            blob = null,
                        )
                    }
                }
            }
        )
    }

    @Test
    fun testFieldCustomAttributeWithBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                  .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor(string) = ( 01 00 05 68 65 6C 6C 6F 00 00 )
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private)) {
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
                    }
                }
            }
        )
    }

    @Test
    fun testGlobalFieldCustomAttribute() {
        assertContentEquals(
            expected = """
                .field public static int32 globalCount
                .custom instance void [System.Runtime]System.CLSCompliantAttribute::.ctor(bool) = ( 01 00 01 00 00 )
            """.trimIndent(),
            actual = generateText {
                field("globalCount", Type.Int32, attributes = FieldAttributes(
                    FieldAttributes.Public,
                    FieldAttributes.Static,
                )) {
                    custom(
                        CustomAttributeReference(
                            attributeType = TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.CLSCompliantAttribute",
                            ),
                            parameterTypes = listOf(Type.Bool),
                        ),
                        blob = byteArrayOf(0x01, 0x00, 0x01, 0x00, 0x00),
                    )
                }
            }
        )
    }

    @Test
    fun testMultipleFieldsWithCustomAttributes() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .field private int32 x
                  .custom instance void [System.Runtime]System.NonSerializedAttribute::.ctor()
                  .field private int32 y
                  .custom instance void [System.Runtime]System.NonSerializedAttribute::.ctor()
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    field("x", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private)) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.NonSerializedAttribute",
                                ),
                            ),
                            blob = null,
                        )
                    }
                    field("y", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private)) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.NonSerializedAttribute",
                                ),
                            ),
                            blob = null,
                        )
                    }
                }
            }
        )
    }
}
