/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.try_catch

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: TryCatchGenerator <output.il>" }
    val il = generateTryCatchTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val systemException = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.Exception",
)

private val exceptionCtor = MethodReference(
    callConv = CallConv(instance = true),
    declaringType = systemException,
    name = ".ctor",
    returnType = Type.Void,
    parameterTypes = emptyList(),
)

private val consoleWriteLineString = MethodReference(
    declaringType = TypeReference(
        resolutionScope = ResolutionScope.Assembly("System.Console"),
        name = "System.Console",
    ),
    name = "WriteLine",
    returnType = Type.Void,
    parameterTypes = listOf(Type.String),
)

private fun generateTryCatchTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("try_catch_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("try_catch_test.dll")
        class_("TryCatchTest",
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
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.Static,
                    MethodAttribute.HideBySig,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                maxStack(1)
                code()
                val exitLabel = Label()
                tryCatch(
                    exceptionType = systemException,
                    tryBlock = {
                        insn(OpCode.Code.newobj, exceptionCtor)
                        insn(OpCode.Code.`throw`)
                    },
                    catchBlock = {
                        insn(OpCode.Code.pop)
                        ldc("Caught!")
                        insn(OpCode.Code.call, consoleWriteLineString)
                        insn(OpCode.Code.leave, exitLabel)
                    },
                )
                label(exitLabel)
                ldc("Done")
                insn(OpCode.Code.call, consoleWriteLineString)
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
