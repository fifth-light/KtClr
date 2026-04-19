/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.4
sealed interface NativeType {
    data object Boolean : NativeType
    data object Int8 : NativeType
    data object UnsignedInt8 : NativeType
    data object Int16 : NativeType
    data object UnsignedInt16 : NativeType
    data object Int32 : NativeType
    data object UnsignedInt32 : NativeType
    data object Int64 : NativeType
    data object UnsignedInt64 : NativeType
    data object Float32 : NativeType
    data object Float64 : NativeType
    data object LPStr : NativeType
    data object LPWStr : NativeType
    data object SysInt : NativeType
    data object SysUInt : NativeType
    data object Method : NativeType

    // ECMA-335 II.7.4: nativeType '*'
    data class Pointer(val type: NativeType) : NativeType

    // ECMA-335 II.7.4: nativeType '[' ']' | nativeType '[' int32 ']' | nativeType '[' int32 '+' int32 ']' | nativeType '[' '+' int32 ']'
    data class Array(
        val elementType: NativeType,
        val size: Int? = null,
        val sizeParamIndex: Int? = null,
    ) : NativeType
}
