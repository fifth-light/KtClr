package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*

@AsmDsl
class MethodDslScope(val visitor: MethodVisitor) {
    fun maxStack(maxStack: Int) = visitor.visitMaxStack(maxStack)

    fun code() = visitor.visitCode()

    fun label(): Label = Label().also { visitor.visitLabel(it) }

    fun label(label: Label) = visitor.visitLabel(label)

    fun insn(code: OpCode.Code, vararg prefixes: Short) =
        visitor.visitInsn(OpCode(code, *prefixes))

    fun insn(code: OpCode.Code, ref: MethodReference, vararg prefixes: Short) =
        visitor.visitMethodInsn(OpCode(code, *prefixes), ref)

    fun insn(code: OpCode.Code, ref: FieldReference, vararg prefixes: Short) =
        visitor.visitFieldInsn(OpCode(code, *prefixes), ref)

    fun insn(code: OpCode.Code, varIndex: Int, vararg prefixes: Short) =
        visitor.visitVarInsn(OpCode(code, *prefixes), varIndex)

    fun ldc(value: Any) = visitor.visitLdc(value)

    fun locals(init: Boolean = true, vararg locals: LocalVariable) =
        visitor.visitLocalVariables(init, locals.toList())

    fun locals(vararg locals: LocalVariable) = locals(init = true, *locals)
}
