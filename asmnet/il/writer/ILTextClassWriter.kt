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
                typeSpec(extends)
                line()
            }
            implements.forEach { iface ->
                +"implements "
                typeSpec(iface)
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
        parameters: List<Parameter>,
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

    // ECMA-335 II.16
    override fun visitField(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes,
        offset: Int?,
        initValue: FieldInitValue?,
    ) = writer.write {
        fieldDecl(name, type, attributes, offset, initValue)
    }

    // ECMA-335 II.17
    @Suppress("RedundantNullableReturnType")
    override fun visitProperty(
        name: String,
        type: TypeSpec,
        callConv: CallConv,
        attributes: PropertyAttributes,
        parameters: List<Parameter>,
    ): PropertyVisitor? {
        writer.write {
            +".property "
            propertyAttr(attributes)
            callConv(callConv)
            +' '
            type(type)
            +' '
            identifier(name)
            params(parameters)
            +' '
            +"{"
            indent()
            line()
        }
        return ILTextPropertyWriter(writer, name)
    }

    // ECMA-335 II.18
    @Suppress("RedundantNullableReturnType")
    override fun visitEvent(
        name: String,
        type: TypeSpec,
        attributes: EventAttributes,
    ): EventVisitor? {
        writer.write {
            +".event "
            eventAttr(attributes)
            type(type)
            +' '
            identifier(name)
            line()
            +"{"
            indent()
            line()
        }
        return ILTextEventWriter(writer, name)
    }

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
