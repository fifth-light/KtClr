/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.6.8
interface TypeForwarderVisitor {
    fun visitExternAssembly(name: String)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
