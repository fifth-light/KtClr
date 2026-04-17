package top.fifthlight.asmnet

// ECMA-335 II.23.1.4
@JvmInline
value class EventAttributes(val value: Short) {
    constructor(vararg flags: Short) : this(flags.or())

    val specialName: Boolean
        get() = (value and SpecialName) != 0.toShort()
    val rtSpecialName: Boolean
        get() = (value and RTSpecialName) != 0.toShort()

    companion object {
        const val SpecialName: Short = 0x0200
        const val RTSpecialName: Short = 0x0400
    }
}
