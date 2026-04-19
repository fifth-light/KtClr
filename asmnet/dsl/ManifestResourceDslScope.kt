/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.ManifestResourceVisitor

@AsmDsl
class ManifestResourceDslScope(val visitor: ManifestResourceVisitor) {
    fun file(fileName: String, offset: Int) = visitor.visitFile(fileName, offset)

    fun externAssembly(name: String) = visitor.visitExternAssembly(name)

    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)
}
