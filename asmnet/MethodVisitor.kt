package top.fifthlight.asmnet

interface MethodVisitor {
    fun visitMaxStack(maxStack: Int)
    fun visitLocalVariables(init: Boolean, locals: List<LocalVariable>)

    fun visitCode()

    fun visitLabel(label: Label)

    fun visitInsn(opCode: OpCode)
    fun visitLdc(value: Any)
    fun visitVarInsn(opcode: OpCode, varIndex: Int)
    fun visitMethodInsn(opcode: OpCode, ref: MethodReference)
    fun visitFieldInsn(opcode: OpCode, ref: FieldReference)
    fun visitJumpInsn(opcode: OpCode, label: Label)
    fun visitTypeInsn(opcode: OpCode, type: TypeSpec)
    fun visitEnd()
}
