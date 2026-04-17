package top.fifthlight.asmnet

interface MethodVisitor {
    // ECMA-335 II.15.4.1
    fun visitMaxStack(maxStack: Int)

    // ECMA-335 II.15.4.1.3
    fun visitLocalVariables(init: Boolean, locals: List<LocalVariable>)

    fun visitCode()

    // ECMA-335 II.5.4
    fun visitLabel(label: Label)

    // ECMA-335 VI.C.4.2
    fun visitInsn(opCode: OpCode)

    // ECMA-335 VI.C.4.4-6, VI.C.4.11
    fun visitLdc(value: Any)

    // ECMA-335 VI.C.4.3
    fun visitVarInsn(opcode: OpCode, varIndex: Int)

    // ECMA-335 VI.C.4.8
    fun visitMethodInsn(opcode: OpCode, ref: MethodReference)

    // ECMA-335 VI.C.4.9
    fun visitFieldInsn(opcode: OpCode, ref: FieldReference)

    // ECMA-335 VI.C.4.7
    fun visitJumpInsn(opcode: OpCode, label: Label)

    // ECMA-335 VI.C.4.10
    fun visitTypeInsn(opcode: OpCode, type: TypeSpec)

    // ECMA-335 VI.C.4.14
    fun visitSwitchInsn(labels: List<Label>)

    // ECMA-335 II.10.3.2
    fun visitOverride(baseType: TypeSpec, baseName: String)

    // ECMA-335 II.19
    fun visitExceptionHandler(
        flags: ExceptionFlag,
        tryStart: Label,
        tryEnd: Label,
        handlerStart: Label,
        handlerEnd: Label,
        exceptionType: TypeSpec? = null,
        filterStart: Label? = null,
    )

    fun visitEnd()
}
