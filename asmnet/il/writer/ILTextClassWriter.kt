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

    override fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?) = writer.write {
        customAttributeRef(reference, blob)
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
    @Suppress("RedundantNullableReturnType")
    override fun visitField(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes,
        offset: Int?,
        initValue: FieldInitValue?,
    ): FieldVisitor? {
        writer.write {
            fieldDecl(name, type, attributes, offset, initValue)
        }
        return ILTextFieldWriter(writer)
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
            line()
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

    // ECMA-335 II.10.7
    override fun visitPack(packSize: Int) = writer.write {
        +".pack "
        +"$packSize"
        line()
    }

    // ECMA-335 II.10.7
    override fun visitSize(classSize: Int) = writer.write {
        +".size "
        +"$classSize"
        line()
    }

    // ECMA-335 II.10.3.2
    override fun visitOverride(baseType: TypeSpec, baseName: String, implementation: MethodReference) = writer.write {
        +".override "
        typeSpec(baseType)
        +"::"
        identifier(baseName)
        +" with "
        methodRef(implementation)
        line()
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
