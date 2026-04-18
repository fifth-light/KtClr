package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*
import java.util.*

@AsmDsl
class ModuleDslScope(val visitor: ModuleVisitor) {
    fun assembly(
        name: String,
        declaration: AssemblyDeclaration = AssemblyDeclaration(),
    ) = visitor.visitAssembly(name, declaration)

    fun externAssembly(
        name: String,
        declaration: ExternAssemblyDeclaration = ExternAssemblyDeclaration(),
    ) = visitor.visitExternAssembly(name, declaration)

    fun module(name: String, mvid: UUID? = null) = visitor.visitModule(name, mvid)

    fun externModule(fileName: String) = visitor.visitExternModule(fileName)

    fun file(
        fileName: String,
        hash: ByteArray,
        noMetadata: Boolean = false,
        entryPoint: Boolean = false,
    ) = visitor.visitFile(noMetadata, fileName, hash, entryPoint)

    fun imageBase(base: Int) = visitor.visitImageBase(base)

    fun fileAlignment(alignment: Int) = visitor.visitFileAlignment(alignment)

    fun stackReserve(stackReserve: Int) = visitor.visitStackReserve(stackReserve)

    fun subsystem(subsystem: Subsystem) = visitor.visitSubsystem(subsystem)

    fun corFlags(flag: RuntimeFlags) = visitor.visitCorFlags(flag)

    fun class_(
        name: String,
        attrs: TypeAttributes = TypeAttributes(
            TypeAttributes.Public,
            TypeAttributes.AutoLayout,
            TypeAttributes.Class,
            TypeAttributes.AnsiClass,
        ),
        extends: TypeSpec? = null,
        implements: Set<TypeSpec> = emptySet(),
        block: ClassDslScope.() -> Unit,
    ) {
        visitor.visitClass(name)?.let { cv ->
            cv.visit(attrs, extends, implements)
            ClassDslScope(cv).block()
            cv.visitEnd()
        }
    }

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
    ) = visitor.visitField(name, type, attributes, offset, initValue)
}
