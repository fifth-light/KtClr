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

    fun insn(code: OpCode.Code, label: Label, vararg prefixes: Short) =
        visitor.visitJumpInsn(OpCode(code, *prefixes), label)

    fun insn(code: OpCode.Code, type: TypeSpec, vararg prefixes: Short) =
        visitor.visitTypeInsn(OpCode(code, *prefixes), type)

    fun switch(labels: List<Label>) = visitor.visitSwitchInsn(labels)

    fun ldc(value: Any) = visitor.visitLdc(value)

    fun override(baseType: TypeSpec, baseName: String) =
        visitor.visitOverride(baseType, baseName)

    fun locals(init: Boolean = true, vararg locals: LocalVariable) =
        visitor.visitLocalVariables(init, locals.toList())

    fun locals(vararg locals: LocalVariable) = locals(init = true, *locals)

    fun tryCatch(
        exceptionType: TypeSpec,
        tryBlock: MethodDslScope.() -> Unit,
        catchBlock: MethodDslScope.() -> Unit,
    ) {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        label(tryStart)
        tryBlock()
        label(handlerStart)
        catchBlock()
        label(handlerEnd)
        visitor.visitExceptionHandler(
            flags = ExceptionFlag.Exception,
            tryStart = tryStart,
            tryEnd = handlerStart,
            exceptionType = exceptionType,
            handlerStart = handlerStart,
            handlerEnd = handlerEnd,
        )
    }

    fun tryFinally(
        tryBlock: MethodDslScope.() -> Unit,
        finallyBlock: MethodDslScope.() -> Unit,
    ) {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        label(tryStart)
        tryBlock()
        label(handlerStart)
        finallyBlock()
        label(handlerEnd)
        visitor.visitExceptionHandler(
            flags = ExceptionFlag.Finally,
            tryStart = tryStart,
            tryEnd = handlerStart,
            handlerStart = handlerStart,
            handlerEnd = handlerEnd,
        )
    }

    fun tryFault(
        tryBlock: MethodDslScope.() -> Unit,
        faultBlock: MethodDslScope.() -> Unit,
    ) {
        val tryStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        label(tryStart)
        tryBlock()
        label(handlerStart)
        faultBlock()
        label(handlerEnd)
        visitor.visitExceptionHandler(
            flags = ExceptionFlag.Fault,
            tryStart = tryStart,
            tryEnd = handlerStart,
            handlerStart = handlerStart,
            handlerEnd = handlerEnd,
        )
    }

    fun tryFilter(
        tryBlock: MethodDslScope.() -> Unit,
        filterBlock: MethodDslScope.() -> Unit,
        handlerBlock: MethodDslScope.() -> Unit,
    ) {
        val tryStart = Label()
        val filterStart = Label()
        val handlerStart = Label()
        val handlerEnd = Label()
        label(tryStart)
        tryBlock()
        label(filterStart)
        filterBlock()
        label(handlerStart)
        handlerBlock()
        label(handlerEnd)
        visitor.visitExceptionHandler(
            flags = ExceptionFlag.Filter,
            tryStart = tryStart,
            tryEnd = filterStart,
            filterStart = filterStart,
            handlerStart = handlerStart,
            handlerEnd = handlerEnd,
        )
    }

    fun exceptionHandler(
        flags: ExceptionFlag,
        tryStart: Label,
        tryEnd: Label,
        handlerStart: Label,
        handlerEnd: Label,
        exceptionType: TypeSpec? = null,
        filterStart: Label? = null,
    ) = visitor.visitExceptionHandler(
        flags = flags,
        tryStart = tryStart,
        tryEnd = tryEnd,
        handlerStart = handlerStart,
        handlerEnd = handlerEnd,
        exceptionType = exceptionType,
        filterStart = filterStart,
    )
}
