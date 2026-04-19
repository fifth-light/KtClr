package top.fifthlight.asmnet

// ECMA-335 II.6.2.2
interface ManifestResourceVisitor {
    fun visitFile(fileName: String, offset: Int)

    fun visitExternAssembly(name: String)

    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
