package top.fifthlight.asmnet

data class Parameter(
    val type: TypeSpec,
    val name: String? = null,
    val flags: ParamAttributes = ParamAttributes(),
    val marshal: NativeType? = null,
)
