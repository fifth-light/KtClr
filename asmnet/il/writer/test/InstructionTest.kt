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
                        MethodParameter(Type.String, "a"),
                        MethodParameter(Type.String, "b"),
                        MethodParameter(Type.String, "c"),
                        MethodParameter(Type.String, "d"),
                        MethodParameter(Type.String, "e"),
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
}
