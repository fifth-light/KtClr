/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class DataTest {
    @Test
    fun testSingleDataItem() {
        assertContentEquals(
            expected = """
                .data D_0 = int32(0x0000007B)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Int32(123)),
                )
            }
        )
    }

    @Test
    fun testDataWithoutLabel() {
        assertContentEquals(
            expected = """
                .data int32(0x0000007B)
            """.trimIndent(),
            actual = generateText {
                data(items = listOf(DataItem.Int32(123)))
            }
        )
    }

    @Test
    fun testDataTls() {
        assertContentEquals(
            expected = """
                .data tls D_0 = int32(0x0000007B)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    tls = true,
                    items = listOf(DataItem.Int32(123)),
                )
            }
        )
    }

    @Test
    fun testDataInt8() {
        assertContentEquals(
            expected = """
                .data D_0 = int8(0xFF)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Int8((-1).toByte())),
                )
            }
        )
    }

    @Test
    fun testDataInt16() {
        assertContentEquals(
            expected = """
                .data D_0 = int16(0x0100)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Int16(256)),
                )
            }
        )
    }

    @Test
    fun testDataInt64() {
        assertContentEquals(
            expected = """
                .data D_0 = int64(0x0000000000000064)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Int64(100)),
                )
            }
        )
    }

    @Test
    fun testDataFloat32() {
        assertContentEquals(
            expected = """
                .data D_0 = float32(3.14)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Float32(3.14f)),
                )
            }
        )
    }

    @Test
    fun testDataFloat64() {
        assertContentEquals(
            expected = """
                .data D_0 = float64(3.14)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Float64(3.14)),
                )
            }
        )
    }

    @Test
    fun testDataByteArray() {
        assertContentEquals(
            expected = """
                .data D_0 = bytearray(01 02 03)
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.ByteArray(byteArrayOf(1, 2, 3))),
                )
            }
        )
    }

    @Test
    fun testDataCharArray() {
        assertContentEquals(
            expected = """
                .data D_0 = char*("Hello")
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.CharArray(charArrayOf('H', 'e', 'l', 'l', 'o'))),
                )
            }
        )
    }

    @Test
    fun testDataRepeatCount() {
        assertContentEquals(
            expected = """
                .data D_0 = int8(0x03)[10]
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.Int8(3, repeatCount = 10)),
                )
            }
        )
    }

    @Test
    fun testDataMultipleItems() {
        assertContentEquals(
            expected = """
                .data D_0 =
                {
                  int32(0x00000001),
                  int32(0x00000002),
                  int32(0x00000003)
                }
            """.trimIndent(),
            actual = generateText {
                data(
                    label = DataLabel(),
                    items = listOf(
                        DataItem.Int32(1),
                        DataItem.Int32(2),
                        DataItem.Int32(3),
                    ),
                )
            }
        )
    }

    @Test
    fun testFieldAtDataLabel() {
        val label = DataLabel()
        assertContentEquals(
            expected = """
                .data D_0 = int32(0x0000007B)
                .field public static int32 myInt at D_0
            """.trimIndent(),
            actual = generateText {
                data(label = label, items = listOf(DataItem.Int32(123)))
                field("myInt", Type.Int32, attributes = FieldAttributes(
                    FieldAttributes.Public,
                    FieldAttributes.Static,
                ), dataLabel = label)
            }
        )
    }

    @Test
    fun testDataInClass() {
        val label = DataLabel()
        assertContentEquals(
            expected = """
                .class public auto ansi beforefieldinit MyClass
                extends [System.Runtime]System.Object
                {
                  .data D_0 = int32(0x0000007B)
                  .field public static int32 myInt at D_0
                } // end of class MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyClass",
                    attrs = TypeAttributes(
                        TypeAttributes.Public,
                        TypeAttributes.BeforeFieldInit,
                    ),
                    extends = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ),
                ) {
                    data(label = label, items = listOf(DataItem.Int32(123)))
                    field("myInt", Type.Int32, attributes = FieldAttributes(
                        FieldAttributes.Public,
                        FieldAttributes.Static,
                    ), dataLabel = label)
                }
            }
        )
    }

    @Test
    fun testDataAddressOf() {
        val target = DataLabel()
        assertContentEquals(
            expected = """
                .data D_0 = int32(0x0000007B)
                .data D_1 = &(D_0)
            """.trimIndent(),
            actual = generateText {
                data(label = target, items = listOf(DataItem.Int32(123)))
                data(
                    label = DataLabel(),
                    items = listOf(DataItem.AddressOf(target)),
                )
            }
        )
    }

    @Test
    fun testDataLabelIndexing() {
        val label0 = DataLabel()
        val label1 = DataLabel()
        val label2 = DataLabel()
        assertContentEquals(
            expected = """
                .data D_0 = int32(0x00000001)
                .data D_1 = int32(0x00000002)
                .data D_2 = int32(0x00000003)
            """.trimIndent(),
            actual = generateText {
                data(label = label0, items = listOf(DataItem.Int32(1)))
                data(label = label1, items = listOf(DataItem.Int32(2)))
                data(label = label2, items = listOf(DataItem.Int32(3)))
            }
        )
    }
}
