/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.FieldVisitor

@AsmDsl
class FieldDslScope(val visitor: FieldVisitor) {
    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)
}
