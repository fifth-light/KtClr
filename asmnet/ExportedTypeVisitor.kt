/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.6.7
interface ExportedTypeVisitor {
    fun visitFile(fileName: String)

    fun visitParentType(name: String)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
