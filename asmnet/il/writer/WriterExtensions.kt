package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*

fun WriteScope.resolutionScope(resolutionScope: ResolutionScope) {
    when (resolutionScope) {
        is ResolutionScope.Assembly -> {
            +'['
            identifier(resolutionScope.name)
            +']'
        }

        is ResolutionScope.Module -> {
            +".module "
            identifier(resolutionScope.name)
        }
    }
}

// ECMA-335 II.7.1
fun WriteScope.type(type: TypeSpec) {
    when (type) {
        is Type.BuiltInType -> +type.assemblyName

        Type.Void -> +"void"

        is Type.MethodPointer -> {
            callConv(type.callConv)
            +' '
            type(type.returnType)
            +"*("
            params(type.parameterTypes)
            +')'
        }

        is Type.UnmanagedTypePointer -> {
            type(type.type)
            +"*"
        }

        is Type.ManagedTypePointer -> {
            type(type.type)
            +"&"
        }

        is Type.Array -> {
            type(type.type)
            +'['
            type.bounds.forEachIndexed { index, bound ->
                if (bound == null || (bound.first == 0 && bound.last == Int.MAX_VALUE)) {
                    +"..."
                } else if (bound.first == 0) {
                    +"${bound.last}"
                } else if (bound.last == Int.MAX_VALUE) {
                    +"${bound.first}"
                    +"..."
                } else {
                    +"${bound.first}"
                    +"..."
                    +"${bound.last}"
                }
                if (index < type.bounds.size - 1) +", "
            }
            +']'
        }

        is Type.ValueType -> {
            +"valuetype "
            typeSpec(type.type)
        }

        is Type.Pinned -> {
            type(type.type)
            +" pinned"
        }

        is TypeReference -> {
            +"class "
            type.resolutionScope?.let { resolutionScope(it) }
            type.names.forEachIndexed { index, name ->
                identifier(name)
                if (index < type.names.size - 1) +'/'
            }
        }
    }
}

// ECMA-335 II.7.3
fun WriteScope.typeSpec(type: TypeSpec) {
    when (type) {
        is TypeReference -> {
            type.resolutionScope?.let { resolutionScope(it) }
            type.names.forEachIndexed { index, name ->
                identifier(name)
                if (index < type.names.size - 1) +'/'
            }
        }

        else -> type(type)
    }
}

// ECMA-335 II.15.4.1.3
fun WriteScope.localsSignature(init: Boolean, locals: List<LocalVariable>) {
    if (locals.isEmpty()) return
    +".locals"
    if (init) {
        +" init"
    }
    +" ("
    locals.forEachIndexed { index, local ->
        type(local.type)
        local.name?.let {
            +' '
            identifier(it)
        }
        if (index < locals.size - 1) {
            +", "
        }
    }
    +")"
    line()
}

// ECMA-335 II.16
fun WriteScope.fieldDecl(
    name: String,
    type: TypeSpec,
    attributes: FieldAttributes,
    offset: Int?,
    initValue: FieldInitValue?,
) {
    +".field "
    offset?.let {
        +"[${it}] "
    }
    fieldAttr(attributes)
    type(type)
    +' '
    identifier(name)
    initValue?.let {
        +" = "
        fieldInitValue(it)
    }
    line()
}

// ECMA-335 VI.C.4.8
fun WriteScope.methodRef(ref: MethodReference) {
    callConv(ref.callConv)
    +' '
    type(ref.returnType)
    +' '
    typeSpec(ref.declaringType)
    +"::"
    identifier(ref.name)
    +'('
    ref.parameterTypes.forEachIndexed { index, type ->
        if (index > 0) +", "
        type(type)
    }
    +')'
}

// ECMA-335 VI.C.4.9
fun WriteScope.fieldRef(ref: FieldReference) {
    type(ref.fieldType)
    +' '
    typeSpec(ref.declaringType)
    +"::"
    identifier(ref.name)
}

