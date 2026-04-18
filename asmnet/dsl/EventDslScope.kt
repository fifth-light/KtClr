package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.CustomAttributeReference
import top.fifthlight.asmnet.EventVisitor
import top.fifthlight.asmnet.MethodReference

@AsmDsl
class EventDslScope(val visitor: EventVisitor) {
    fun addOn(ref: MethodReference) = visitor.visitAddOn(ref)
    fun removeOn(ref: MethodReference) = visitor.visitRemoveOn(ref)
    fun fire(ref: MethodReference) = visitor.visitFire(ref)
    fun other(ref: MethodReference) = visitor.visitOther(ref)
    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)
}
