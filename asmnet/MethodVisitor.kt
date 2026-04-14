package top.fifthlight.asmnet

interface MethodVisitor {
    fun visitMaxStack(maxStack: Int)
    fun visitCode()

    fun visitLabel(label: Label)

    fun visitInsn(opCode: OpCode)
    fun visitLdc(value: Any)
    fun visitVarInsn(opcode: OpCode, varIndex: Int)
    fun visitMethodInsn(opcode: OpCode, ref: MethodReference)

    fun visitEnd()
}
