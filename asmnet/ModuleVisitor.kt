package top.fifthlight.asmnet

import java.util.*

// ECMA-335 II.5.10
interface ModuleVisitor {
    /* Only in assembly */
    // ECMA-335 II.6.2
    fun visitAssembly(
        name: String,
        declaration: AssemblyDeclaration = AssemblyDeclaration(),
    )

    // ECMA-335 II.6.3
    fun visitExternAssembly(
        name: String,
        declaration: ExternAssemblyDeclaration = ExternAssemblyDeclaration(),
    )

    // ECMA-335 II.6.3
    fun visitFile(
        noMetadata: Boolean = false,
        fileName: String,
        hash: ByteArray,
        entryPoint: Boolean = false,
    )
    /* End of only in assembly */

    // ECMA-335 II.6.4, mvid can be null when reading ILASM
    fun visitModule(name: String, mvid: UUID? = null)

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
        returnType: TypeSpec = Type.Void,
        callConv: CallConv = CallConv(),
        attributes: List<MethodAttribute> = listOf(
            MethodAttribute.Public,
        ),
        implAttributes: ImplementationAttributes = ImplementationAttributes(
            ImplementationAttributes.IL,
            ImplementationAttributes.Managed,
        ),
        entryPoint: Boolean = false,
        parameters: List<Parameter> = emptyList(),
    ): MethodVisitor?

    // ECMA-335 II.6.2.2
    fun visitManifestResource(
        name: String,
        attributes: ManifestResourceAttributes = ManifestResourceAttributes(ManifestResourceAttributes.Public),
    ): ManifestResourceVisitor?

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    // ECMA-335 II.16
    fun visitField(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
        dataLabel: DataLabel? = null,
    ): FieldVisitor?

    // ECMA-335 II.16.3
    fun visitData(
        label: DataLabel? = null,
        tls: Boolean = false,
        items: List<DataItem>,
    )

    fun visitEnd()
}
