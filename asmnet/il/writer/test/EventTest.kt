/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

private val delegateType = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.EventHandler",
)

private val eventType = TypeReference(name = "MyClass")

class EventTest {
    @Test
    fun testEventAddOnRemoveOn() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventWithFire() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                    .fire instance void MyClass::OnMyEvent()
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        fire(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "OnMyEvent",
                            returnType = Type.Void,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventWithOther() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                    .other instance void MyClass::ResetMyEvent()
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        other(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "ResetMyEvent",
                            returnType = Type.Void,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventWithAllAccessors() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                    .fire instance void MyClass::OnMyEvent()
                    .other instance void MyClass::ResetMyEvent()
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        fire(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "OnMyEvent",
                            returnType = Type.Void,
                        ))
                        other(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "ResetMyEvent",
                            returnType = Type.Void,
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventWithSpecialName() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event specialname class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType,
                        attributes = EventAttributes(EventAttributes.SpecialName),
                    ) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventWithRTSpecialName() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event rtspecialname class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType,
                        attributes = EventAttributes(EventAttributes.RTSpecialName),
                    ) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventNoAttributes() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                    .removeon instance void MyClass::remove_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                        removeOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "remove_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventCustomAttributeWithoutBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .custom instance void [System.Runtime]System.CLSCompliantAttribute::.ctor()
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.CLSCompliantAttribute",
                                ),
                            ),
                            blob = null,
                        )
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }

    @Test
    fun testEventCustomAttributeWithBlob() {
        assertContentEquals(
            expected = """
                .class public auto ansi MyClass
                {
                  .event class [System.Runtime]System.EventHandler MyEvent
                  {
                    .custom instance void [System.Runtime]System.ObsoleteAttribute::.ctor(string) = ( 01 00 05 68 65 6C 6C 6F 00 00 )
                    .addon instance void MyClass::add_MyEvent(class [System.Runtime]System.EventHandler)
                  } // end of event MyEvent
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass") {
                    event("MyEvent", delegateType) {
                        custom(
                            CustomAttributeReference(
                                attributeType = TypeReference(
                                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                                    name = "System.ObsoleteAttribute",
                                ),
                                parameterTypes = listOf(Type.String),
                            ),
                            blob = byteArrayOf(0x01, 0x00, 0x05, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x00, 0x00),
                        )
                        addOn(MethodReference(
                            callConv = CallConv(instance = true),
                            declaringType = eventType,
                            name = "add_MyEvent",
                            returnType = Type.Void,
                            parameterTypes = listOf(delegateType),
                        ))
                    }
                }
            }
        )
    }
}
