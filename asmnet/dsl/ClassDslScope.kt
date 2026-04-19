/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*

@AsmDsl
class ClassDslScope(val visitor: ClassVisitor) {
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
    ) = visitor.visitMethod(
        name, returnType, callConv, attributes, implAttributes, entryPoint, parameters, returnMarshal,
    )?.let { mv ->
        MethodDslScope(mv).block()
        mv.visitEnd()
    }

    fun custom(reference: CustomAttributeReference, blob: ByteArray?) = visitor.visitCustomAttribute(reference, blob)

    fun field(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes = FieldAttributes(),
        offset: Int? = null,
        initValue: FieldInitValue? = null,
        dataLabel: DataLabel? = null,
        marshal: NativeType? = null,
        block: FieldDslScope.() -> Unit = {},
    ) = visitor.visitField(name, type, attributes, offset, initValue, dataLabel, marshal)?.let { fv ->
        FieldDslScope(fv).block()
        fv.visitEnd()
    }

    fun property(
        name: String,
        type: TypeSpec,
        callConv: CallConv = CallConv(),
        attributes: PropertyAttributes = PropertyAttributes(),
        parameters: List<Parameter> = emptyList(),
        block: PropertyDslScope.() -> Unit,
    ) = visitor.visitProperty(
        name, type, callConv, attributes, parameters,
    )?.let { pv ->
        PropertyDslScope(pv).block()
        pv.visitEnd()
    }

    fun override(baseType: TypeSpec, baseName: String, implementation: MethodReference) =
        visitor.visitOverride(baseType, baseName, implementation)

    fun pack(packSize: Int) = visitor.visitPack(packSize)

    fun size(classSize: Int) = visitor.visitSize(classSize)

    fun event(
        name: String,
        type: TypeSpec,
        attributes: EventAttributes = EventAttributes(),
        block: EventDslScope.() -> Unit,
    ) = visitor.visitEvent(
        name, type, attributes,
    )?.let { ev ->
        EventDslScope(ev).block()
        ev.visitEnd()
    }

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
    ) = visitor.visitClass(name)?.let { cv ->
        cv.visit(attrs, extends, implements)
        ClassDslScope(cv).block()
        cv.visitEnd()
    }

    fun data(
        label: DataLabel? = null,
        tls: Boolean = false,
        items: List<DataItem>,
    ) = visitor.visitData(label, tls, items)
}
