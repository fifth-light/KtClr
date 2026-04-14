package top.fifthlight.asmnet

import java.util.UUID

// ECMA-335 II.5.10
interface ModuleVisitor {
    /* Only in assembly */
    // ECMA-335 II.6.2
    fun visitAssembly(name: String, declaration: AssemblyDeclaration)
    // ECMA-335 II.6.3
    fun visitExternAssembly(name: String, declaration: ExternAssemblyDeclaration)
    // ECMA-335 II.6.3
    fun visitFile(
        noMetadata: Boolean = false,
        fileName: String,
        hash: ByteArray,
        entryPoint: Boolean = false,
    )
    /* End of only in assembly */

    // ECMA-335 II.6.4, mvid can be null when reading ILASM
    fun visitModule(name: String, mvid: UUID?)
    // ECMA-335 II.6.5
    fun visitExternModule(fileName: String)

    fun visitImageBase(base: Int)
    fun visitFileAlignment(alignment: Int)
    fun visitStackReserve(stackReserve: Int)

    // ECMA-335 II.6.2
    fun visitSubsystem(subsystem: Subsystem)
    // ECMA-335 II.6.2
    fun visitCorFlags(flag: RuntimeFlags)

    // ECMA-335 II.10
    fun visitClass(name: String): ClassVisitor?
    // ECMA-335 II.15
    fun visitMethod(
        name: String,
        returnType: TypeSpec,
        callConv: CallConv,
        vararg: Boolean = false,
        attributes: MethodAttributes,
        implAttributes: ImplementationAttributes,
        entryPoint: Boolean = false,
        parameters: List<MethodParameter>,
    ): MethodVisitor?

    fun visitEnd()
}
