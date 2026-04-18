package top.fifthlight.asmnet

// ECMA-335 II.10
interface ClassVisitor {
    // ECMA-335 II.10.1
    fun visit(
        attrs: TypeAttributes = TypeAttributes(
            TypeAttributes.Public,
            TypeAttributes.AutoLayout,
            TypeAttributes.Class,
            TypeAttributes.AnsiClass,
        ),
        extends: TypeSpec? = null,
        implements: Set<TypeSpec> = emptySet(),
    )

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    // ECMA-335 II.15
    fun visitMethod(
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
    ): MethodVisitor?

    // ECMA-335 II.16
    fun visitField(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
        // TODO: DataLabel
    )

    // ECMA-335 II.17
    fun visitProperty(
        name: String,
        type: TypeSpec,
        callConv: CallConv = CallConv(),
        attributes: PropertyAttributes = PropertyAttributes(),
        parameters: List<Parameter> = emptyList(),
    ): PropertyVisitor?

    // ECMA-335 II.18
    fun visitEvent(
        name: String,
        type: TypeSpec,
        attributes: EventAttributes = EventAttributes(),
    ): EventVisitor?

    // ECMA-335 II.10.7
    fun visitPack(packSize: Int)

    // ECMA-335 II.10.7
    fun visitSize(classSize: Int)

    // ECMA-335 II.10.3.2
    fun visitOverride(baseType: TypeSpec, baseName: String, implementation: MethodReference)

    fun visitEnd()
}
