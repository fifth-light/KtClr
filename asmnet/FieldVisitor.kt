package top.fifthlight.asmnet

// ECMA-335 II.16
interface FieldVisitor {
    // ECMA-335 II.21
    fun visitCustomAttribute(reference: CustomAttributeReference, blob: ByteArray?)

    fun visitEnd()
}
