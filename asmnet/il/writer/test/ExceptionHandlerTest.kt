/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class ExceptionHandlerTest {
    @Test
    fun testCatch() {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  leave LABEL_1
                  LABEL_2: pop
                  leave LABEL_1
                  LABEL_1: ret
                  .try LABEL_0 to LABEL_2 catch [System.Runtime]System.Exception handler LABEL_2 to LABEL_1
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label(tryStart)
                    insn(OpCode.Code.nop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(handlerStart)
                    insn(OpCode.Code.pop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(handlerEnd)
                    insn(OpCode.Code.ret)
                    exceptionHandler(
                        flags = ExceptionFlag.Exception,
                        tryStart = tryStart,
                        tryEnd = handlerStart,
                        handlerStart = handlerStart,
                        handlerEnd = handlerEnd,
                        exceptionType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Exception",
                        ),
                    )
                }
            }
        )
    }

    @Test
    fun testFilter() {
        val tryStart = Label()
        val filterStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  leave LABEL_1
                  LABEL_2: pop
                  ldc.i4 0x00000001
                  LABEL_3: pop
                  leave LABEL_1
                  LABEL_1: ret
                  .try LABEL_0 to LABEL_2 filter LABEL_2 handler LABEL_3 to LABEL_1
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label(tryStart)
                    insn(OpCode.Code.nop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(filterStart)
                    insn(OpCode.Code.pop)
                    ldc(1)
                    label(handlerStart)
                    insn(OpCode.Code.pop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(handlerEnd)
                    insn(OpCode.Code.ret)
                    exceptionHandler(
                        flags = ExceptionFlag.Filter,
                        tryStart = tryStart,
                        tryEnd = filterStart,
                        handlerStart = handlerStart,
                        handlerEnd = handlerEnd,
                        filterStart = filterStart,
                    )
                }
            }
        )
    }

    @Test
    fun testFinally() {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  leave LABEL_1
                  LABEL_2: endfinally
                  LABEL_1: ret
                  .try LABEL_0 to LABEL_2 finally handler LABEL_2 to LABEL_1
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label(tryStart)
                    insn(OpCode.Code.nop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(handlerStart)
                    insn(OpCode.Code.endfinally)
                    label(handlerEnd)
                    insn(OpCode.Code.ret)
                    exceptionHandler(
                        flags = ExceptionFlag.Finally,
                        tryStart = tryStart,
                        tryEnd = handlerStart,
                        handlerStart = handlerStart,
                        handlerEnd = handlerEnd,
                    )
                }
            }
        )
    }

    @Test
    fun testFault() {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        assertContentEquals(
            expected = """
                .method public static hidebysig void Main() cil managed
                {
                  .maxstack 8
                  LABEL_0: nop
                  leave LABEL_1
                  LABEL_2: endfinally
                  LABEL_1: ret
                  .try LABEL_0 to LABEL_2 fault handler LABEL_2 to LABEL_1
                }
            """.trimIndent(),
            actual = generateText {
                method("Main",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.HideBySig,
                    ),
                    implAttributes = ImplementationAttributes(ImplementationAttributes.IL),
                ) {
                    maxStack(8)
                    code()
                    label(tryStart)
                    insn(OpCode.Code.nop)
                    insn(OpCode.Code.leave, handlerEnd)
                    label(handlerStart)
                    insn(OpCode.Code.endfault)
                    label(handlerEnd)
                    insn(OpCode.Code.ret)
                    exceptionHandler(
                        flags = ExceptionFlag.Fault,
                        tryStart = tryStart,
                        tryEnd = handlerStart,
                        handlerStart = handlerStart,
                        handlerEnd = handlerEnd,
                    )
                }
            }
        )
    }
}
