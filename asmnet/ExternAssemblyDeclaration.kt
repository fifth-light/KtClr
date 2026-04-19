/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.6.3
data class ExternAssemblyDeclaration(
    val culture: String? = null,
    val publicKeyToken: ByteArray? = null,
    val publicKey: ByteArray? = null,
    val version: Version? = null,
    val flags: AssemblyFlags = AssemblyFlags(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExternAssemblyDeclaration

        if (culture != other.culture) return false
        if (!publicKeyToken.contentEquals(other.publicKeyToken)) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (version != other.version) return false
        if (flags != other.flags) return false

        return true
    }

    override fun hashCode(): Int {
        var result = culture.hashCode()
        result = 31 * result + (publicKeyToken?.contentHashCode() ?: 0)
        result = 31 * result + (publicKey?.contentHashCode() ?: 0)
        result = 31 * result + version.hashCode()
        result = 31 * result + flags.hashCode()
        return result
    }
}
