package top.fifthlight.asmnet

// ECMA-335 II.23.1.15
@JvmInline
value class TypeAttributes(val value: Int) {
    constructor(vararg flags: Int): this(flags.or())

    val visibility: Int
        get() = value and VisibilityMask

    val layout: Int
        get() = value and LayoutMask

    val classSemantics: Int
        get() = value and ClassSemanticsMask

    val abstract: Boolean
        get() = (value and Abstract) != 0
    val sealed: Boolean
        get() = (value and Sealed) != 0
    val specialName: Boolean
        get() = (value and SpecialName) != 0

    val import: Boolean
        get() = (value and Import) != 0
    val serializable: Boolean
        get() = (value and Serializable) != 0

    val stringFormatting: Int
        get() = value and StringFormatMask

    val beforeFieldInit: Boolean
        get() = (value and BeforeFieldInit) != 0
    val rtSpecialName: Boolean
        get() = (value and RTSpecialName) != 0
    val hasSecurity: Boolean
        get() = (value and HasSecurity) != 0
    val isTypeForwarder: Boolean
        get() = (value and IsTypeForwarder) != 0

    companion object {
        // Visibility attributes
        const val VisibilityMask = 0x00000007
        const val NotPublic = 0x00000000
        const val Public = 0x00000001
        const val NestedPublic = 0x00000002
        const val NestedPrivate = 0x00000003
        const val NestedFamily = 0x00000004
        const val NestedAssembly = 0x00000005
        const val NestedFamANDAssem = 0x00000006
        const val NestedFamORAssem = 0x00000007

        // Class layout attributes
        const val LayoutMask = 0x00000018
        const val AutoLayout = 0x00000000
        const val SequentialLayout = 0x00000008
        const val ExplicitLayout = 0x00000010

        // Class semantics attributes
        const val ClassSemanticsMask = 0x00000020
        const val Class = 0x00000000
        const val Interface = 0x00000020

        // Special semantics in addition to class semantics
        const val Abstract = 0x00000080
        const val Sealed = 0x00000100
        const val SpecialName = 0x00000400

        // Implementation Attributes
        const val Import = 0x00001000
        const val Serializable = 0x00002000

        // String formatting Attributes
        const val StringFormatMask = 0x00030000
        const val AnsiClass = 0x00000000
        const val UnicodeClass = 0x00010000
        const val AutoClass = 0x00020000
        const val CustomFormatClass = 0x00030000
        const val CustomStringFormatMask = 0x00C00000

        // Class Initialization Attributes
        const val BeforeFieldInit = 0x00100000

        // Additional Flags
        const val RTSpecialName = 0x00000800
        const val HasSecurity = 0x00040000
        const val IsTypeForwarder = 0x00200000
    }
}