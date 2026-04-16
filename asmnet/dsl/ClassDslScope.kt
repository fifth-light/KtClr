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
        parameters: List<MethodParameter> = emptyList(),
        block: MethodDslScope.() -> Unit,
    ) {
        visitor.visitMethod(
            name, returnType, callConv, attributes, implAttributes, entryPoint, parameters,
        )?.let { mv ->
            MethodDslScope(mv).block()
            mv.visitEnd()
        }
    }

    fun field(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
    ) = visitor.visitField(name, type, attributes, offset, initValue)
}
