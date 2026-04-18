package top.fifthlight.asmnet

sealed interface MethodAttribute {
    val value: Short

    @ConsistentCopyVisibility
    data class Normal internal constructor(override val value: Short) : MethodAttribute

    // ECMA-335 II.15.5.2
    data class PInvokeImpl(
        val moduleName: String,
        val methodName: String? = null,
        val attributes: PInvokeAttributes = PInvokeAttributes(),
    ) : MethodAttribute {
        override val value: Short = 0x2000
    }

    companion object {
        // ECMA-335 II.23.1.10
        // MemberAccess (mask 0x0007)
        val CompilerControlled = Normal(0x0000)
        val Private = Normal(0x0001)
        val FamANDAssem = Normal(0x0002)
        val Assem = Normal(0x0003)
        val Family = Normal(0x0004)
        val FamORAssem = Normal(0x0005)
        val Public = Normal(0x0006)

        val UnmanagedExport = Normal(0x0008)
        val Static = Normal(0x0010)
        val Final = Normal(0x0020)
        val Virtual = Normal(0x0040)
        val HideBySig = Normal(0x0080)

        // VtableLayout (mask 0x0100)
        val ReuseSlot = Normal(0x0000)
        val NewSlot = Normal(0x0100)

        val Strict = Normal(0x0200)
        val Abstract = Normal(0x0400)
        val SpecialName = Normal(0x0800)
        val RTSpecialName = Normal(0x1000)
        val HasSecurity = Normal(0x4000)
        val RequireSecObject = Normal(0x8000.toShort())
    }
}

@JvmInline
value class PInvokeAttributes(val value: Short) {
    constructor(vararg flags: Short) : this(flags.or())

    val noMangle: Boolean
        get() = (value and NoMangle) != 0.toShort()

    val charSet: Short
        get() = value and CharSetMask

    val callConv: Short
        get() = value and CallConvMask

    val supportsLastError: Boolean
        get() = (value and SupportsLastError) != 0.toShort()

    companion object {
        // ECMA-335 II.23.1.8
        const val NoMangle: Short = 0x0001

        const val CharSetMask: Short = 0x0006
        const val CharSetNotSpec: Short = 0x0000
        const val CharSetAnsi: Short = 0x0002
        const val CharSetUnicode: Short = 0x0004
        const val CharSetAuto: Short = 0x0006

        const val SupportsLastError: Short = 0x0040

        const val CallConvMask: Short = 0x0700
        const val CallConvPlatformApi: Short = 0x0100
        const val CallConvCdecl: Short = 0x0200
        const val CallConvStdCall: Short = 0x0300
        const val CallConvThisCall: Short = 0x0400
        const val CallConvFastCall: Short = 0x0500
    }
}
