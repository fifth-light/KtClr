package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class MethodTest {
    @Test
    fun testSimpleStaticVoidMethod() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testStaticMethodWithParameters() {
        assertContentEquals(
            expected = """
                .method public static hidebysig int32 Add(int32 a, int32 b) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Add",
                    returnType = Type.Int32,
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        MethodParameter(Type.Int32, "a"),
                        MethodParameter(Type.Int32, "b"),
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testInstanceMethod() {
        assertContentEquals(
            expected = """
                .method public hidebysig instance string GetName() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetName",
                    returnType = Type.String,
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testConstructor() {
        assertContentEquals(
            expected = """
                .method public hidebysig specialname rtspecialname instance void .ctor() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(".ctor",
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                        MethodAttributes.SpecialName,
                        MethodAttributes.RTSpecialName,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testVirtualMethod() {
        assertContentEquals(
            expected = """
                .method public virtual hidebysig newslot instance void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("DoWork",
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                        MethodAttributes.NewSlot,
                        MethodAttributes.Virtual,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testAbstractMethod() {
        assertContentEquals(
            expected = """
                .method public virtual hidebysig newslot abstract instance void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("DoWork",
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                        MethodAttributes.NewSlot,
                        MethodAttributes.Virtual,
                        MethodAttributes.Abstract,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testMethodWithOutParameter() {
        assertContentEquals(
            expected = """
                .method public static hidebysig bool TryParse(string input, [out] int32& result) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("TryParse",
                    returnType = Type.Bool,
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        MethodParameter(Type.String, "input"),
                        MethodParameter(
                            Type.ManagedTypePointer(Type.Int32),
                            "result",
                            ParamAttributes(ParamAttributes.Out),
                        ),
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testMethodWithMaxStack() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    code()
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testMethodWithEntryPoint() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .entrypoint
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = true,
                ) {
                    code()
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testMethodInsideClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .method public static hidebysig void Main() cil managed
                  {
                    .maxstack 8
                    LABEL_0: nop
                    LABEL_1: ret
                  } // end of method MyClass::Main
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    method("Main", attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    )) {
                        maxStack(8)
                        label()
                        insn(OpCode.Code.nop)
                        label()
                        insn(OpCode.Code.ret)
                    }
                }
            }
        )
    }

    @Test
    fun testMethodWithNativeImpl() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil unmanaged
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Unmanaged,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testVarargMethod() {
        assertContentEquals(
            expected = """
                .method public static hidebysig vararg void Printf(string format) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Printf",
                    callConv = CallConv(callKind = CallKind.Managed(vararg = true)),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        MethodParameter(Type.String, "format"),
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testMethodWithOptionalParameter() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Foo([opt] int32 x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Foo",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        MethodParameter(Type.Int32, "x", ParamAttributes(ParamAttributes.Optional)),
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testFinalMethod() {
        assertContentEquals(
            expected = """
                .method public static final hidebysig void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("DoWork",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                        MethodAttributes.Final,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }
}
