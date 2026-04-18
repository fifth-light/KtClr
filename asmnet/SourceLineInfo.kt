package top.fifthlight.asmnet

data class SourceLineInfo(
    val line: Int,
    val column: Int? = null,
    val filename: String? = null,
)
