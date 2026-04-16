package top.fifthlight.asmnet.il.writer.test.integration.jump_insn

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: JumpInsnGenerator <output.il>" }
    val il = generateJumpInsnTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private fun generateJumpInsnTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("jump_insn", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("jump_insn.dll")
        class_("JumpTest/Program",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.AutoLayout,
                TypeAttributes.AnsiClass,
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
                    MethodAttributes.HideBySig,
                    MethodAttributes.Static,
                ),
                parameters = listOf(MethodParameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                locals(
                    LocalVariable(Type.Int32, "sum"),
                    LocalVariable(Type.Int32, "x"),
                    LocalVariable(Type.Int32, "i"),
                    LocalVariable(Type.Bool, "loopCond"),
                    LocalVariable(Type.Bool, "ifCond"),
                )
                maxStack(2)
                insn(OpCode.Code.ldcI40)
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.ldcI41)
                insn(OpCode.Code.stloc, 2)
                val loopCond = Label()
                insn(OpCode.Code.brS, loopCond)
                val loopBody = Label()
                label(loopBody)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldloc, 2)
                insn(OpCode.Code.add)
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.ldloc, 2)
                insn(OpCode.Code.ldcI41)
                insn(OpCode.Code.add)
                insn(OpCode.Code.stloc, 2)
                label(loopCond)
                insn(OpCode.Code.ldloc, 2)
                insn(OpCode.Code.ldcI45)
                insn(OpCode.Code.cgt)
                insn(OpCode.Code.ldcI40)
                insn(OpCode.Code.ceq)
                insn(OpCode.Code.stloc, 3)
                insn(OpCode.Code.ldloc, 3)
                insn(OpCode.Code.brtrueS, loopBody)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                ldc(10)
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.ldcI45)
                insn(OpCode.Code.cgt)
                insn(OpCode.Code.stloc, 4)
                insn(OpCode.Code.ldloc, 4)
                val elseLabel = Label()
                insn(OpCode.Code.brfalseS, elseLabel)
                ldc("big")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                val endLabel = Label()
                insn(OpCode.Code.brS, endLabel)
                label(elseLabel)
                ldc("small")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                label(endLabel)
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
