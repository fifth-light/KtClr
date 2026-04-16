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
        parameters: List<MethodParameter> = emptyList(),
    ): MethodVisitor?

    fun visitEnd()
}
