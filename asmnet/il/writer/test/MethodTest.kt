/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

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
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
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
                method(
                    name = "Add",
                    returnType = Type.Int32,
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        Parameter(Type.Int32, "a"),
                        Parameter(Type.Int32, "b"),
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
                method(
                    name = "GetName",
                    returnType = Type.String,
                    callConv = CallConv(instance = true),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.HideBySig,
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
                .method public hidebysig specialname rtspecialname instance void '.ctor'() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = ".ctor",
                    callConv = CallConv(instance = true),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.HideBySig,
                        MethodAttribute.SpecialName,
                        MethodAttribute.RTSpecialName,
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
                .method public hidebysig newslot virtual instance void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "DoWork",
                    callConv = CallConv(instance = true),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.HideBySig,
                        MethodAttribute.NewSlot,
                        MethodAttribute.Virtual,
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
                .method public hidebysig newslot virtual abstract instance void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "DoWork",
                    callConv = CallConv(instance = true),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.HideBySig,
                        MethodAttribute.NewSlot,
                        MethodAttribute.Virtual,
                        MethodAttribute.Abstract,
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
                method(
                    name = "TryParse",
                    returnType = Type.Bool,
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        Parameter(Type.String, "input"),
                        Parameter(
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
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
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
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    entryPoint = true,
                ) {
                    maxStack(8)
                    code()
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
                    nop
                    ret
                  } // end of method MyClass::Main
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    method(
                        name = "Main",
                        attributes = listOf(
                            MethodAttribute.Public,
                            MethodAttribute.Static,
                            MethodAttribute.HideBySig,
                        ),
                    ) {
                        maxStack(8)
                        code()
                        insn(OpCode.Code.nop)
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
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
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
                method(
                    name = "Printf",
                    callConv = CallConv(callKind = CallKind.Managed(vararg = true)),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        Parameter(Type.String, "format"),
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
                method(
                    name = "Foo",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = listOf(
                        Parameter(Type.Int32, "x", ParamAttributes(ParamAttributes.Optional)),
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testFinalMethod() {
        assertContentEquals(
            expected = """
                .method public static hidebysig final void DoWork() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "DoWork",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                        MethodAttribute.Final,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {}
            }
        )
    }

    @Test
    fun testMethodWithLocalsInit() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .locals init (int32 x, int32 y)
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    locals(
                        LocalVariable(Type.Int32, "x"),
                        LocalVariable(Type.Int32, "y"),
                    )
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testMethodWithLocalsNoInit() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .locals (int32 x)
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    locals(init = false, LocalVariable(Type.Int32, "x"))
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testMethodWithUnnamedLocals() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .locals init (int32, string)
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    locals(
                        LocalVariable(Type.Int32),
                        LocalVariable(Type.String),
                    )
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testMethodWithPinnedLocal() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .locals init (string& pinned p)
                  .maxstack 8
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    locals(
                        LocalVariable(Type.Pinned(Type.ManagedTypePointer(Type.String)), "p"),
                    )
                    maxStack(8)
                }
            }
        )
    }

    @Test
    fun testJumpInsnBackwardReference() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  br LABEL_0
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label(target)
                    insn(OpCode.Code.nop)
                    insn(OpCode.Code.br, target)
                }
            }
        )
    }

    @Test
    fun testJumpInsnForwardReference() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  br LABEL_0
                  LABEL_0: ret
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(OpCode.Code.br, target)
                    label(target)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnConditional() {
        val end = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  brtrue.s LABEL_0
                  nop
                  LABEL_0: ret
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(OpCode.Code.brtrueS, end)
                    insn(OpCode.Code.nop)
                    label(end)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnLeave() {
        val end = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  leave LABEL_0
                  LABEL_0: ret
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(OpCode.Code.leave, end)
                    label(end)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTypeInsnNewArr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  newarr int32
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(OpCode.Code.newarr, Type.Int32)
                }
            }
        )
    }

    @Test
    fun testTypeInsnCastclass() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  castclass [SomeAssembly]SomeNamespace/SomeClass
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(
                        OpCode.Code.castclass,
                        TypeReference(
                            resolutionScope = ResolutionScope.Assembly("SomeAssembly"),
                            names = listOf("SomeNamespace", "SomeClass"),
                        ),
                    )
                }
            }
        )
    }

    @Test
    fun testTypeInsnBoxUnbox() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  box int32
                  unbox.any int32
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    insn(OpCode.Code.box, Type.Int32)
                    insn(OpCode.Code.unboxAny, Type.Int32)
                }
            }
        )
    }

    @Test
    fun testOverride() {
        assertContentEquals(
            expected = """
                .method public hidebysig instance void M2() cil managed
                {
                  .override .module testIMyInterface::M
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "M2",
                    callConv = CallConv(instance = true),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    override(
                        baseType = TypeReference(
                            resolutionScope = ResolutionScope.Module("test"),
                            name = "IMyInterface",
                        ),
                        baseName = "M",
                    )
                }
            }
        )
    }

    @Test
    fun testSwitchInsn() {
        val l0 = Label()
        val l1 = Label()
        val l2 = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  switch (LABEL_0, LABEL_1, LABEL_2)
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    switch(listOf(l0, l1, l2))
                }
            }
        )
    }

    @Test
    fun testSwitchInsnFallThrough() {
        val case0 = Label()
        val case1 = Label()
        val end = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  switch (LABEL_0, LABEL_1)
                  br.s LABEL_2
                  LABEL_0: ret
                  LABEL_1: ret
                  LABEL_2: ret
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    switch(listOf(case0, case1))
                    insn(OpCode.Code.brS, end)
                    label(case0)
                    insn(OpCode.Code.ret)
                    label(case1)
                    insn(OpCode.Code.ret)
                    label(end)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testEmitByte() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .emitbyte 0xFF
                  .emitbyte 0x00
                  .emitbyte 0x2A
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    emitByte(0xFFu)
                    emitByte(0x00u)
                    emitByte(0x2Au)
                }
            }
        )
    }

    @Test
    fun testSourceLineLineOnly() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .line 42
                  nop
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    sourceLine(42)
                    insn(OpCode.Code.nop)
                }
            }
        )
    }

    @Test
    fun testSourceLineWithFilename() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .line 42 'test.cs'
                  nop
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    sourceLine(42, filename = "test.cs")
                    insn(OpCode.Code.nop)
                }
            }
        )
    }

    @Test
    fun testSourceLineWithColumn() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .line 42: 10
                  nop
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    sourceLine(42, column = 10)
                    insn(OpCode.Code.nop)
                }
            }
        )
    }

    @Test
    fun testSourceLineWithColumnAndFilename() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .line 42: 10 'test.cs'
                  nop
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    sourceLine(42, column = 10, filename = "test.cs")
                    insn(OpCode.Code.nop)
                }
            }
        )
    }

    @Test
    fun testSourceLineWithInstructions() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  .line 10 'Program.cs'
                  nop
                  .line 11 'Program.cs'
                  ldstr "hello"
                  .line 12 'Program.cs'
                  ret
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    sourceLine(10, filename = "Program.cs")
                    insn(OpCode.Code.nop)
                    sourceLine(11, filename = "Program.cs")
                    ldc("hello")
                    sourceLine(12, filename = "Program.cs")
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testMethodCustomAttributeWithoutBlob() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor()
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
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
        )
    }

    @Test
    fun testMethodCustomAttributeWithBlob() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .custom instance void [System.Runtime]System.STAThreadAttribute::.ctor() = ( 01 00 00 00 )
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    custom(
                        CustomAttributeReference(
                            attributeType = TypeReference(
                                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                name = "System.STAThreadAttribute",
                            ),
                        ),
                        blob = byteArrayOf(0x01, 0x00, 0x00, 0x00),
                    )
                }
            }
        )
    }

    @Test
    fun testParamStringAndInt32() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main(string s, int32 x) cil managed
                {
                  .param [1] = "hello"
                  .param [2] = int32(0x0000002A)
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Main",
                    parameters = listOf(
                        Parameter(Type.String, "s"),
                        Parameter(Type.Int32, "x"),
                    ),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    param(1, FieldInitValue.String("hello"))
                    param(2, FieldInitValue.Int32(42))
                }
            }
        )
    }

    @Test
    fun testParamReturnValue() {
        assertContentEquals(
            expected = """
                .method public static hidebysig int32 Foo() cil managed
                {
                  .param [0] = int32(0x00000000)
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Foo",
                    returnType = Type.Int32,
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    param(0, FieldInitValue.Int32(0))
                }
            }
        )
    }

    @Test
    fun testParamMixedTypes() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Bar(bool b, float32 f, float64 d, string s) cil managed
                {
                  .param [1] = bool(true)
                  .param [2] = float32(3.14)
                  .param [3] = float64(2.718281828)
                  .param [4] = nullref
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Bar",
                    parameters = listOf(
                        Parameter(Type.Bool, "b"),
                        Parameter(Type.Float32, "f"),
                        Parameter(Type.Float64, "d"),
                        Parameter(Type.String, "s"),
                    ),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    param(1, FieldInitValue.Boolean(true))
                    param(2, FieldInitValue.Float32(3.14f))
                    param(3, FieldInitValue.Float64(2.718281828))
                    param(4, FieldInitValue.NullRef)
                }
            }
        )
    }

    @Test
    fun testParamInt64() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Baz(int64 n) cil managed
                {
                  .param [1] = int64(0x7FFFFFFFFFFFFFFF)
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    name = "Baz",
                    parameters = listOf(
                        Parameter(Type.Int64, "n"),
                    ),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    param(1, FieldInitValue.Int64(Long.MAX_VALUE))
                }
            }
        )
    }

    @Test
    fun testParamMarshalInt32() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(int32 marshal(int32) x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.Int32, "x", marshal = NativeType.Int32),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalLPWStr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(string marshal(lpwstr) name) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.String, "name", marshal = NativeType.LPWStr),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalLPStr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(string marshal(lpstr) name) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.String, "name", marshal = NativeType.LPStr),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalArrayFixedSize() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M1(int32 marshal(int32), bool[] marshal(bool[5]) ar) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M1", parameters = listOf(
                        Parameter(Type.Int32, marshal = NativeType.Int32),
                        Parameter(
                            type = Type.Array(Type.Bool, emptyList()),
                            name = "ar",
                            marshal = NativeType.Array(
                                elementType = NativeType.Boolean,
                                size = 5,
                            ),
                        ),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalArrayParamSize() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M2(int32 marshal(int32), int32[] marshal(int32[+1]) ar) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M2", parameters = listOf(
                        Parameter(Type.Int32, marshal = NativeType.Int32),
                        Parameter(
                            type = Type.Array(Type.Int32, emptyList()),
                            name = "ar",
                            marshal = NativeType.Array(
                                elementType = NativeType.Int32,
                                sizeParamIndex = 1,
                            ),
                        ),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalArrayFixedPlusParam() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M3(int32 marshal(int32), bool[] marshal(bool[7+1]) ar) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M3", parameters = listOf(
                        Parameter(Type.Int32, marshal = NativeType.Int32),
                        Parameter(
                            type = Type.Array(Type.Bool, emptyList()),
                            name = "ar",
                            marshal = NativeType.Array(
                                elementType = NativeType.Boolean,
                                size = 7,
                                sizeParamIndex = 1,
                            )
                        ),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalBool() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(int32 marshal(bool) x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.Int32, "x", marshal = NativeType.Boolean),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalUnsignedInt() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(native int marshal(unsigned int) x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.NativeInt, "x", marshal = NativeType.SysUInt),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalWithParamAttr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M([in] int32& marshal(int32) x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(
                            Type.ManagedTypePointer(Type.Int32),
                            "x",
                            flags = ParamAttributes(ParamAttributes.In),
                            marshal = NativeType.Int32,
                        ),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testParamMarshalMultipleParams() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void M(int32 marshal(int32) a, string marshal(lpstr) b, float64 marshal(float64) c) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", parameters = listOf(
                        Parameter(Type.Int32, "a", marshal = NativeType.Int32),
                        Parameter(Type.String, "b", marshal = NativeType.LPStr),
                        Parameter(Type.Float64, "c", marshal = NativeType.Float64),
                    ), attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testReturnMarshalLPStr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig string marshal(lpstr) M() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", returnType = Type.String, returnMarshal = NativeType.LPStr, attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testReturnMarshalLPWStr() {
        assertContentEquals(
            expected = """
                .method public static hidebysig string marshal(lpwstr) M() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", returnType = Type.String, returnMarshal = NativeType.LPWStr, attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testReturnMarshalInt() {
        assertContentEquals(
            expected = """
                .method public static hidebysig native int marshal(int) M() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M", returnType = Type.NativeInt, returnMarshal = NativeType.SysInt, attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    )
                ) {}
            }
        )
    }

    @Test
    fun testReturnMarshalWithParams() {
        assertContentEquals(
            expected = """
                .method public static hidebysig string marshal(lpstr) M(int32 marshal(int32) x) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M",
                    returnType = Type.String,
                    returnMarshal = NativeType.LPStr,
                    parameters = listOf(
                        Parameter(Type.Int32, "x", marshal = NativeType.Int32),
                    ),
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testReturnMarshalWithCallConv() {
        assertContentEquals(
            expected = """
                .method public static hidebysig string marshal(lpstr) M() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method(
                    "M",
                    returnType = Type.String,
                    returnMarshal = NativeType.LPStr,
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                ) {}
            }
        )
    }
}
