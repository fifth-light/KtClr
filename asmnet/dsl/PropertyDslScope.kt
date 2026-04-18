package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.PropertyVisitor

@AsmDsl
class PropertyDslScope(val visitor: PropertyVisitor) {
    fun get(ref: MethodReference) = visitor.visitGet(ref)
    fun set(ref: MethodReference) = visitor.visitSet(ref)
    fun other(ref: MethodReference) = visitor.visitOther(ref)
    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)
}
