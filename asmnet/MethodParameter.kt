package top.fifthlight.asmnet

data class MethodParameter(
    val type: TypeSpec,
    val name: String?,
    val flags: ParamAttributes,
)
