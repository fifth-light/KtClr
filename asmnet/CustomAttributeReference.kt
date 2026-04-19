package top.fifthlight.asmnet

// ECMA-335 II.21
data class CustomAttributeReference(
    val callConv: CallConv = CallConv(instance = true),
    val attributeType: TypeSpec,
    val returnType: Type = Type.Void,
    val parameterTypes: List<TypeSpec> = emptyList(),
)
