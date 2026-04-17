package top.fifthlight.asmnet

// ECMA-335 II.25.2.3.2
@JvmInline
value class Subsystem(val value: Short) {
    companion object {
        const val WINDOWS_GUI: Short = 0x0002
        const val WINDOWS_CUI: Short = 0x0003
    }
}
