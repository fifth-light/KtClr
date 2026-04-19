/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.hello_world

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: HelloWorldGenerator <output.il>" }
    val il = generateHelloWorld()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private fun generateHelloWorld(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly(
            name = "System.Runtime",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = runtimeToken,
                version = Version(10, 0, 0, 0),
            )
        )
        externAssembly(
            name = "System.Console",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = runtimeToken,
                version = Version(10, 0, 0, 0),
            )
        )
        assembly(
            name = "hello_world",
            declaration = AssemblyDeclaration(
                hash = HashAlgorithm.SHA1,
                version = Version(0, 0, 0, 0),
            )
        )

        module("hello_world.dll")
        imageBase(0x00400000u)
        fileAlignment(0x00000200u)
        stackReserve(0x00100000u)
        subsystem(Subsystem.WINDOWS_CUI)
        corFlags(RuntimeFlags(RuntimeFlags.ILONLY))

        class_(
            name = "Hello",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            method(
                name = "Main",
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.Static,
                    MethodAttribute.HideBySig,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                maxStack(8)
                code()
                ldc("Hello, world!")
                insn(
                    code = OpCode.Code.call,
                    ref = MethodReference(
                        declaringType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Console"),
                            name = "System.Console",
                        ),
                        name = "WriteLine",
                        returnType = Type.Void,
                        parameterTypes = listOf(Type.String),
                    ),
                )
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
