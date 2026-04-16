package top.fifthlight.asmnet.il.writer.test.integration.field

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: FieldTestGenerator <output.il>" }
    val il = generateFieldTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val fieldType = TypeReference(name = "FieldTest")

private fun generateFieldTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("field_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("field_test.dll")
        class_("FieldTest",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            field("instanceField", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))
            field("staticField", Type.Int32, attributes = FieldAttributes(
                FieldAttributes.Public,
                FieldAttributes.Static,
            ))
            method(".ctor",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.HideBySig,
                    MethodAttributes.SpecialName,
                    MethodAttributes.RTSpecialName,
                ),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.call, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ),
                    name = ".ctor",
                    returnType = Type.Void,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.ldarg0)
                ldc(10)
                insn(OpCode.Code.stfld, FieldReference(fieldType, "instanceField", Type.Int32))
                insn(OpCode.Code.ret)
            }
            method("Main",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.Static,
                    MethodAttributes.HideBySig,
                ),
                parameters = listOf(MethodParameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                maxStack(8)
                insn(OpCode.Code.newobj, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = fieldType,
                    name = ".ctor",
                    returnType = Type.Void,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.ldfld, FieldReference(fieldType, "instanceField", Type.Int32))
                insn(OpCode.Code.stsfld, FieldReference(fieldType, "staticField", Type.Int32))
                insn(OpCode.Code.ldsfld, FieldReference(fieldType, "staticField", Type.Int32))
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
