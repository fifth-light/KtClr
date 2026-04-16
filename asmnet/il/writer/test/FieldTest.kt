package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.FieldAttributes
import top.fifthlight.asmnet.FieldInitValue
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.Type

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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Private),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "count",
                        type = Type.Int32,
                        attributes = FieldAttributes(
                            FieldAttributes.Public,
                            FieldAttributes.Static,
                        ),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "pointCount",
                        type = Type.Int32,
                        attributes = FieldAttributes(
                            FieldAttributes.Public,
                            FieldAttributes.Static,
                            FieldAttributes.InitOnly,
                        ),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "no_error",
                        type = Type.Int8,
                        attributes = FieldAttributes(
                            FieldAttributes.Public,
                            FieldAttributes.Static,
                            FieldAttributes.Literal,
                        ),
                        initValue = FieldInitValue.Int8(0.toByte()),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit(attrs = top.fifthlight.asmnet.TypeAttributes(
                        top.fifthlight.asmnet.TypeAttributes.Public,
                        top.fifthlight.asmnet.TypeAttributes.ExplicitLayout,
                    ))
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Public),
                        offset = 0,
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "ref",
                        type = Type.Object,
                        attributes = FieldAttributes(FieldAttributes.Private),
                        initValue = FieldInitValue.NullRef,
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "name",
                        type = Type.String,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.String("hello"),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "pi",
                        type = Type.Float32,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.Float32(3.14f),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "pi",
                        type = Type.Float64,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.Float64(3.141592653589793),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "big",
                        type = Type.Int64,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.Int64(Long.MAX_VALUE),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "max",
                        type = Type.UnsignedInt32,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.UnsignedInt32(0xFFFFFFFFu),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "flag",
                        type = Type.Bool,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.Boolean(true),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "ch",
                        type = Type.Char,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.Static,
                        ),
                        initValue = FieldInitValue.Char('A'),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Private),
                    )
                    visitField(
                        name = "y",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Private),
                    )
                    visitField(
                        name = "count",
                        type = Type.Int32,
                        attributes = FieldAttributes(
                            FieldAttributes.Public,
                            FieldAttributes.Static,
                        ),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Private),
                    )
                    visitMethod(
                        name = "Main",
                        attributes = MethodAttributes(
                            MethodAttributes.Public,
                            MethodAttributes.Static,
                            MethodAttributes.HideBySig,
                        ),
                    )!!.apply {
                        visitEnd()
                    }
                    visitEnd()
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
                visitField(
                    name = "globalCount",
                    type = Type.Int32,
                    attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                    ),
                )
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Family),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.FamORAssem),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(FieldAttributes.Assembly),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit(attrs = top.fifthlight.asmnet.TypeAttributes(
                        top.fifthlight.asmnet.TypeAttributes.Public,
                        top.fifthlight.asmnet.TypeAttributes.Serializable,
                    ))
                    visitField(
                        name = "x",
                        type = Type.Int32,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.NotSerialized,
                        ),
                    )
                    visitEnd()
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
                visitClass("MyClass")!!.apply {
                    visit()
                    visitField(
                        name = "value__",
                        type = Type.Int32,
                        attributes = FieldAttributes(
                            FieldAttributes.Private,
                            FieldAttributes.SpecialName,
                            FieldAttributes.RTSpecialName,
                        ),
                    )
                    visitEnd()
                }
            }
        )
    }
}