// ECMA-335 II.10.1
fun WriteScope.typeAttr(attrs: TypeAttributes) {
    when (val visibility = attrs.visibility) {
        TypeAttributes.NotPublic -> +"private "
        TypeAttributes.Public -> +"public "
        TypeAttributes.NestedPublic -> +"nested public "
        TypeAttributes.NestedPrivate -> +"nested private "
        TypeAttributes.NestedFamily -> +"nested family "
        TypeAttributes.NestedAssembly -> +"nested assembly "
        TypeAttributes.NestedFamANDAssem -> +"nested famandassem "
        TypeAttributes.NestedFamORAssem -> +"nested famorassem "
        else -> throw IllegalArgumentException("Unknown visibility: $visibility")
    }
    when (val layout = attrs.layout) {
        TypeAttributes.AutoLayout -> +"auto "
        TypeAttributes.SequentialLayout -> +"sequential "
        TypeAttributes.ExplicitLayout -> +"explicit "
        else -> throw IllegalArgumentException("Unknown layout: $layout")
    }
    when (val stringFormatting = attrs.stringFormatting) {
        TypeAttributes.AnsiClass -> +"ansi "
        TypeAttributes.UnicodeClass -> +"unicode "
        TypeAttributes.AutoClass -> +"autochar "
        TypeAttributes.CustomFormatClass -> throw IllegalArgumentException("Custom layout is unused")
        else -> throw IllegalArgumentException("Unknown string formatting: $stringFormatting")
    }
    when (val classSemantics = attrs.classSemantics) {
        TypeAttributes.Interface -> +"interface "
        TypeAttributes.Class -> {}
        else -> throw IllegalArgumentException("Unknown semantics: $classSemantics")
    }
    if (attrs.abstract) +"abstract "
    if (attrs.sealed) +"sealed "
    if (attrs.specialName) +"specialname "
    if (attrs.import) +"import "
    if (attrs.serializable) +"serializable "
    if (attrs.rtSpecialName) +"rtspecialname "
    if (attrs.beforeFieldInit) +"beforefieldinit "
}

// ECMA-335 II.15.4.2
fun WriteScope.methodAttr(attrs: List<MethodAttribute>) {
    for (attr in attrs) {
        when (attr) {
            MethodAttribute.CompilerControlled -> {}
            MethodAttribute.Private -> +"private "
            MethodAttribute.FamANDAssem -> +"famandassem "
            MethodAttribute.Assem -> +"assembly "
            MethodAttribute.Family -> +"family "
            MethodAttribute.FamORAssem -> +"famorassem "
            MethodAttribute.Public -> +"public "

            MethodAttribute.UnmanagedExport -> throw IllegalArgumentException("Unmanaged export is unused")
            MethodAttribute.Static -> +"static "
            MethodAttribute.Final -> +"final "
            MethodAttribute.Virtual -> +"virtual "
            MethodAttribute.HideBySig -> +"hidebysig "

            MethodAttribute.ReuseSlot -> {}
            MethodAttribute.NewSlot -> +"newslot "

            MethodAttribute.Strict -> +"strict "
            MethodAttribute.Abstract -> +"abstract "
            MethodAttribute.SpecialName -> +"specialname "
            MethodAttribute.RTSpecialName -> +"rtspecialname "
            MethodAttribute.HasSecurity -> +"hassecurity "
            MethodAttribute.RequireSecObject -> +"requiresecobject "

            is MethodAttribute.PInvokeImpl -> {
                +"pinvokeimpl("
                quoted(attr.moduleName)
                attr.methodName?.let {
                    +" as "
                    quoted(it)
                }
                pinvokeAttr(attr.attributes)
                +") "
            }
            else -> throw IllegalArgumentException("Unknown method attribute: $attr")
        }
    }
}

