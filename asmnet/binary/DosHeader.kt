package top.fifthlight.asmnet.binary

// ECMA-335 II.25.2.1, PE Format § MS-DOS Stub
data class DosHeader(
    val e_magic: UShort,
    val e_cblp: UShort,
    val e_cp: UShort,
    val e_crlc: UShort,
    val e_cparhdr: UShort,
    val e_minalloc: UShort,
    val e_maxalloc: UShort,
    val e_ss: UShort,
    val e_sp: UShort,
    val e_csum: UShort,
    val e_ip: UShort,
    val e_cs: UShort,
    val e_lfarlc: UShort,
    val e_ovno: UShort,
    val e_res: UShortArray,
    val e_oemid: UShort,
    val e_oeminfo: UShort,
    val e_res2: UShortArray,
    val e_lfanew: UInt,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DosHeader

        if (e_magic != other.e_magic) return false
        if (e_cblp != other.e_cblp) return false
        if (e_cp != other.e_cp) return false
        if (e_crlc != other.e_crlc) return false
        if (e_cparhdr != other.e_cparhdr) return false
        if (e_minalloc != other.e_minalloc) return false
        if (e_maxalloc != other.e_maxalloc) return false
        if (e_ss != other.e_ss) return false
        if (e_sp != other.e_sp) return false
        if (e_csum != other.e_csum) return false
        if (e_ip != other.e_ip) return false
        if (e_cs != other.e_cs) return false
        if (e_lfarlc != other.e_lfarlc) return false
        if (e_ovno != other.e_ovno) return false
        if (e_oemid != other.e_oemid) return false
        if (e_oeminfo != other.e_oeminfo) return false
        if (e_lfanew != other.e_lfanew) return false
        if (!e_res.contentEquals(other.e_res)) return false
        if (!e_res2.contentEquals(other.e_res2)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = e_magic.hashCode()
        result = 31 * result + e_cblp.hashCode()
        result = 31 * result + e_cp.hashCode()
        result = 31 * result + e_crlc.hashCode()
        result = 31 * result + e_cparhdr.hashCode()
        result = 31 * result + e_minalloc.hashCode()
        result = 31 * result + e_maxalloc.hashCode()
        result = 31 * result + e_ss.hashCode()
        result = 31 * result + e_sp.hashCode()
        result = 31 * result + e_csum.hashCode()
        result = 31 * result + e_ip.hashCode()
        result = 31 * result + e_cs.hashCode()
        result = 31 * result + e_lfarlc.hashCode()
        result = 31 * result + e_ovno.hashCode()
        result = 31 * result + e_oemid.hashCode()
        result = 31 * result + e_oeminfo.hashCode()
        result = 31 * result + e_lfanew.hashCode()
        result = 31 * result + e_res.contentHashCode()
        result = 31 * result + e_res2.contentHashCode()
        return result
    }

    companion object {
        const val MAGIC_MZ: UShort = 0x5A4Du
        const val SIZE = 64
    }
}
