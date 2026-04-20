/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

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

    fun imageBase(base: ULong) = visitor.visitImageBase(base)

    fun fileAlignment(alignment: UInt) = visitor.visitFileAlignment(alignment)

    fun stackReserve(stackReserve: ULong) = visitor.visitStackReserve(stackReserve)

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
        attributes: List<MethodAttribute> = listOf(
            MethodAttribute.Public,
        ),
        implAttributes: ImplementationAttributes = ImplementationAttributes(
            ImplementationAttributes.IL,
            ImplementationAttributes.Managed,
        ),
        entryPoint: Boolean = false,
        parameters: List<Parameter> = emptyList(),
        returnMarshal: NativeType? = null,
        block: MethodDslScope.() -> Unit,
    ) {
        visitor.visitMethod(
            name, returnType, callConv, attributes, implAttributes, entryPoint, parameters, returnMarshal,
        )?.let { mv ->
            MethodDslScope(mv).block()
            mv.visitEnd()
        }
    }

    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)

    fun manifestResource(
        name: String,
        attributes: ManifestResourceAttributes = ManifestResourceAttributes(ManifestResourceAttributes.Public),
        block: ManifestResourceDslScope.() -> Unit = {},
    ) {
        visitor.visitManifestResource(name, attributes)?.let { mv ->
            ManifestResourceDslScope(mv).block()
            mv.visitEnd()
        }
    }

    fun exportedType(
        name: String,
        flags: TypeAttributes = TypeAttributes(TypeAttributes.Public),
        block: ExportedTypeDslScope.() -> Unit = {},
    ) {
        visitor.visitExportedType(name, flags)?.let { ev ->
            ExportedTypeDslScope(ev).block()
            ev.visitEnd()
        }
    }

    fun typeForwarder(
        name: String,
        block: TypeForwarderDslScope.() -> Unit = {},
    ) {
        visitor.visitTypeForwarder(name)?.let { fv ->
            TypeForwarderDslScope(fv).block()
            fv.visitEnd()
        }
    }

    fun field(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
        dataLabel: DataLabel? = null,
        marshal: NativeType? = null,
        block: FieldDslScope.() -> Unit = {},
    ) {
        visitor.visitField(name, type, attributes, offset, initValue, dataLabel, marshal)?.let { fv ->
            FieldDslScope(fv).block()
            fv.visitEnd()
        }
    }

    fun data(
        label: DataLabel? = null,
        tls: Boolean = false,
        items: List<DataItem>,
    ) = visitor.visitData(label, tls, items)
}
