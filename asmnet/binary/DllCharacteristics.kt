package top.fifthlight.asmnet.binary

// PE Format § Optional Header - DLL Characteristics
@JvmInline
value class DllCharacteristics(val value: UShort) {
    val isHighEntropyVa: Boolean get() = (value.toInt() and 0x0020) != 0
    val isDynamicBase: Boolean get() = (value.toInt() and 0x0040) != 0
    val isForceIntegrity: Boolean get() = (value.toInt() and 0x0080) != 0
    val isNxCompat: Boolean get() = (value.toInt() and 0x0100) != 0
    val isNoIsolation: Boolean get() = (value.toInt() and 0x0200) != 0
    val isNoSeh: Boolean get() = (value.toInt() and 0x0400) != 0
    val isNoBind: Boolean get() = (value.toInt() and 0x0800) != 0
    val isAppContainer: Boolean get() = (value.toInt() and 0x1000) != 0
    val isWdmDriver: Boolean get() = (value.toInt() and 0x2000) != 0
    val isGuardCf: Boolean get() = (value.toInt() and 0x4000) != 0
    val isTerminalServerAware: Boolean get() = (value.toInt() and 0x8000) != 0

    companion object {
        const val HIGH_ENTROPY_VA: UShort = 0x0020u
        const val DYNAMIC_BASE: UShort = 0x0040u
        const val FORCE_INTEGRITY: UShort = 0x0080u
        const val NX_COMPAT: UShort = 0x0100u
        const val NO_ISOLATION: UShort = 0x0200u
        const val NO_SEH: UShort = 0x0400u
        const val NO_BIND: UShort = 0x0800u
        const val APPCONTAINER: UShort = 0x1000u
        const val WDM_DRIVER: UShort = 0x2000u
        const val GUARD_CF: UShort = 0x4000u
        const val TERMINAL_SERVER_AWARE: UShort = 0x8000u
    }
}
