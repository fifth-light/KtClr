package top.fifthlight.asmnet.il.writer.test.integration.field

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: FieldTestGenerator <output.il>" }
    val il = generateFieldTest()
    File(args[0]).writeText(il)
}

private val fieldType = TypeReference(name = "FieldTest")

private fun generateFieldTest(): String = StringWriter().use {
    ILTextModuleWriter(it).apply {
        visitExternAssembly(
            name = "System.Runtime",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = byteArrayOf(
                    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
                    0x11, 0xD5.toByte(), 0x0A, 0x3A,
                ),
                version = Version(10, 0, 0, 0),
            ),
        )
        visitExternAssembly(
            name = "System.Console",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = byteArrayOf(
                    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
                    0x11, 0xD5.toByte(), 0x0A, 0x3A,
                ),
                version = Version(10, 0, 0, 0),
            ),
        )
        visitAssembly(
            name = "field_test",
            declaration = AssemblyDeclaration(
                hash = HashAlgorithm.SHA1,
                version = Version(0, 0, 0, 0),
            ),
        )
        visitModule("field_test.dll")
        visitClass("FieldTest")!!.apply {
            visit(
                attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.BeforeFieldInit,
                ),
                extends = TypeReference(
                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                    name = "System.Object",
                ),
            )
            visitField(
                name = "instanceField",
                type = Type.Int32,
                attributes = FieldAttributes(FieldAttributes.Private),
            )
            visitField(
                name = "staticField",
                type = Type.Int32,
                attributes = FieldAttributes(
                    FieldAttributes.Public,
                    FieldAttributes.Static,
                ),
            )
            visitMethod(
                name = ".ctor",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.HideBySig,
                    MethodAttributes.SpecialName,
                    MethodAttributes.RTSpecialName,
                ),
            )!!.apply {
                visitMaxStack(8)
                visitInsn(OpCode(OpCode.Code.ldarg0))
                visitMethodInsn(
                    opcode = OpCode(OpCode.Code.call),
                    MethodReference(
                        callConv = CallConv(instance = true),
                        declaringType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Object",
                        ),
                        name = ".ctor",
                        returnType = Type.Void,
                        parameterTypes = emptyList(),
                    ),
                )
                visitInsn(OpCode(OpCode.Code.ldarg0))
                visitLdc(10)
                visitFieldInsn(
                    opcode = OpCode(OpCode.Code.stfld),
                    FieldReference(
                        declaringType = fieldType,
                        name = "instanceField",
                        fieldType = Type.Int32,
                    ),
                )
                visitInsn(OpCode(OpCode.Code.ret))
                visitEnd()
            }
            visitMethod(
                name = "Main",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.Static,
                    MethodAttributes.HideBySig,
                ),
                parameters = listOf(
                    MethodParameter(
                        type = Type.Array(Type.String),
                        name = "args",
                    ),
                ),
                entryPoint = true,
            )!!.apply {
                visitMaxStack(8)
                visitMethodInsn(
                    opcode = OpCode(OpCode.Code.newobj),
                    MethodReference(
                        callConv = CallConv(instance = true),
                        declaringType = fieldType,
                        name = ".ctor",
                        returnType = Type.Void,
                        parameterTypes = emptyList(),
                    ),
                )
                visitFieldInsn(
                    opcode = OpCode(OpCode.Code.ldfld),
                    FieldReference(
                        declaringType = fieldType,
                        name = "instanceField",
                        fieldType = Type.Int32,
                    ),
                )
                visitFieldInsn(
                    opcode = OpCode(OpCode.Code.stsfld),
                    FieldReference(
                        declaringType = fieldType,
                        name = "staticField",
                        fieldType = Type.Int32,
                    ),
                )
                visitFieldInsn(
                    opcode = OpCode(OpCode.Code.ldsfld),
                    FieldReference(
                        declaringType = fieldType,
                        name = "staticField",
                        fieldType = Type.Int32,
                    ),
                )
                visitMethodInsn(
                    opcode = OpCode(OpCode.Code.call),
                    MethodReference(
                        declaringType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Console"),
                            name = "System.Console",
                        ),
                        name = "WriteLine",
                        returnType = Type.Void,
                        parameterTypes = listOf(Type.Int32),
                    ),
                )
                visitInsn(OpCode(OpCode.Code.ret))
                visitEnd()
            }
            visitEnd()
        }
        visitEnd()
    }
    it.toString()
}
