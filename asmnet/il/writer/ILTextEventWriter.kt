/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.EventVisitor
import top.fifthlight.asmnet.MethodReference

class ILTextEventWriter internal constructor(
    private val writer: TextWriter,
    private val eventName: String,
) : EventVisitor {
    override fun visitAddOn(ref: MethodReference) = writer.write {
        +".addon "
        methodRef(ref)
        line()
    }

    override fun visitRemoveOn(ref: MethodReference) = writer.write {
        +".removeon "
        methodRef(ref)
        line()
    }

    override fun visitFire(ref: MethodReference) = writer.write {
        +".fire "
        methodRef(ref)
        line()
    }

    override fun visitOther(ref: MethodReference) = writer.write {
        +".other "
        methodRef(ref)
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
            +" // end of event "
            +eventName
            line()
        }
    }
}
