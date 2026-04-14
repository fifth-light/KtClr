package top.fifthlight.asmnet

// ECMA-335 II.23.1.10
@JvmInline
value class MethodAttributes(val value: Short) {
    val memberAccess: Short
        get() = value and MemberAccessMask

    val static: Boolean
        get() = (value and Static) != 0.toShort()
    val final: Boolean
        get() = (value and Final) != 0.toShort()
    val virtual: Boolean
        get() = (value and Virtual) != 0.toShort()
    val hideBySig: Boolean
        get() = (value and HideBySig) != 0.toShort()

    val vtableLayout: Short
        get() = value and VtableLayoutMask

    val strict: Boolean
        get() = (value and Strict) != 0.toShort()
    val abstract: Boolean
        get() = (value and Abstract) != 0.toShort()
    val specialName: Boolean
        get() = (value and SpecialName) != 0.toShort()

    val pInvokeImpl: Boolean
        get() = (value and PInvokeImpl) != 0.toShort()
    val unmanagedExport: Boolean
        get() = (value and UnmanagedExport) != 0.toShort()
    val rTSpecialName: Boolean
        get() = (value and RTSpecialName) != 0.toShort()
    val hasSecurity: Boolean
        get() = (value and HasSecurity) != 0.toShort()
    val requireSecObject: Boolean
        get() = (value and RequireSecObject) != 0.toShort()

    companion object {
        const val MemberAccessMask: Short = 0x0007
        const val CompilerControlled: Short = 0x0000
        const val Private: Short = 0x0001
        const val FamANDAssem: Short = 0x0002
        const val Assem: Short = 0x0003
        const val Family: Short = 0x0004
        const val FamORAssem: Short = 0x0005
        const val Public: Short = 0x0006

        const val Static: Short = 0x0010
        const val Final: Short = 0x0020
        const val Virtual: Short = 0x0040
        const val HideBySig: Short = 0x0080

        const val VtableLayoutMask: Short = 0x0100
        const val ReuseSlot: Short = 0x00000
        const val NewSlot: Short = 0x0100

        const val Strict: Short = 0x0200
        const val Abstract: Short = 0x0400
        const val SpecialName: Short = 0x0800

        // Interop attributes
        const val PInvokeImpl: Short = 0x2000
        const val UnmanagedExport: Short = 0x0008
        const val RTSpecialName: Short = 0x1000
        const val HasSecurity: Short = 0x4000
        const val RequireSecObject: Short = 0.toShort()
    }
}