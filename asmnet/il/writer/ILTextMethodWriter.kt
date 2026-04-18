package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*

class ILTextMethodWriter internal constructor(
    private val writer: TextWriter,
    private val className: String?,
    private val name: String,
    private val returnType: TypeSpec,
    private val callConv: CallConv,
    private val attributes: List<MethodAttribute>,
    private val implAttributes: ImplementationAttributes,
    private val entryPoint: Boolean,
    private val parameters: List<Parameter>,
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

    // ECMA-335 II.21
    override fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?) {
        writer.write {
            customAttributeRef(reference, blob)
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

    // ECMA-335 II.15.4.1.1
    override fun visitEmitByte(value: UByte) = writer.write {
        +".emitbyte "
        hex(value)
        line()
    }

    // ECMA-335 II.5.7
    override fun visitLine(sourceLine: SourceLineInfo) = writer.write {
        lineDirective(sourceLine)
    }

    // ECMA-335 II.15.4.1.4
    override fun visitParam(index: Int, defaultValue: FieldInitValue) = writer.write {
        +".param [$index] = "
        fieldInitValue(defaultValue)
        line()
    }

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
                    +' '
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
            typeSpec(type)
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

    // ECMA-335 II.10.3.2
    override fun visitOverride(baseType: TypeSpec, baseName: String) = writer.write {
        +".override "
        typeSpec(baseType)
        +"::"
        identifier(baseName)
        line()
    }

    override fun visitExceptionHandler(
        flags: ExceptionFlag,
        tryStart: Label,
        tryEnd: Label,
        handlerStart: Label,
        handlerEnd: Label,
        exceptionType: TypeSpec?,
        filterStart: Label?
    ) {
        writer.write {
            +".try "
            +"LABEL_${getOrCreateLabelIndex(tryStart)} to LABEL_${getOrCreateLabelIndex(tryEnd)} "
            when (flags) {
                ExceptionFlag.Exception -> {
                    +"catch "
                    typeSpec(exceptionType!!)
                    +' '
                }
                ExceptionFlag.Filter -> {
                    +"filter LABEL_${getOrCreateLabelIndex(filterStart!!)} "
                }
                ExceptionFlag.Finally -> {
                    +"finally "
                }
                ExceptionFlag.Fault -> {
                    +"fault "
                }
                else -> error("Unknown exception flag: $flags")
            }
            +"handler LABEL_${getOrCreateLabelIndex(handlerStart)} to LABEL_${getOrCreateLabelIndex(handlerEnd)}"
            line()
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
