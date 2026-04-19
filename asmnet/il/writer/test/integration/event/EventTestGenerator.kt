/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.event

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: EventTestGenerator <output.il>" }
    val il = generateEventTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val eventHandlerType = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.EventHandler",
)

private val eventType = TypeReference(name = "EventTest")

private val timeUpFieldRef = FieldReference(eventType, "TimeUp", eventHandlerType)

private val countFieldRef = FieldReference(eventType, "count", Type.Int32)

private val eventArgsEmptyFieldRef = FieldReference(
    declaringType = TypeReference(
        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
        name = "System.EventArgs",
    ),
    name = "Empty",
    fieldType = TypeReference(
        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
        name = "System.EventArgs",
    ),
)

private val delegateType = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.Delegate",
)

private val objectRef = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.Object",
)

private val eventArgsRef = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.EventArgs",
)

private val combineRef = MethodReference(
    declaringType = delegateType,
    name = "Combine",
    returnType = delegateType,
    parameterTypes = listOf(delegateType, delegateType),
)

private val removeRef = MethodReference(
    declaringType = delegateType,
    name = "Remove",
    returnType = delegateType,
    parameterTypes = listOf(delegateType, delegateType),
)

private val invokeRef = MethodReference(
    callConv = CallConv(instance = true),
    declaringType = eventHandlerType,
    name = "Invoke",
    returnType = Type.Void,
    parameterTypes = listOf(objectRef, eventArgsRef),
)

private val handler1Ref = MethodReference(
    declaringType = eventType,
    name = "Handler1",
    returnType = Type.Void,
    parameterTypes = listOf(objectRef, eventArgsRef),
)

private val handler2Ref = MethodReference(
    declaringType = eventType,
    name = "Handler2",
    returnType = Type.Void,
    parameterTypes = listOf(objectRef, eventArgsRef),
)

private val addTimeUpRef = MethodReference(
    callConv = CallConv(instance = true),
    declaringType = eventType,
    name = "add_TimeUp",
    returnType = Type.Void,
    parameterTypes = listOf(eventHandlerType),
)

private val onTimeUpRef = MethodReference(
    callConv = CallConv(instance = true),
    declaringType = eventType,
    name = "OnTimeUp",
    returnType = Type.Void,
    parameterTypes = emptyList(),
)

private val writeLineStringRef = MethodReference(
    declaringType = TypeReference(
        resolutionScope = ResolutionScope.Assembly("System.Console"),
        name = "System.Console",
    ),
    name = "WriteLine",
    returnType = Type.Void,
    parameterTypes = listOf(Type.String),
)

private val writeLineIntRef = MethodReference(
    declaringType = TypeReference(
        resolutionScope = ResolutionScope.Assembly("System.Console"),
        name = "System.Console",
    ),
    name = "WriteLine",
    returnType = Type.Void,
    parameterTypes = listOf(Type.Int32),
)

private val eventHandlerCtorRef = MethodReference(
    callConv = CallConv(instance = true),
    declaringType = eventHandlerType,
    name = ".ctor",
    returnType = Type.Void,
    parameterTypes = listOf(objectRef, Type.NativeInt),
)

private fun generateEventTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("event_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("event_test.dll")
        class_("EventTest",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            field("TimeUp", eventHandlerType, attributes = FieldAttributes(FieldAttributes.Private))
            field("count", Type.Int32, attributes = FieldAttributes(
                FieldAttributes.Private,
                FieldAttributes.Static,
            ))

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
                insn(OpCode.Code.ret)
            }

            method("add_TimeUp",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                ),
                parameters = listOf(Parameter(eventHandlerType, "value")),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldfld, timeUpFieldRef)
                insn(OpCode.Code.ldarg1)
                insn(OpCode.Code.call, combineRef)
                insn(OpCode.Code.castclass, eventHandlerType)
                insn(OpCode.Code.stfld, timeUpFieldRef)
                insn(OpCode.Code.ret)
            }

            method("remove_TimeUp",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                ),
                parameters = listOf(Parameter(eventHandlerType, "value")),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldfld, timeUpFieldRef)
                insn(OpCode.Code.ldarg1)
                insn(OpCode.Code.call, removeRef)
                insn(OpCode.Code.castclass, eventHandlerType)
                insn(OpCode.Code.stfld, timeUpFieldRef)
                insn(OpCode.Code.ret)
            }

            method("OnTimeUp",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                ),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldfld, timeUpFieldRef)
                insn(OpCode.Code.dup)
                val invokeLabel = Label()
                insn(OpCode.Code.brtrue, invokeLabel)
                insn(OpCode.Code.pop)
                insn(OpCode.Code.ret)
                label(invokeLabel)
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.ldsfld, eventArgsEmptyFieldRef)
                insn(OpCode.Code.callvirt, invokeRef)
                insn(OpCode.Code.ret)
            }

            event("TimeUp", eventHandlerType) {
                addOn(addTimeUpRef)
                removeOn(MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = eventType,
                    name = "remove_TimeUp",
                    returnType = Type.Void,
                    parameterTypes = listOf(eventHandlerType),
                ))
            }

            method("Handler1",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                ),
                parameters = listOf(Parameter(objectRef, "sender"), Parameter(eventArgsRef, "e")),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldsfld, countFieldRef)
                insn(OpCode.Code.ldcI41)
                insn(OpCode.Code.add)
                insn(OpCode.Code.stsfld, countFieldRef)
                ldc("Event fired!")
                insn(OpCode.Code.call, writeLineStringRef)
                insn(OpCode.Code.ret)
            }

            method("Handler2",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                ),
                parameters = listOf(Parameter(objectRef, "sender"), Parameter(eventArgsRef, "e")),
            ) {
                maxStack(8)
                insn(OpCode.Code.ldsfld, countFieldRef)
                insn(OpCode.Code.ldcI41)
                insn(OpCode.Code.add)
                insn(OpCode.Code.stsfld, countFieldRef)
                ldc("Another handler!")
                insn(OpCode.Code.call, writeLineStringRef)
                insn(OpCode.Code.ret)
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
                maxStack(4)
                code()
                locals(LocalVariable(eventType))
                insn(OpCode.Code.newobj, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = eventType,
                    name = ".ctor",
                    returnType = Type.Void,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.stloc0)
                insn(OpCode.Code.ldcI40)
                insn(OpCode.Code.stsfld, countFieldRef)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.ldnull)
                insn(OpCode.Code.ldftn, handler1Ref)
                insn(OpCode.Code.newobj, eventHandlerCtorRef)
                insn(OpCode.Code.callvirt, addTimeUpRef)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.ldnull)
                insn(OpCode.Code.ldftn, handler2Ref)
                insn(OpCode.Code.newobj, eventHandlerCtorRef)
                insn(OpCode.Code.callvirt, addTimeUpRef)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.callvirt, onTimeUpRef)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.callvirt, onTimeUpRef)
                insn(OpCode.Code.ldloc0)
                insn(OpCode.Code.callvirt, onTimeUpRef)
                insn(OpCode.Code.ldsfld, countFieldRef)
                insn(OpCode.Code.call, writeLineIntRef)
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
