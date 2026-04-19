/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.21
data class CustomAttributeReference(
    val callConv: CallConv = CallConv(instance = true),
    val attributeType: TypeSpec,
    val returnType: Type = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
)
