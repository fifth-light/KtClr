package top.fifthlight.asmnet

// ECMA-335 II.6.2.1.4
data class Version(
    val major: Int,
    val minor: Int,
    val build: Int,
    val revision: Int,
) {
    override fun toString() = "$major:$minor:$build:$revision"
}
