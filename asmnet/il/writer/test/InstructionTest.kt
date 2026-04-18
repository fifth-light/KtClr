package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class InstructionTest {
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
                    label()
                    insn(OpCode.Code.nop)
                    label()
                    insn(OpCode.Code.ret)
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
                  LABEL_0: ldc.i4 0x0000002A
                  LABEL_1: ldc.i8 0x000000000001E240
                  LABEL_2: ldc.r4 3.14
                  LABEL_3: ldc.r8 2.718
                  LABEL_4: ret
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
                    label()
                    ldc(42)
                    label()
                    ldc(123456L)
                    label()
                    ldc(3.14f)
                    label()
                    ldc(2.718)
                    label()
                    insn(OpCode.Code.ret)
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
                    label()
                    insn(OpCode.Code.call, MethodReference(
                        declaringType = Type.Object,
                        name = "WriteLine",
                        returnType = Type.Void,
                        parameterTypes = listOf(Type.String),
                    ))
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testMethodWithLdarg() {
        assertContentEquals(
            expected = """
                .method public hidebysig instance void '.ctor'() cil managed
                {
                  .maxstack 8
                  LABEL_0: ldarg.0
                  LABEL_1: ret
                }
            """.trimIndent(),
            actual = generateText {
                method(".ctor",
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label()
                    insn(OpCode.Code.ldarg0)
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testMethodWithLdargS() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Foo(string a, string b, string c, string d, string e) cil managed
                {
                  .maxstack 8
                  LABEL_0: ldarg.s 4
                  LABEL_1: ret
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
                        Parameter(Type.String, "a"),
                        Parameter(Type.String, "b"),
                        Parameter(Type.String, "c"),
                        Parameter(Type.String, "d"),
                        Parameter(Type.String, "e"),
                    ),
                ) {
                    maxStack(8)
                    code()
                    label()
                    insn(OpCode.Code.ldargS, 4)
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testMethodWithFieldInsn() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: ldfld int32 MyClass::_count
                  LABEL_1: stfld int32 MyClass::_count
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
                    label()
                    insn(OpCode.Code.ldfld, FieldReference(
                        declaringType = TypeReference("MyClass"),
                        name = "_count",
                        fieldType = Type.Int32,
                    ))
                    label()
                    insn(OpCode.Code.stfld, FieldReference(
                        declaringType = TypeReference("MyClass"),
                        name = "_count",
                        fieldType = Type.Int32,
                    ))
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnBr() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: br LABEL_1
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.br, target)
                    label(target)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnBrFalse() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: brfalse LABEL_1
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.brfalse, target)
                    label(target)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnBrTrue() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: brtrue LABEL_1
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.brtrue, target)
                    label(target)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testJumpInsnLeave() {
        val target = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: leave LABEL_1
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.leave, target)
                    label(target)
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
                  LABEL_0: newarr int32
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.newarr, Type.Int32)
                    label()
                    insn(OpCode.Code.ret)
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
                  LABEL_0: castclass [System.Runtime]System.Object
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.castclass, TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ))
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTypeInsnIsInst() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: isinst [System.Runtime]System.Object
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.isinst, TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ))
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTypeInsnBox() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: box int32
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.box, Type.Int32)
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTypeInsnUnbox() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: unbox int32
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.unbox, Type.Int32)
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTypeInsnUnboxAny() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: unbox.any int32
                  LABEL_1: ret
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
                    label()
                    insn(OpCode.Code.unboxAny, Type.Int32)
                    label()
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testTailPrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  tail.
                  call void MyClass::Foo()
                  ret
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
                    insn(OpCode.Code.call, MethodReference(
                        declaringType = TypeReference("MyClass"),
                        name = "Foo",
                        returnType = Type.Void,
                    ), OpCode.Prefix.Tail)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testVolatilePrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  volatile.
                  ldfld int32 MyClass::_count
                  ret
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
                    insn(OpCode.Code.ldfld, FieldReference(
                        declaringType = TypeReference("MyClass"),
                        name = "_count",
                        fieldType = Type.Int32,
                    ), OpCode.Prefix.Volatile)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testReadonlyPrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  readonly.
                  ldelema int32
                  ret
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
                    insn(OpCode.Code.ldelema, Type.Int32, OpCode.Prefix.Readonly)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testConstrainedPrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  constrained. int32
                  callvirt void [System.Runtime]System.Object::ToString()
                  ret
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
                    insn(OpCode.Code.callvirt, MethodReference(
                        declaringType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Object",
                        ),
                        name = "ToString",
                        returnType = Type.Void,
                    ), OpCode.Prefix.Constrained(Type.Int32))
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testUnalignedPrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  unaligned. 2
                  ldfld int32 MyClass::_data
                  ret
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
                    insn(OpCode.Code.ldfld, FieldReference(
                        declaringType = TypeReference("MyClass"),
                        name = "_data",
                        fieldType = Type.Int32,
                    ), OpCode.Prefix.Unaligned(2))
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testNoPrefixSingleFlag() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  no. 1
                  castclass [System.Runtime]System.Object
                  ret
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
                    insn(OpCode.Code.castclass, TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ), OpCode.Prefix.No(OpCode.Prefix.No.Flag(OpCode.Prefix.No.Flag.typeCheck)))
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testNoPrefixMultipleFlags() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  no. 7
                  ldelem.ref
                  ret
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
                        OpCode.Code.ldelemRef,
                        OpCode.Prefix.No(OpCode.Prefix.No.Flag(
                            OpCode.Prefix.No.Flag.typeCheck,
                            OpCode.Prefix.No.Flag.rangeCheck,
                            OpCode.Prefix.No.Flag.nullCheck,
                        )),
                    )
                    insn(OpCode.Code.ret)
                }
            }
        )
    }

    @Test
    fun testCombinedUnalignedVolatilePrefix() {
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  unaligned. 4
                  volatile.
                  ldfld int32 MyClass::_data
                  ret
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
                    insn(OpCode.Code.ldfld, FieldReference(
                        declaringType = TypeReference("MyClass"),
                        name = "_data",
                        fieldType = Type.Int32,
                    ), OpCode.Prefix.Unaligned(4), OpCode.Prefix.Volatile)
                    insn(OpCode.Code.ret)
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
                  LABEL_0: switch (LABEL_1, LABEL_2, LABEL_3)
                  LABEL_1: ret
                  LABEL_2: ret
                  LABEL_3: ret
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
                    label()
                    switch(listOf(l0, l1, l2))
                    label(l0)
                    insn(OpCode.Code.ret)
                    label(l1)
                    insn(OpCode.Code.ret)
                    label(l2)
                    insn(OpCode.Code.ret)
                }
            }
        )
    }
}
