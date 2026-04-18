package top.fifthlight.asmnet

// ECMA-335 II.21
data class CustomAttributeReference(
    val callConv: CallConv = CallConv(instance = true),
    val returnType: TypeSpec = Type.Void,
    val attributeType: TypeSpec,
    val parameterTypes: List<TypeSpec> = emptyList(),
)
