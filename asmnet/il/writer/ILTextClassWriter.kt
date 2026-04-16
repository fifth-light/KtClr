package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*

class ILTextClassWriter internal constructor(
    private val writer: TextWriter,
    private val className: String,
) : ClassVisitor {
    override fun visit(
        attrs: TypeAttributes,
        extends: TypeSpec?,
        implements: Set<TypeSpec>,
    ) {
        writer.write {
            +".class "
            typeAttr(attrs)
            identifier(className)
            line()
            extends?.let {
                +"extends "
                type(extends)
                line()
            }
            implements.forEach { iface ->
                +"implements "
                type(iface)
                line()
            }
            +"{"
            indent()
            line()
        }
    }

    @Suppress("RedundantNullableReturnType")
    override fun visitMethod(
        name: String,
        returnType: TypeSpec,
        callConv: CallConv,
        attributes: MethodAttributes,
        implAttributes: ImplementationAttributes,
        entryPoint: Boolean,
        parameters: List<MethodParameter>,
    ): MethodVisitor? = ILTextMethodWriter(
        writer = writer,
        className = className,
        name = name,
        returnType = returnType,
        callConv = callConv,
        attributes = attributes,
        implAttributes = implAttributes,
        entryPoint = entryPoint,
        parameters = parameters,
    )

    override fun visitEnd() {
        writer.write {
            unindent()
            line()
            +"}"
            +" // end of class "
            +className
            line()
        }
    }
}
