package top.fifthlight.asmnet

interface PropertyVisitor {
    fun visitGet(ref: MethodReference)
    fun visitSet(ref: MethodReference)
    fun visitOther(ref: MethodReference)
    fun visitEnd()
}
