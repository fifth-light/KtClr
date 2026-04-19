/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.ManifestResourceVisitor

class ILTextManifestResourceWriter internal constructor(
    private val writer: TextWriter,
    private val resourceName: String,
) : ManifestResourceVisitor {
    override fun visitFile(fileName: String, offset: Int) = writer.write {
        +".file "
        identifier(fileName)
        +" at "
        hex(offset)
        line()
    }

    override fun visitExternAssembly(name: String) = writer.write {
        +".assembly extern "
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
            +" // end of mresource "
            +resourceName
            line()
        }
    }
}
