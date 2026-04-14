package top.fifthlight.asmnet

// ECMA-335 II.6.2.1
data class AssemblyDeclaration(
    val hash: HashAlgorithm? = null,
    val culture: String? = null,
    val publicKey: ByteArray? = null,
    val version: Version? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AssemblyDeclaration

        if (hash != other.hash) return false
        if (culture != other.culture) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hash?.hashCode() ?: 0
        result = 31 * result + (culture?.hashCode() ?: 0)
        result = 31 * result + (publicKey?.contentHashCode() ?: 0)
        result = 31 * result + version.hashCode()
        return result
    }
}
