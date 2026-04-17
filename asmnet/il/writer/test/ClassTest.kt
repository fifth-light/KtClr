package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.CallConv
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.ResolutionScope
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
                class_("MyClass") {

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
                class_("MyClass", attrs = TypeAttributes(TypeAttributes.NotPublic)) {}
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.Abstract,
                    TypeAttributes.Sealed,
                )) {}
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.BeforeFieldInit,
                )) {}
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
                class_("IMyInterface", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.Interface,
                )) {}
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.Serializable,
                )) {}
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.SequentialLayout,
                )) {}
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.UnicodeClass,
                )) {}
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
                class_("MyClass", extends = TypeReference(
                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                    name = "System.Object",
                )) {}
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
                class_("MyClass", implements = setOf(
                    TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.IDisposable",
                    ),
                )) {}
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
                class_("MyClass",
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
                    ),
                ) {}
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
                class_("InnerClass", attrs = TypeAttributes(TypeAttributes.NestedPublic)) {}
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
                class_("MyClass",
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
                    ),
                ) {}
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
                class_("MyClass") {
                    method("Main", attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    )) {}
                }
            }
        )
    }

    @Test
    fun testOverride() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                implements .module testIMyInterface
                {
                  .override .module testIMyInterface::M with instance void .module testMyClass::M2()
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", implements = setOf(
                    TypeReference(
                        resolutionScope = ResolutionScope.Module("test"),
                        name = "IMyInterface",
                    ),
                )) {
                    override(
                        baseType = TypeReference(
                            resolutionScope = ResolutionScope.Module("test"),
                            name = "IMyInterface",
                        ),
                        baseName = "M",
                        implementation = MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = TypeReference(
                                resolutionScope = ResolutionScope.Module("test"),
                                name = "MyClass",
                            ),
                            name = "M2",
                        ),
                    )
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
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.SpecialName,
                    TypeAttributes.RTSpecialName,
                )) {}
            }
        )
    }

    @Test
    fun testPack() {
        assertContentEquals(
            expected = """
                .class public sequential ansi MyClass
                {
                  .pack 2
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.SequentialLayout,
                )) {
                    pack(2)
                }
            }
        )
    }

    @Test
    fun testSize() {
        assertContentEquals(
            expected = """
                .class public sequential ansi MyClass
                {
                  .size 16
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.SequentialLayout,
                )) {
                    size(16)
                }
            }
        )
    }

    @Test
    fun testPackAndSize() {
        assertContentEquals(
            expected = """
                .class public sequential ansi MyClass
                {
                  .pack 2
                  .size 16
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass", attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.SequentialLayout,
                )) {
                    pack(2)
                    size(16)
                }
            }
        )
    }
}
