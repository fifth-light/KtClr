package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.ResolutionScope
import top.fifthlight.asmnet.Type
import top.fifthlight.asmnet.TypeAttributes
import top.fifthlight.asmnet.TypeReference

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
                    visit()
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
                    visit(attrs = TypeAttributes(TypeAttributes.NotPublic))
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.Abstract,
                            TypeAttributes.Sealed,
                        ),
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.BeforeFieldInit,
                        ),
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.Interface,
                        ),
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.Serializable,
                        ),
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.SequentialLayout,
                        ),
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.UnicodeClass,
                        ),
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
                        extends = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Object",
                        ),
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
                implements [System.Runtime]System.IDisposable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        implements = setOf(
                            TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.IDisposable",
                            ),
                        )
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
                implements [System.Runtime]System.IDisposable
                implements [System.Runtime]System.IComparable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        extends = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Object",
                        ),
                        implements = setOf(
                            TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.IDisposable",
                            ),
                            TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.IComparable",
                            ),
                        )
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
                    visit(attrs = TypeAttributes(TypeAttributes.NestedPublic))
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
                implements [System.Runtime]System.IDisposable
                {
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                visitClass("MyClass")!!.apply {
                    visit(
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.Abstract,
                            TypeAttributes.BeforeFieldInit,
                        ),
                        extends = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Object",
                        ),
                        implements = setOf(
                            TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.IDisposable",
                            ),
                        )
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
                    visit()
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
                        attrs = TypeAttributes(
                            TypeAttributes.Public,
                            TypeAttributes.SpecialName,
                            TypeAttributes.RTSpecialName,
                        ),
                    )
                    visitEnd()
                }
            }
        )
    }
}
