package top.fifthlight.asmnet

class MethodReference(
    val callConv: CallConv = CallConv(),
    val declaringType: TypeSpec,
    val name: String,
    val returnType: TypeSpec = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
) {
    override fun toString() = buildString {
        append('(')
        parameterTypes.forEach { append(it) }
        append(')')
        append(returnType)
    }
}