// ECMA-335 II.15.5.2
fun WriteScope.pinvokeAttr(attrs: PInvokeAttributes) {
    if (attrs.noMangle) +" nomangle"

    when (attrs.charSet) {
        PInvokeAttributes.CharSetNotSpec -> {}
        PInvokeAttributes.CharSetAnsi -> +" ansi"
        PInvokeAttributes.CharSetUnicode -> +" unicode"
        PInvokeAttributes.CharSetAuto -> +" autochar"
        else -> throw IllegalArgumentException("Unknown charset: ${attrs.charSet}")
    }

    if (attrs.supportsLastError) +" lasterr"

    when (attrs.callConv) {
        PInvokeAttributes.CallConvPlatformApi -> +" platformapi"
        PInvokeAttributes.CallConvCdecl -> +" cdecl"
        PInvokeAttributes.CallConvStdCall -> +" stdcall"
        PInvokeAttributes.CallConvThisCall -> +" thiscall"
        PInvokeAttributes.CallConvFastCall -> +" fastcall"
        else -> throw IllegalArgumentException("Unknown calling convention: ${attrs.callConv}")
    }
}

// ECMA-335 II.15.4.3
fun WriteScope.implAttr(attrs: ImplementationAttributes) {
    when (attrs.codeType) {
        ImplementationAttributes.IL -> +"cil "
        ImplementationAttributes.Native -> +"native "
        ImplementationAttributes.OPTIL -> +"optil "
        ImplementationAttributes.Runtime -> +"runtime "
    }

    when (attrs.managed) {
        ImplementationAttributes.Managed -> +"managed "
        ImplementationAttributes.Unmanaged -> +"unmanaged "
        else -> throw IllegalArgumentException("Unknown managed: $attrs.managed")
    }

    if (attrs.forwardRef) +"forwardref "
    if (attrs.preserveSig) +"preservesig "
    if (attrs.internalCall) +"internalcall "
    if (attrs.synchronized) +"synchronized "
    if (attrs.noInlining) +"noinlining "
    if (attrs.noOptimization) +"nooptimization "
}

// ECMA-335 II.15.3
fun WriteScope.callConv(callConv: CallConv) {
    if (callConv.instance) +"instance "
    if (callConv.explicit) +"explicit "
    callKind(callConv.callKind)
}

// ECMA-335 II.15.3
fun WriteScope.callKind(callKind: CallKind) {
    when (callKind) {
        is CallKind.Unmanaged -> +callKind.assemblyName
        is CallKind.Managed -> if (callKind.vararg) {
            +"vararg"
        }
    }
}

// ECMA-335 II.15.4
fun WriteScope.paramAttr(paramAttr: ParamAttributes) {
    if (paramAttr.`in`) +"[in] "
    if (paramAttr.out) +"[out] "
    if (paramAttr.optional) +"[opt] "
}

// ECMA-335 II.15.4
fun WriteScope.param(param: Parameter) {
    paramAttr(param.flags)
    type(param.type)
    param.name?.let { +' '; identifier(it) }
}

// ECMA-335 II.15.4
fun WriteScope.params(parameters: List<Parameter>) {
    +'('
    parameters.forEachIndexed { index, param ->
        if (index > 0) +", "
        param(param)
    }
    +')'
}

