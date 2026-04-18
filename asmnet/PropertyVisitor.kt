package top.fifthlight.asmnet

interface PropertyVisitor {
    fun visitGet(ref: MethodReference)
    fun visitSet(ref: MethodReference)
    fun visitOther(ref: MethodReference)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
