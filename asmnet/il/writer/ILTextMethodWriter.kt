package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*

class ILTextMethodWriter internal constructor(
    private val writer: TextWriter,
    private val className: String?,
    private val name: String,
    private val returnType: TypeSpec,
    private val callConv: CallConv,
    private val attributes: MethodAttributes,
    private val implAttributes: ImplementationAttributes,
    private val entryPoint: Boolean,
    private val parameters: List<MethodParameter>,
) : MethodVisitor {
    private val labels = mutableMapOf<Label, Int>()
    private val emittedLabels = mutableSetOf<Label>()

    private fun getOrCreateLabelIndex(label: Label): Int =
        labels.getOrPut(label) { labels.size }

    init {
        writer.write {
            +".method "
            methodAttr(attributes)
            callConv(callConv)
            +' '
            type(returnType)
            +' '
            identifier(name)
            params(parameters)
            +' '
            implAttr(implAttributes)
            line()
            +"{"
            indent()
            line()

            if (entryPoint) {
                +".entrypoint"
                line()
            }
        }
    }

    override fun visitMaxStack(maxStack: Int) {
        writer.write {
            +".maxstack "
            +"$maxStack"
            line()
        }
    }

    override fun visitCode() {}

    override fun visitLabel(label: Label) {
        require(label !in emittedLabels) { "Label $label already emitted" }
        val labelIndex = getOrCreateLabelIndex(label)
        emittedLabels.add(label)
        writer.write {
            +"LABEL_$labelIndex: "
        }
    }

    override fun visitInsn(opCode: OpCode) {
        writer.write {
            opcode(opCode)
            line()
        }
    }

    override fun visitLdc(value: Any) {
        writer.write {
            when (value) {
                is Int -> {
                    opcode(OpCode.Code.ldcI4)
                    +" "
                    hex(value)
                }
                is Long -> {
                    opcode(OpCode.Code.ldcI8)
                    +" "
                    hex(value)
                }
                is Float -> {
                    opcode(OpCode.Code.ldcR4)
                    +" $value"
                }
                is Double -> {
                    opcode(OpCode.Code.ldcR8)
                    +" $value"
                }
                is String -> {
                    opcode(OpCode.Code.ldstr)
                    quoted(value)
                }
                else -> throw IllegalArgumentException("Unsupported LDC value: $value")
            }
            line()
        }
    }

    override fun visitVarInsn(opcode: OpCode, varIndex: Int) {
        writer.write {
            opcode(opcode)
            +" $varIndex"
            line()
        }
    }

    override fun visitMethodInsn(opcode: OpCode, ref: MethodReference) {
        writer.write {
            opcode(opcode)
            +' '
            methodRef(ref)
            line()
        }
    }

    override fun visitFieldInsn(opcode: OpCode, ref: FieldReference) {
        writer.write {
            opcode(opcode)
            +' '
            fieldRef(ref)
            line()
        }
    }

    override fun visitJumpInsn(opcode: OpCode, label: Label) {
        val labelIndex = getOrCreateLabelIndex(label)
        writer.write {
            opcode(opcode)
            +' '
            +"LABEL_$labelIndex"
            line()
        }
    }

    override fun visitTypeInsn(opcode: OpCode, type: TypeSpec) {
        writer.write {
            opcode(opcode)
            +' '
            type(type)
            line()
        }
    }

    override fun visitSwitchInsn(labels: List<Label>) {
        writer.write {
            +"switch ("
            labels.forEachIndexed { index, label ->
                if (index > 0) {
                    +", "
                }
                +"LABEL_${getOrCreateLabelIndex(label)}"
            }
            +")"
            line()
        }
    }

    override fun visitLocalVariables(init: Boolean, locals: List<LocalVariable>) {
        writer.write {
            localsSignature(init, locals)
        }
    }

    override fun visitEnd() {
        writer.write {
            unindent()
            line()
            +"}"
            if (className != null) {
                +" // end of method $className::$name"
            }
            line()
        }
    }
}