fun WriteScope.opcode(code: OpCode.Code) {
    when (code) {
        OpCode.Code.nop -> +"nop"
        OpCode.Code.`break` -> +"break"
        OpCode.Code.ldarg0 -> +"ldarg.0"
        OpCode.Code.ldarg1 -> +"ldarg.1"
        OpCode.Code.ldarg2 -> +"ldarg.2"
        OpCode.Code.ldarg3 -> +"ldarg.3"
        OpCode.Code.ldloc0 -> +"ldloc.0"
        OpCode.Code.ldloc1 -> +"ldloc.1"
        OpCode.Code.ldloc2 -> +"ldloc.2"
        OpCode.Code.ldloc3 -> +"ldloc.3"
        OpCode.Code.stloc0 -> +"stloc.0"
        OpCode.Code.stloc1 -> +"stloc.1"
        OpCode.Code.stloc2 -> +"stloc.2"
        OpCode.Code.stloc3 -> +"stloc.3"
        OpCode.Code.ldargS -> +"ldarg.s"
        OpCode.Code.ldargaS -> +"ldarga.s"
        OpCode.Code.stargS -> +"starg.s"
        OpCode.Code.ldlocS -> +"ldloc.s"
        OpCode.Code.ldlocaS -> +"ldloca.s"
        OpCode.Code.stlocS -> +"stloc.s"
        OpCode.Code.ldnull -> +"ldnull"
        OpCode.Code.ldcI4M1 -> +"ldc.i4.m1"
        OpCode.Code.ldcI40 -> +"ldc.i4.0"
        OpCode.Code.ldcI41 -> +"ldc.i4.1"
        OpCode.Code.ldcI42 -> +"ldc.i4.2"
        OpCode.Code.ldcI43 -> +"ldc.i4.3"
        OpCode.Code.ldcI44 -> +"ldc.i4.4"
        OpCode.Code.ldcI45 -> +"ldc.i4.5"
        OpCode.Code.ldcI46 -> +"ldc.i4.6"
        OpCode.Code.ldcI47 -> +"ldc.i4.7"
        OpCode.Code.ldcI48 -> +"ldc.i4.8"
        OpCode.Code.ldcI4S -> +"ldc.i4.s"
        OpCode.Code.ldcI4 -> +"ldc.i4"
        OpCode.Code.ldcI8 -> +"ldc.i8"
        OpCode.Code.ldcR4 -> +"ldc.r4"
        OpCode.Code.ldcR8 -> +"ldc.r8"
        OpCode.Code.dup -> +"dup"
        OpCode.Code.pop -> +"pop"
        OpCode.Code.jmp -> +"jmp"
        OpCode.Code.call -> +"call"
        OpCode.Code.calli -> +"calli"
        OpCode.Code.ret -> +"ret"
        OpCode.Code.brS -> +"br.s"
        OpCode.Code.brfalseS -> +"brfalse.s"
        OpCode.Code.brtrueS -> +"brtrue.s"
        OpCode.Code.beqS -> +"beq.s"
        OpCode.Code.bgeS -> +"bge.s"
        OpCode.Code.bgtS -> +"bgt.s"
        OpCode.Code.bleS -> +"ble.s"
        OpCode.Code.bltS -> +"blt.s"
        OpCode.Code.bneUnS -> +"bne.un.s"
        OpCode.Code.bgeUnS -> +"bge.un.s"
        OpCode.Code.bgtUnS -> +"bgt.un.s"
        OpCode.Code.bleUnS -> +"ble.un.s"
        OpCode.Code.bltUnS -> +"blt.un.s"
        OpCode.Code.br -> +"br"
        OpCode.Code.brfalse -> +"brfalse"
        OpCode.Code.brtrue -> +"brtrue"
        OpCode.Code.beq -> +"beq"
        OpCode.Code.bge -> +"bge"
        OpCode.Code.bgt -> +"bgt"
        OpCode.Code.ble -> +"ble"
        OpCode.Code.blt -> +"blt"
        OpCode.Code.bneUn -> +"bne.un"
        OpCode.Code.bgeUn -> +"bge.un"
        OpCode.Code.bgtUn -> +"bgt.un"
        OpCode.Code.bleUn -> +"ble.un"
        OpCode.Code.bltUn -> +"blt.un"
        OpCode.Code.switch -> +"switch"
        OpCode.Code.ldindI1 -> +"ldind.i1"
        OpCode.Code.ldindU1 -> +"ldind.u1"
        OpCode.Code.ldindI2 -> +"ldind.i2"
        OpCode.Code.ldindU2 -> +"ldind.u2"
        OpCode.Code.ldindI4 -> +"ldind.i4"
        OpCode.Code.ldindU4 -> +"ldind.u4"
        OpCode.Code.ldindI8 -> +"ldind.i8"
        OpCode.Code.ldindI -> +"ldind.i"
        OpCode.Code.ldindR4 -> +"ldind.r4"
        OpCode.Code.ldindR8 -> +"ldind.r8"
        OpCode.Code.ldindRef -> +"ldind.ref"
        OpCode.Code.stindRef -> +"stind.ref"
        OpCode.Code.stindI1 -> +"stind.i1"
        OpCode.Code.stindI2 -> +"stind.i2"
        OpCode.Code.stindI4 -> +"stind.i4"
        OpCode.Code.stindI8 -> +"stind.i8"
        OpCode.Code.stindR4 -> +"stind.r4"
        OpCode.Code.stindR8 -> +"stind.r8"
        OpCode.Code.add -> +"add"
        OpCode.Code.sub -> +"sub"
        OpCode.Code.mul -> +"mul"
        OpCode.Code.div -> +"div"
        OpCode.Code.divUn -> +"div.un"
        OpCode.Code.rem -> +"rem"
        OpCode.Code.remUn -> +"rem.un"
        OpCode.Code.and -> +"and"
        OpCode.Code.or -> +"or"
        OpCode.Code.xor -> +"xor"
        OpCode.Code.shl -> +"shl"
        OpCode.Code.shr -> +"shr"
        OpCode.Code.shrUn -> +"shr.un"
        OpCode.Code.neg -> +"neg"
        OpCode.Code.not -> +"not"
        OpCode.Code.convI1 -> +"conv.i1"
        OpCode.Code.convI2 -> +"conv.i2"
        OpCode.Code.convI4 -> +"conv.i4"
        OpCode.Code.convI8 -> +"conv.i8"
        OpCode.Code.convR4 -> +"conv.r4"
        OpCode.Code.convR8 -> +"conv.r8"
        OpCode.Code.convU4 -> +"conv.u4"
        OpCode.Code.convU8 -> +"conv.u8"
        OpCode.Code.callvirt -> +"callvirt"
        OpCode.Code.cpobj -> +"cpobj"
        OpCode.Code.ldobj -> +"ldobj"
        OpCode.Code.ldstr -> +"ldstr"
        OpCode.Code.newobj -> +"newobj"
        OpCode.Code.castclass -> +"castclass"
        OpCode.Code.isinst -> +"isinst"
        OpCode.Code.convRUn -> +"conv.r.un"
        OpCode.Code.unbox -> +"unbox"
        OpCode.Code.`throw` -> +"throw"
        OpCode.Code.ldfld -> +"ldfld"
        OpCode.Code.ldflda -> +"ldflda"
        OpCode.Code.stfld -> +"stfld"
        OpCode.Code.ldsfld -> +"ldsfld"
        OpCode.Code.ldsflda -> +"ldsflda"
        OpCode.Code.stsfld -> +"stsfld"
        OpCode.Code.stobj -> +"stobj"
        OpCode.Code.convOvfI1Un -> +"conv.ovf.i1.un"
        OpCode.Code.convOvfI2Un -> +"conv.ovf.i2.un"
        OpCode.Code.convOvfI4Un -> +"conv.ovf.i4.un"
        OpCode.Code.convOvfI8Un -> +"conv.ovf.i8.un"
        OpCode.Code.convOvfU1Un -> +"conv.ovf.u1.un"
        OpCode.Code.convOvfU2Un -> +"conv.ovf.u2.un"
        OpCode.Code.convOvfU4Un -> +"conv.ovf.u4.un"
        OpCode.Code.convOvfU8Un -> +"conv.ovf.u8.un"
        OpCode.Code.convOvfIUn -> +"conv.ovf.i.un"
        OpCode.Code.convOvfUUn -> +"conv.ovf.u.un"
        OpCode.Code.box -> +"box"
        OpCode.Code.newarr -> +"newarr"
        OpCode.Code.ldlen -> +"ldlen"
        OpCode.Code.ldelema -> +"ldelema"
        OpCode.Code.ldelemI1 -> +"ldelem.i1"
        OpCode.Code.ldelemU1 -> +"ldelem.u1"
        OpCode.Code.ldelemI2 -> +"ldelem.i2"
        OpCode.Code.ldelemU2 -> +"ldelem.u2"
        OpCode.Code.ldelemI4 -> +"ldelem.i4"
        OpCode.Code.ldelemU4 -> +"ldelem.u4"
        OpCode.Code.ldelemI8 -> +"ldelem.i8"
        OpCode.Code.ldelemI -> +"ldelem.i"
        OpCode.Code.ldelemR4 -> +"ldelem.r4"
        OpCode.Code.ldelemR8 -> +"ldelem.r8"
        OpCode.Code.ldelemRef -> +"ldelem.ref"
        OpCode.Code.stelemI -> +"stelem.i"
        OpCode.Code.stelemI1 -> +"stelem.i1"
        OpCode.Code.stelemI2 -> +"stelem.i2"
        OpCode.Code.stelemI4 -> +"stelem.i4"
        OpCode.Code.stelemI8 -> +"stelem.i8"
        OpCode.Code.stelemR4 -> +"stelem.r4"
        OpCode.Code.stelemR8 -> +"stelem.r8"
        OpCode.Code.stelemRef -> +"stelem.ref"
        OpCode.Code.ldelem -> +"ldelem"
        OpCode.Code.stelem -> +"stelem"
        OpCode.Code.unboxAny -> +"unbox.any"
        OpCode.Code.convOvfI1 -> +"conv.ovf.i1"
        OpCode.Code.convOvfU1 -> +"conv.ovf.u1"
        OpCode.Code.convOvfI2 -> +"conv.ovf.i2"
        OpCode.Code.convOvfU2 -> +"conv.ovf.u2"
        OpCode.Code.convOvfI4 -> +"conv.ovf.i4"
        OpCode.Code.convOvfU4 -> +"conv.ovf.u4"
        OpCode.Code.convOvfI8 -> +"conv.ovf.i8"
        OpCode.Code.convOvfU8 -> +"conv.ovf.u8"
        OpCode.Code.refanyval -> +"refanyval"
        OpCode.Code.ckfinite -> +"ckfinite"
        OpCode.Code.mkrefany -> +"mkrefany"
        OpCode.Code.ldtoken -> +"ldtoken"
        OpCode.Code.convU2 -> +"conv.u2"
        OpCode.Code.convU1 -> +"conv.u1"
        OpCode.Code.convI -> +"conv.i"
        OpCode.Code.convOvfI -> +"conv.ovf.i"
        OpCode.Code.convOvfU -> +"conv.ovf.u"
        OpCode.Code.addOvf -> +"add.ovf"
        OpCode.Code.addOvfUn -> +"add.ovf.un"
        OpCode.Code.mulOvf -> +"mul.ovf"
        OpCode.Code.mulOvfUn -> +"mul.ovf.un"
        OpCode.Code.subOvf -> +"sub.ovf"
        OpCode.Code.subOvfUn -> +"sub.ovf.un"
        OpCode.Code.endfinally -> +"endfinally"
        OpCode.Code.leave -> +"leave"
        OpCode.Code.leaveS -> +"leave.s"
        OpCode.Code.stindI -> +"stind.i"
        OpCode.Code.convU -> +"conv.u"
        OpCode.Code.arglist -> +"arglist"
        OpCode.Code.ceq -> +"ceq"
        OpCode.Code.cgt -> +"cgt"
        OpCode.Code.cgtUn -> +"cgt.un"
        OpCode.Code.clt -> +"clt"
        OpCode.Code.cltUn -> +"clt.un"
        OpCode.Code.ldftn -> +"ldftn"
        OpCode.Code.ldvirtftn -> +"ldvirtftn"
        OpCode.Code.ldarg -> +"ldarg"
        OpCode.Code.ldarga -> +"ldarga"
        OpCode.Code.starg -> +"starg"
        OpCode.Code.ldloc -> +"ldloc"
        OpCode.Code.ldloca -> +"ldloca"
        OpCode.Code.stloc -> +"stloc"
        OpCode.Code.localloc -> +"localloc"
        OpCode.Code.endfilter -> +"endfilter"
        OpCode.Code.initobj -> +"initobj"
        OpCode.Code.cpblk -> +"cpblk"
        OpCode.Code.initblk -> +"initblk"
        OpCode.Code.rethrow -> +"rethrow"
        OpCode.Code.sizeof -> +"sizeof"
        OpCode.Code.refanytype -> +"refanytype"
        else -> throw IllegalArgumentException("Unknown opcode: $code")
    }
}

