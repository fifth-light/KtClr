package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*

class ILTextClassWriter internal constructor(
    private val writer: TextWriter,
    private val className: String,
) : ClassVisitor {
    override fun visit(attrs: TypeAttributes, extends: String?, implements: Set<String>) {
        writer.write {
            +".class "
            typeAttr(attrs)
            identifier(className)
            line()
            extends?.let {
                +"extends "
                +it
                line()
            }
            implements.forEach { iface ->
                +"implements "
                +iface
                line()
            }
            +"{"
            indent()
            line()
        }
    }

    override fun visitMethod(
        name: String,
        returnType: TypeSpec,
        callConv: CallConv,
        attributes: MethodAttributes,
        implAttributes: ImplementationAttributes,
        parameters: List<MethodParameter>,
    ): MethodVisitor = ILTextMethodWriter(
        writer = writer,
        className = className,
        name = name,
        returnType = returnType,
        callConv = callConv,
        attributes = attributes,
        implAttributes = implAttributes,
        entryPoint = false,
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
