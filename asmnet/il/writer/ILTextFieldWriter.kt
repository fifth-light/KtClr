/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.FieldVisitor

class ILTextFieldWriter internal constructor(
    private val writer: TextWriter,
) : FieldVisitor {
    override fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?) = writer.write {
        customAttributeRef(reference, blob)
    }

    override fun visitEnd() {}
}