fun WriteScope.opcode(opcode: OpCode) {
    opcode.prefixes?.forEach {
        // Prefix is also an instruction per spec III.2, so we emit a line after each prefixes
        when (it) {
            is OpCode.Prefix.Unaligned -> {
                +"unaligned. "
                num(it.alignment)
            }

            is OpCode.Prefix.Constrained -> {
                +"constrained. "
                typeSpec(it.thisType)
            }

            is OpCode.Prefix.No -> {
                +"no. "
                num(it.flags.value)
            }

            OpCode.Prefix.Volatile -> +"volatile."
            OpCode.Prefix.Tail -> +"tail."
            OpCode.Prefix.Readonly -> +"readonly."
            else -> throw IllegalArgumentException("Unknown prefix: $it")
        }
        line()
    }
    opcode(opcode.code)
}

// ECMA-335 II.21
fun WriteScope.customAttributeRef(ref: CustomAttributeReference, blob: ByteArray?) {
    +".custom "
    callConv(ref.callConv)
    +' '
    type(ref.returnType)
    +' '
    typeSpec(ref.attributeType)
    +"::.ctor("
    ref.parameterTypes.forEachIndexed { index, type ->
        if (index > 0) +", "
        type(type)
    }
    +')'
    blob?.let {
        +" = ( "
        hex(it)
        +" )"
    }
    line()
}

