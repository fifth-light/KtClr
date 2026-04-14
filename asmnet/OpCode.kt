package top.fifthlight.asmnet

@JvmInline
value class OpCode(val value: Int) {
    companion object {
        val NOP = OpCode(0x00)
        val LDSTR = OpCode(0x72)
        val CALL = OpCode(0x28)
        val RET = OpCode(0x2A)
        val LDARG_0 = OpCode(0x02)
    }
}
