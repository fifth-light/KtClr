package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.PropertyVisitor

class ILTextPropertyWriter internal constructor(
    private val writer: TextWriter,
    private val propertyName: String,
) : PropertyVisitor {
    override fun visitGet(ref: MethodReference) = writer.write {
        +".get "
        methodRef(ref)
        line()
    }

    override fun visitSet(ref: MethodReference) = writer.write {
        +".set "
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
            +" // end of property "
            +propertyName
            line()
        }
    }
}
