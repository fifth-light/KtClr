package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.CallConv
import top.fifthlight.asmnet.ImplementationAttributes
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.Type
import top.fifthlight.asmnet.TypeAttributes

class ClassTest {
    @Test
    fun testSimplePublicClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(TypeAttributes(TypeAttributes.Public), null, emptySet())
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testPrivateClass() {
        assertContentEquals(
            expected = """
                .class private auto ansi MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(TypeAttributes(TypeAttributes.NotPublic), null, emptySet())
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testAbstractSealedClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi abstract sealed MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(
                            TypeAttributes.Public or TypeAttributes.Abstract or TypeAttributes.Sealed
                        ), null, emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testBeforeFieldInit() {
        assertContentEquals(
            expected = """
                .class public auto ansi beforefieldinit MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(
                            TypeAttributes.Public or TypeAttributes.BeforeFieldInit
                        ), null, emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testInterface() {
        assertContentEquals(
            expected = """
                .class public auto ansi interface IMyInterface
                {
                } // end of class IMyInterface
            """.trimIndent(),
            actual = generateText {
                visitClass("IMyInterface")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public or TypeAttributes.Interface),
                        null,
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testSerializableClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi serializable MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public or TypeAttributes.Serializable),
                        null,
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testSequentialLayout() {
        assertContentEquals(
            expected = """
                .class public sequential ansi MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public or TypeAttributes.SequentialLayout),
                        null,
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testUnicodeStringFormat() {
        assertContentEquals(
            expected = """
                .class public auto unicode MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public or TypeAttributes.UnicodeClass),
                        null,
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testClassWithExtends() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                extends [System.Runtime]System.Object
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public),
                        "[System.Runtime]System.Object",
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testClassWithImplements() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                implements IDisposable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public),
                        null,
                        setOf("IDisposable")
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testClassWithExtendsAndImplements() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                extends [System.Runtime]System.Object
                implements IDisposable
                implements IComparable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(TypeAttributes.Public),
                        "[System.Runtime]System.Object",
                        setOf("IDisposable", "IComparable")
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testNestedPublicClass() {
        assertContentEquals(
            expected = """
                .class nested public auto ansi InnerClass
                {
                } // end of class InnerClass
            """.trimIndent(),
            actual = generateText {
                visitClass("InnerClass")!!.apply {
                    visit(TypeAttributes(TypeAttributes.NestedPublic), null, emptySet())
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testClassWithAllAttributes() {
        assertContentEquals(
            expected = """
                .class public auto ansi abstract beforefieldinit MyClass
                extends [System.Runtime]System.Object
                implements IDisposable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(
                            TypeAttributes.Public
                                    or TypeAttributes.Abstract
                                    or TypeAttributes.BeforeFieldInit
                        ),
                        "[System.Runtime]System.Object",
                        setOf("IDisposable")
                    )
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testClassWithMethodInside() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .method public static hidebysig void Main() cil managed
                  {
                  } // end of method MyClass::Main
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(TypeAttributes(TypeAttributes.Public), null, emptySet())
                    visitMethod(
                        name = "Main",
                        returnType = Type.Void,
                        callConv = CallConv(),
                        attributes = MethodAttributes(
                            MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                        ),
                        implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                        parameters = emptyList(),
                    )!!.apply {
                        visitEnd()
                    }
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testSpecialNameClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi specialname rtspecialname MyClass
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        TypeAttributes(
                            TypeAttributes.Public
                                    or TypeAttributes.SpecialName
                                    or TypeAttributes.RTSpecialName
                        ),
                        null,
                        emptySet()
                    )
                    visitEnd()
                }
            }
        )
    }
}
