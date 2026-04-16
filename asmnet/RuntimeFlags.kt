package top.fifthlight.asmnet

// ECMA-335 II.25.3.3.1
@JvmInline
value class RuntimeFlags(val value: Int) {
    constructor(vararg flags: Int): this(flags.or())

    val ilOnly: Boolean
        get() = (value and ILONLY) != 0

    val _32BitRequired: Boolean
        get() = (value and _32BITREQUIRED) != 0

    val strongNameSigned: Boolean
        get() = (value and STRONGNAMESIGNED) != 0

    val nativeEntryPoint: Boolean
        get() = (value and NATIVE_ENTRYPOINT) != 0

    val trackDebugData: Boolean
        get() = (value and TRACKDEBUGDATA) != 0

    companion object {
        const val ILONLY = 0x00000001
        const val _32BITREQUIRED = 0x00000002
        const val STRONGNAMESIGNED = 0x00000008
        const val NATIVE_ENTRYPOINT = 0x00000010
        const val TRACKDEBUGDATA = 0x00010000
    }
}