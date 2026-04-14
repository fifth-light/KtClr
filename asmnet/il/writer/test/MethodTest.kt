package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.CallConv
import top.fifthlight.asmnet.CallKind
import top.fifthlight.asmnet.ImplementationAttributes
import top.fifthlight.asmnet.Label
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.MethodParameter
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.OpCode
import top.fifthlight.asmnet.ParamAttributes
import top.fifthlight.asmnet.Type
import top.fifthlight.asmnet.TypeAttributes

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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "Add",
                    returnType = Type.Int32,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = listOf(
                        MethodParameter(Type.Int32, "a", ParamAttributes(0)),
                        MethodParameter(Type.Int32, "b", ParamAttributes(0)),
                    ),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "GetName",
                    returnType = Type.String,
                    callConv = CallConv(
                        instance = true,
                    ),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = ".ctor",
                    returnType = Type.Void,
                    callConv = CallConv(
                        instance = true,
                    ),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public
                                or MethodAttributes.HideBySig
                                or MethodAttributes.SpecialName
                                or MethodAttributes.RTSpecialName
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "DoWork",
                    returnType = Type.Void,
                    callConv = CallConv(
                        instance = true,
                    ),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public
                                or MethodAttributes.HideBySig
                                or MethodAttributes.NewSlot
                                or MethodAttributes.Virtual
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "DoWork",
                    returnType = Type.Void,
                    callConv = CallConv(
                        instance = true,
                    ),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public
                                or MethodAttributes.HideBySig
                                or MethodAttributes.NewSlot
                                or MethodAttributes.Virtual
                                or MethodAttributes.Abstract
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "TryParse",
                    returnType = Type.Bool,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = listOf(
                        MethodParameter(Type.String, "input", ParamAttributes(0)),
                        MethodParameter(
                            Type.ManagedTypePointer(Type.Int32),
                            "result",
                            ParamAttributes(ParamAttributes.Out)
                        ),
                    ),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitCode()
                    visitMaxStack(8)
                    visitEnd()
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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = true,
                    parameters = emptyList(),
                )!!.apply {
                    visitCode()
                    visitMaxStack(8)
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testMethodWithNopAndRet() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  LABEL_1: ret
                }
            """.trimIndent(),
            actual = generateText {
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitInsn(OpCode.NOP)
                    visitLabel(Label())
                    visitInsn(OpCode.RET)
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testMethodWithLdc() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: ldc.i4 42
                  LABEL_1: ldc.i8 123456
                  LABEL_2: ldc.r4 3.14
                  LABEL_3: ldc.r8 2.718
                  LABEL_4: ret
                }
            """.trimIndent(),
            actual = generateText {
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitLdc(42)
                    visitLabel(Label())
                    visitLdc(123456L)
                    visitLabel(Label())
                    visitLdc(3.14f)
                    visitLabel(Label())
                    visitLdc(2.718)
                    visitLabel(Label())
                    visitInsn(OpCode.RET)
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testMethodWithCall() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: call void object::WriteLine(string)
                  LABEL_1: ret
                }
            """.trimIndent(),
            actual = generateText {
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitMethodInsn(
                        OpCode.CALL,
                        MethodReference(
                            callConv = CallConv(),
                            declaringType = Type.Object,
                            name = "WriteLine",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.String),
                        ),
                    )
                    visitLabel(Label())
                    visitInsn(OpCode.RET)
                    visitEnd()
                }
            }
        )
    }

    @Test
    fun testMethodWithLdarg() {
        assertContentEquals(
            expected = """
                .method public hidebysig instance void .ctor() cil managed
                {
                  .maxstack 8
                  LABEL_0: ldarg.0
                  LABEL_1: ret
                }
            """.trimIndent(),
            actual = generateText {
                visitMethod(
                    name = ".ctor",
                    returnType = Type.Void,
                    callConv = CallConv(
                        instance = true,
                    ),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitInsn(OpCode.LDARG_0)
                    visitLabel(Label())
                    visitInsn(OpCode.RET)
                    visitEnd()
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
                        visitMaxStack(8)
                        visitLabel(Label())
                        visitInsn(OpCode.NOP)
                        visitLabel(Label())
                        visitInsn(OpCode.RET)
                        visitEnd()
                    }
                    visitEnd()
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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL or ImplementationAttributes.Unmanaged
                    ),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "Printf",
                    returnType = Type.Void,
                    callConv = CallConv(
                        callKind = CallKind.Managed(
                            vararg = true,
                        ),
                    ),
                    vararg = true,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = listOf(
                        MethodParameter(Type.String, "format", ParamAttributes(0)),
                    ),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "Foo",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public or MethodAttributes.Static or MethodAttributes.HideBySig
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = listOf(
                        MethodParameter(Type.Int32, "x", ParamAttributes(ParamAttributes.Optional)),
                    ),
                )!!.apply {
                    visitEnd()
                }
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
                visitMethod(
                    name = "DoWork",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    vararg = false,
                    attributes = MethodAttributes(
                        MethodAttributes.Public
                                or MethodAttributes.Static
                                or MethodAttributes.HideBySig
                                or MethodAttributes.Final
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = false,
                    parameters = emptyList(),
                )!!.apply {
                    visitEnd()
                }
            }
        )
    }
}
