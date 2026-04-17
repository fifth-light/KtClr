package top.fifthlight.asmnet.il.writer.test.integration.local_variable

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: LocalVariableGenerator <output.il>" }
    val il = generateLocalVariableTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private fun generateLocalVariableTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("local_variable", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("local_variable.dll")
        class_("Program",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            method("Main",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.Static,
                    MethodAttributes.HideBySig,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                locals(
                    LocalVariable(Type.Int32, "x"),
                    LocalVariable(Type.Int32, "y"),
                )
                maxStack(2)
                code()
                ldc(10)
                insn(OpCode.Code.stloc, 0)
                ldc(20)
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.add)
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
