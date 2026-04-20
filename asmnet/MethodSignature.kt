/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.2.3 StandAloneMethodSig
data class MethodSignature(
    val callConv: CallConv = CallConv(),
    val returnType: TypeSpec = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
    val sentinelIndex: Int? = null,
)
