package top.fifthlight.asmnet.il.writer.test.integration.switch_insn

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: SwitchInsnGenerator <output.il>" }
    val il = generateSwitchInsnTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val systemConsole = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Console"),
    name = "System.Console",
)

private fun generateSwitchInsnTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("switch_insn", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("switch_insn.dll")
        class_("SwitchTest",
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
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                locals(
                    LocalVariable(Type.Int32, "x"),
                    LocalVariable(Type.Int32, "tmp"),
                    LocalVariable(Type.Int32, "switchVal"),
                )
                maxStack(1)
                code()
                insn(OpCode.Code.ldcI42)
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.stloc, 2)
                insn(OpCode.Code.ldloc, 2)
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 1)
                val caseZero = Label()
                val caseOne = Label()
                val caseTwo = Label()
                val defaultCase = Label()
                val end = Label()
                switch(listOf(caseZero, caseOne, caseTwo))
                insn(OpCode.Code.brS, defaultCase)
                label(caseZero)
                ldc("zero")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                insn(OpCode.Code.brS, end)
                label(caseOne)
                ldc("one")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                insn(OpCode.Code.brS, end)
                label(caseTwo)
                ldc("two")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                insn(OpCode.Code.brS, end)
                label(defaultCase)
                ldc("other")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                label(end)
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
