package top.fifthlight.asmnet

// ECMA-335 II.23.1.5
@JvmInline
value class FieldAttributes(val value: Short) {
    constructor(vararg flags: Short) : this(flags.or())

    val fieldAccess: Short
        get() = value and FieldAccessMask

    val static: Boolean
        get() = (value and Static) != 0.toShort()
    val initOnly: Boolean
        get() = (value and InitOnly) != 0.toShort()
    val literal: Boolean
        get() = (value and Literal) != 0.toShort()
    val notSerialized: Boolean
        get() = (value and NotSerialized) != 0.toShort()
    val specialName: Boolean
        get() = (value and SpecialName) != 0.toShort()

    val pInvokeImpl: Boolean
        get() = (value and PInvokeImpl) != 0.toShort()

    val rtSpecialName: Boolean
        get() = (value and RTSpecialName) != 0.toShort()
    val hasFieldMarshal: Boolean
        get() = (value and HasFieldMarshal) != 0.toShort()
    val hasDefault: Boolean
        get() = (value and HasDefault) != 0.toShort()
    val hasFieldRVA: Boolean
        get() = (value and HasFieldRVA) != 0.toShort()

    companion object {
        const val FieldAccessMask: Short = 0x0007
        const val CompilerControlled: Short = 0x0000
        const val Private: Short = 0x0001
        const val FamANDAssem: Short = 0x0002
        const val Assembly: Short = 0x0003
        const val Family: Short = 0x0004
        const val FamORAssem: Short = 0x0005
        const val Public: Short = 0x0006

        const val Static: Short = 0x0010
        const val InitOnly: Short = 0x0020
        const val Literal: Short = 0x0040
        const val NotSerialized: Short = 0x0080
        const val SpecialName: Short = 0x0200

        const val PInvokeImpl: Short = 0x2000

        const val RTSpecialName: Short = 0x0400
        const val HasFieldMarshal: Short = 0x1000
        const val HasDefault: Short = 0x8000.toShort()
        const val HasFieldRVA: Short = 0x0100
    }
}
