/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.type_forwarder

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: LibGenerator <output.il>" }
    File(args[0]).writeText(generateLib())
}

private fun generateLib(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime")
        assembly("Lib")
        module("Lib.dll")

        class_(
            name = "MyHelper",
            attrs = TypeAttributes(TypeAttributes.Public, TypeAttributes.BeforeFieldInit),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            method(
                name = "GetMessage",
                returnType = Type.String,
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.Static,
                    MethodAttribute.HideBySig,
                ),
            ) {
                maxStack(1)
                code()
                ldc("Type forwarded!")
                insn(OpCode.Code.ret)
            }

            class_(
                name = "NestedHelper",
                attrs = TypeAttributes(TypeAttributes.NestedPublic, TypeAttributes.BeforeFieldInit),
            ) {
                method(
                    name = "GetMessage",
                    returnType = Type.String,
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                ) {
                    maxStack(1)
                    code()
                    ldc("Nested forwarded!")
                    insn(OpCode.Code.ret)
                }
            }
        }
    }
    it.toString()
}
