/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.18
interface EventVisitor {
    fun visitAddOn(ref: MethodReference)
    fun visitRemoveOn(ref: MethodReference)
    fun visitFire(ref: MethodReference)
    fun visitOther(ref: MethodReference)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
