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
        require(label !in labels.keys) { "Label $label already exists" }
        val labelIndex = labels.size
        labels[label] = labelIndex
        writer.write {
            +"LABEL_$labelIndex: "
        }
    }

    override fun visitInsn(opCode: OpCode) {
        writer.write {
            +opCodeName(opCode)
            line()
        }
    }

    override fun visitLdc(value: Any) {
        writer.write {
            when (value) {
                is Int -> +"ldc.i4 $value"
                is Long -> +"ldc.i8 $value"
                is Float -> +"ldc.r4 $value"
                is Double -> +"ldc.r8 $value"
                is String -> {+"ldstr "; quoted(value)}
                else -> throw IllegalArgumentException("Unsupported LDC value: $value")
            }
            line()
        }
    }

    override fun visitVarInsn(opcode: OpCode, varIndex: Int) {
        writer.write {
            +opCodeName(opcode)
            +" $varIndex"
            line()
        }
    }

    override fun visitMethodInsn(opcode: OpCode, ref: MethodReference) {
        writer.write {
            +opCodeName(opcode)
            +' '
            methodRef(ref)
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

    private fun opCodeName(opCode: OpCode): String = when (opCode.value) {
        0x00 -> "nop"
        0x02 -> "ldarg.0"
        0x28 -> "call"
        0x2A -> "ret"
        0x72 -> "ldstr"
        else -> throw IllegalArgumentException("Unsupported opcode: $opCode")
    }
}
