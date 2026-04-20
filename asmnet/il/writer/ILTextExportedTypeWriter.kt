/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.ExportedTypeVisitor

class ILTextExportedTypeWriter internal constructor(
    private val writer: TextWriter,
    private val typeName: String,
) : ExportedTypeVisitor {
    override fun visitFile(fileName: String) = writer.write {
        +".file "
        identifier(fileName)
        line()
    }

    override fun visitParentType(name: String) = writer.write {
        +".class extern "
        identifier(name)
        line()
    }

    override fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?) = writer.write {
        customAttributeRef(reference, blob)
    }

    override fun visitEnd() {
        writer.write {
            unindent()
            line()
            +"}"
            +" // end of class extern "
            +typeName
            line()
        }
    }
}
