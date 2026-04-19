/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

sealed interface DataItem {
    data class Int8(val value: Byte, val repeatCount: Int? = null) : DataItem
    data class Int16(val value: Short, val repeatCount: Int? = null) : DataItem
    data class Int32(val value: Int, val repeatCount: Int? = null) : DataItem
    data class Int64(val value: Long, val repeatCount: Int? = null) : DataItem
    data class Float32(val value: Float, val repeatCount: Int? = null) : DataItem
    data class Float64(val value: Double, val repeatCount: Int? = null) : DataItem

    data class ByteArray(val value: kotlin.ByteArray) : DataItem {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ByteArray

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    data class CharArray(val value: kotlin.CharArray) : DataItem {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CharArray

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    data class AddressOf(val label: DataLabel) : DataItem
}
