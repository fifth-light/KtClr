/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

data class MethodReference(
    val callConv: CallConv = CallConv(),
    val declaringType: TypeSpec,
    val name: String,
    val returnType: TypeSpec = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
) {
    override fun toString() = buildString {
        append('(')
        parameterTypes.forEach { append(it) }
        append(')')
        append(returnType)
    }
}