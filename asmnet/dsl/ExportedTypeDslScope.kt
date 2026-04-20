/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.ExportedTypeVisitor

@AsmDsl
class ExportedTypeDslScope(val visitor: ExportedTypeVisitor) {
    fun file(fileName: String) = visitor.visitFile(fileName)

    fun parentType(name: String) = visitor.visitParentType(name)

    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)
}
