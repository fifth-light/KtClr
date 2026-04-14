package top.fifthlight.asmnet

// ECMA-335 II.10
interface ClassVisitor {
    // ECMA-335 II.10.1
    fun visit(attrs: TypeAttributes, extends: String?, implements: Set<String>)

    // ECMA-335 II.15
    fun visitMethod(
        name: String,
        returnType: TypeSpec,
        callConv: CallConv,
        attributes: MethodAttributes,
        implAttributes: ImplementationAttributes,
        parameters: List<MethodParameter>,
    ): MethodVisitor?

    fun visitEnd()
}
