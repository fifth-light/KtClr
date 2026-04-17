package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.CallConv
import top.fifthlight.asmnet.EventAttributes
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.Parameter
import top.fifthlight.asmnet.ResolutionScope
import top.fifthlight.asmnet.Type
import top.fifthlight.asmnet.TypeAttributes
import top.fifthlight.asmnet.TypeReference

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
}
