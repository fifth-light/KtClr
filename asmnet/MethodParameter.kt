package top.fifthlight.asmnet

data class MethodParameter(
    val type: TypeSpec,
    val name: String? = null,
    val flags: ParamAttributes = ParamAttributes(),
)
