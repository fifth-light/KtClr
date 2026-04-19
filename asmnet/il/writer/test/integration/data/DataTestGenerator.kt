/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.data

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: DataTestGenerator <output.il>" }
    val il = generateDataTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val myType = TypeReference(name = "DataTest")

private fun generateDataTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("data_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("data_test.dll")
        class_("DataTest",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            val theInt = DataLabel()
            data(label = theInt, items = listOf(DataItem.Int32(42)))
            field(
                "MyInt",
                Type.Int32,
                attributes = FieldAttributes(
                    FieldAttributes.Public,
                    FieldAttributes.Static,
                ),
                dataLabel = theInt,
            )
            method("Main",
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
                insn(OpCode.Code.ldsfld, FieldReference(myType, "MyInt", Type.Int32))
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
