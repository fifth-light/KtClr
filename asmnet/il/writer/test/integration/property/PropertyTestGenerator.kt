/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.property

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: PropertyTestGenerator <output.il>" }
    val il = generatePropertyTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val propertyType = TypeReference(name = "PropertyTest")

private fun generatePropertyTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("property_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("property_test.dll")
        class_("PropertyTest",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            field("_value", Type.Int32, attributes = FieldAttributes(FieldAttributes.Private))

            method(".ctor",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                    MethodAttribute.RTSpecialName,
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
                ldc(42)
                insn(OpCode.Code.stfld, FieldReference(propertyType, "_value", Type.Int32))
                insn(OpCode.Code.ret)
            }

            method("get_Value",
                callConv = CallConv(instance = true),
                returnType = Type.Int32,
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                ),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldfld, FieldReference(propertyType, "_value", Type.Int32))
                insn(OpCode.Code.ret)
            }

            method("set_Value",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                ),
                parameters = listOf(Parameter(Type.Int32, "value")),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldarg1)
                insn(OpCode.Code.stfld, FieldReference(propertyType, "_value", Type.Int32))
                insn(OpCode.Code.ret)
            }

            property("Value", Type.Int32) {
                get(MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = "get_Value",
                    returnType = Type.Int32,
                ))
                set(MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = "set_Value",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
            }

            method("Main",
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.Static,
                    MethodAttribute.HideBySig,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                maxStack(2)
                code()
                locals(LocalVariable(propertyType))
                insn(OpCode.Code.newobj, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = ".ctor",
                    returnType = Type.Void,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.stloc0)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.callvirt, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = "get_Value",
                    returnType = Type.Int32,
                ))
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                insn(OpCode.Code.ldloc0)
                ldc(99)
                insn(OpCode.Code.callvirt, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = "set_Value",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.callvirt, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = propertyType,
                    name = "get_Value",
                    returnType = Type.Int32,
                ))
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
