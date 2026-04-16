package top.fifthlight.asmnet

// ECMA-335 II.23.1.13, but only contains II.15.4's ParamAttr
@JvmInline
value class ParamAttributes(val value: Short) {
    constructor(vararg flags: Short): this(flags.or())

    val `in`: Boolean
        get() = (value and In) != 0.toShort()

    val out: Boolean
        get() = (value and Out) != 0.toShort()

    val optional: Boolean
        get() = (value and Optional) != 0.toShort()

    companion object {
        const val In: Short = 0x0001
        const val Out: Short = 0x0002
        const val Optional: Short = 0x0010
    }
}