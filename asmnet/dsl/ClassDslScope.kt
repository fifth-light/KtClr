package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*

@AsmDsl
class ClassDslScope(val visitor: ClassVisitor) {
    fun method(
        name: String,
        returnType: TypeSpec = Type.Void,
        callConv: CallConv = CallConv(),
        attributes: MethodAttributes = MethodAttributes(
            MethodAttributes.Public,
        ),
        implAttributes: ImplementationAttributes = ImplementationAttributes(
            ImplementationAttributes.IL,
            ImplementationAttributes.Managed,
        ),
        entryPoint: Boolean = false,
        parameters: List<Parameter> = emptyList(),
        block: MethodDslScope.() -> Unit,
    ) {
        visitor.visitMethod(
            name, returnType, callConv, attributes, implAttributes, entryPoint, parameters,
        )?.let { mv ->
            MethodDslScope(mv).block()
            mv.visitEnd()
        }
    }

    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)

    fun field(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
        block: FieldDslScope.() -> Unit = {},
    ) {
        visitor.visitField(name, type, attributes, offset, initValue)?.let { fv ->
            FieldDslScope(fv).block()
            fv.visitEnd()
        }
    }

    fun property(
        name: String,
        type: TypeSpec,
        callConv: CallConv = CallConv(),
        attributes: PropertyAttributes = PropertyAttributes(),
        parameters: List<Parameter> = emptyList(),
        block: PropertyDslScope.() -> Unit,
    ) {
        visitor.visitProperty(
            name, type, callConv, attributes, parameters,
        )?.let { pv ->
            PropertyDslScope(pv).block()
            pv.visitEnd()
        }
    }

    fun override(baseType: TypeSpec, baseName: String, implementation: MethodReference) =
        visitor.visitOverride(baseType, baseName, implementation)

    fun pack(packSize: Int) = visitor.visitPack(packSize)

    fun size(classSize: Int) = visitor.visitSize(classSize)

    fun event(
        name: String,
        type: TypeSpec,
        attributes: EventAttributes = EventAttributes(),
        block: EventDslScope.() -> Unit,
    ) {
        visitor.visitEvent(
            name, type, attributes,
        )?.let { ev ->
            EventDslScope(ev).block()
            ev.visitEnd()
        }
    }
}
