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
                .method public hidebysig specialname rtspecialname instance void '.ctor'() cil managed
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                    method("Main", attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    )) {
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
                method("Foo",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("M2",
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
    fun testMethodCustomAttributeWithoutBlob() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor()
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
                method("Main",
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
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
}