// ECMA-335 II.17
fun WriteScope.propertyAttr(attrs: PropertyAttributes) {
    if (attrs.specialName) +"specialname "
    if (attrs.rtSpecialName) +"rtspecialname "
}

// ECMA-335 II.18
fun WriteScope.eventAttr(attrs: EventAttributes) {
    if (attrs.specialName) +"specialname "
    if (attrs.rtSpecialName) +"rtspecialname "
}

// ECMA-335 II.16.1
fun WriteScope.fieldAttr(attrs: FieldAttributes) {
    when (val access = attrs.fieldAccess) {
        FieldAttributes.CompilerControlled -> +"compilercontrolled "
        FieldAttributes.Private -> +"private "
        FieldAttributes.FamANDAssem -> +"famandassem "
        FieldAttributes.Assembly -> +"assembly "
        FieldAttributes.Family -> +"family "
        FieldAttributes.FamORAssem -> +"famorassem "
        FieldAttributes.Public -> +"public "
        else -> throw IllegalArgumentException("Unknown field access: $access")
    }

    if (attrs.static) +"static "
    if (attrs.initOnly) +"initonly "
    if (attrs.literal) +"literal "
    if (attrs.notSerialized) +"notserialized "
    if (attrs.specialName) +"specialname "

    if (attrs.pInvokeImpl) error("pinvokeimpl attribute can't be set on fields")

    if (attrs.rtSpecialName) +"rtspecialname "
    if (attrs.hasFieldMarshal) TODO("marshal attribute is not yet supported")
}

