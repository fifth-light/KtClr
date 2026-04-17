package top.fifthlight.asmnet

// ECMA-335 II.18
interface EventVisitor {
    fun visitAddOn(ref: MethodReference)
    fun visitRemoveOn(ref: MethodReference)
    fun visitFire(ref: MethodReference)
    fun visitOther(ref: MethodReference)
    fun visitEnd()
}
