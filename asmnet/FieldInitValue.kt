package top.fifthlight.asmnet

import kotlin.Boolean as KBoolean
import kotlin.Char as KChar
import kotlin.String as KString

// ECMA-335 II.16.2
sealed interface FieldInitValue {
    data class Boolean(val value: KBoolean) : FieldInitValue
    data class Char(val value: KChar) : FieldInitValue
    data class Int8(val value: Byte) : FieldInitValue
    data class Int16(val value: Short) : FieldInitValue
    data class Int32(val value: Int) : FieldInitValue
    data class Int64(val value: Long) : FieldInitValue
    data class UnsignedInt8(val value: UByte) : FieldInitValue
    data class UnsignedInt16(val value: UShort) : FieldInitValue
    data class UnsignedInt32(val value: UInt) : FieldInitValue
    data class UnsignedInt64(val value: ULong) : FieldInitValue
    data class Float32(val value: Float) : FieldInitValue
    data class Float64(val value: Double) : FieldInitValue
    data class String(val value: KString) : FieldInitValue
    data object NullRef : FieldInitValue

    data class ByteArray(val value: kotlin.ByteArray) : FieldInitValue {
        override fun equals(other: Any?): KBoolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ByteArray

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int {
            return value.contentHashCode()
        }
    }
}
