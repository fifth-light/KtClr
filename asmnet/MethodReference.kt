package top.fifthlight.asmnet

class MethodReference(
    val callConv: CallConv,
    val declaringType: TypeSpec,
    val name: String,
    val returnType: TypeSpec,
    val parameterTypes: List<TypeSpec>,
) {
    override fun toString() = buildString {
        append('(')
        parameterTypes.forEach { append(it) }
        append(')')
        append(returnType)
    }
}