// ECMA-335 II.16.2
fun WriteScope.fieldInitValue(value: FieldInitValue) {
    when (value) {
        is FieldInitValue.Boolean -> +"bool(${value.value})"
        is FieldInitValue.Char -> +"char(${value.value.code})"

        is FieldInitValue.Int8 -> {
            +"int8("; hex(value.value); +')'
        }

        is FieldInitValue.Int16 -> {
            +"int16("; hex(value.value); +')'
        }

        is FieldInitValue.Int32 -> {
            +"int32("; hex(value.value); +')'
        }

        is FieldInitValue.Int64 -> {
            +"int64("; hex(value.value); +')'
        }

        is FieldInitValue.UnsignedInt8 -> {
            +"unsigned int8("; hex(value.value); +')'
        }

        is FieldInitValue.UnsignedInt16 -> {
            +"unsigned int16("; hex(value.value); +')'
        }

        is FieldInitValue.UnsignedInt32 -> {
            +"unsigned int32("; hex(value.value); +')'
        }

        is FieldInitValue.UnsignedInt64 -> {
            +"unsigned int64("; hex(value.value); +')'
        }

        is FieldInitValue.Float32 -> +"float32(${value.value})"
        is FieldInitValue.Float64 -> +"float64(${value.value})"
        is FieldInitValue.String -> quoted(value.value)
        is FieldInitValue.NullRef -> +"nullref"

        is FieldInitValue.ByteArray -> {
            +"bytearray("
            hex(value.value)
            +')'
        }
    }
}
