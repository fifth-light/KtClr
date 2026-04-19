/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

interface PropertyVisitor {
    fun visitGet(ref: MethodReference)
    fun visitSet(ref: MethodReference)
    fun visitOther(ref: MethodReference)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
