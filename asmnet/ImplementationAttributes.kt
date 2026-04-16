package top.fifthlight.asmnet

// ECMA-335 II.23.1.11
@JvmInline
value class ImplementationAttributes(val value: Short) {
    constructor(vararg flags: Short): this(flags.or())

    val codeType: Short
        get() = value and CodeTypeMask

    val managed: Short
        get() = value and ManagedMask

    val forwardRef: Boolean
        get() = (value and ForwardRef) != 0.toShort()

    val preserveSig: Boolean
        get() = (value and PreserveSig) != 0.toShort()

    val internalCall: Boolean
        get() = (value and InternalCall) != 0.toShort()

    val synchronized: Boolean
        get() = (value and Synchronized) != 0.toShort()

    val noInlining: Boolean
        get() = (value and NoInlining) != 0.toShort()

    val noOptimization: Boolean
        get() = (value and NoOptimization) != 0.toShort()

    companion object {
        const val CodeTypeMask: Short = 0x0003
        const val IL: Short = 0x0000
        const val Native: Short = 0x0001
        const val OPTIL: Short = 0x0002
        const val Runtime: Short = 0x0003

        const val ManagedMask: Short = 0x0004
        const val Unmanaged: Short = 0x0004
        const val Managed: Short = 0x0000

        // Implementation info and interop
        const val ForwardRef: Short = 0x0010
        const val PreserveSig: Short = 0x0080
        const val InternalCall: Short = 0x1000
        const val Synchronized: Short = 0x0020
        const val NoInlining: Short = 0x0008
        const val MaxMethodImplVal: Short = 0xffff.toShort()
        const val NoOptimization: Short = 0x0040
    }
}