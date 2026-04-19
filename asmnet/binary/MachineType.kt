package top.fifthlight.asmnet.binary

// PE Format § COFF File Header - Machine Types
@JvmInline
value class MachineType(val value: UShort) {
    companion object {
        val UNKNOWN = MachineType(0x0000u)
        val I386 = MachineType(0x014Cu)
        val AMD64 = MachineType(0x8664u)
        val ARM = MachineType(0x01C0u)
        val ARM64 = MachineType(0xAA64u)
        val ARM64EC = MachineType(0xA641u)
        val ARM64X = MachineType(0xA64Eu)
        val ARMNT = MachineType(0x01C4u)
        val IA64 = MachineType(0x0200u)
        val LOONGARCH32 = MachineType(0x6232u)
        val LOONGARCH64 = MachineType(0x6264u)
        val RISCV32 = MachineType(0x5032u)
        val RISCV64 = MachineType(0x5064u)
        val RISCV128 = MachineType(0x5128u)
    }
}
