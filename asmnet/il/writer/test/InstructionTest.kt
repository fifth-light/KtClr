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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.nop))
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ret))
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
                  LABEL_0: ldc.i4 0x0000002A
                  LABEL_1: ldc.i8 0x000000000001E240
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
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
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
                    visitInsn(OpCode(OpCode.Code.ret))
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
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitMethodInsn(
                        OpCode(OpCode.Code.call),
                        MethodReference(
                            declaringType = Type.Object,
                            name = "WriteLine",
                            returnType = Type.Void,
                            parameterTypes = listOf(Type.String),
                        ),
                    )
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ret))
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
                    callConv = CallConv(instance = true),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ldarg0))
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ret))
                    visitEnd()
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
                visitMethod(
                    name = "Foo",
                    returnType = Type.Void,
                    callConv = CallConv(),
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
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitVarInsn(OpCode(OpCode.Code.ldargS), 4)
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ret))
                    visitEnd()
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
                visitMethod(
                    name = "Main",
                    returnType = Type.Void,
                    callConv = CallConv(),
                    attributes = MethodAttributes(
                        MethodAttributes.Public,
                        MethodAttributes.Static,
                        MethodAttributes.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                    parameters = emptyList(),
                )!!.apply {
                    visitMaxStack(8)
                    visitLabel(Label())
                    visitFieldInsn(
                        OpCode(OpCode.Code.ldfld),
                        FieldReference(
                            declaringType = TypeReference("MyClass"),
                            name = "_count",
                            fieldType = Type.Int32,
                        ),
                    )
                    visitLabel(Label())
                    visitFieldInsn(
                        OpCode(OpCode.Code.stfld),
                        FieldReference(
                            declaringType = TypeReference("MyClass"),
                            name = "_count",
                            fieldType = Type.Int32,
                        ),
                    )
                    visitLabel(Label())
                    visitInsn(OpCode(OpCode.Code.ret))
                    visitEnd()
                }
            }
        )
    }
}
