/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.2.2 MethodRefSig
data class MethodCallReference(
    val callConv: CallConv = CallConv(),
    val declaringType: TypeSpec,
    val name: String,
    val returnType: TypeSpec = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
    val sentinelIndex: Int? = null,
) {
    constructor(ref: MethodReference, sentinelIndex: Int? = null) : this(
        callConv = ref.callConv,
        declaringType = ref.declaringType,
        name = ref.name,
        returnType = ref.returnType,
        parameterTypes = ref.parameterTypes,
        sentinelIndex = sentinelIndex,
    )

    override fun toString() = buildString {
        append('(')
        parameterTypes.forEach { append(it) }
        append(')')
        append(returnType)
    }
}
