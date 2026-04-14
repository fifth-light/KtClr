package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.CallConv
import top.fifthlight.asmnet.CallKind
import top.fifthlight.asmnet.ImplementationAttributes
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.MethodParameter
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.ParamAttributes
import top.fifthlight.asmnet.TypeAttributes
import top.fifthlight.asmnet.TypeSpec
import top.fifthlight.asmnet.il.writer.TextWriter.WriteScope

fun WriteScope.type(type: TypeSpec) {
    +type.toString()
}

// ECMA-335 VI.C.4.8
fun WriteScope.methodRef(ref: MethodReference) {
    callConv(ref.callConv)
    +' '
    type(ref.returnType)
    +' '
    type(ref.declaringType)
    +"::"
    identifier(ref.name)
    +'('
    ref.parameterTypes.forEachIndexed { index, type ->
        if (index > 0) +", "
        type(type)
    }
    +')'
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
fun WriteScope.methodAttr(attrs: MethodAttributes) {
    when (val memberAccess = attrs.memberAccess) {
        MethodAttributes.CompilerControlled -> +"compilercontrolled "
        MethodAttributes.Private -> +"private "
        MethodAttributes.FamANDAssem -> +"famandassem "
        MethodAttributes.Assem -> +"assembly "
        MethodAttributes.Family -> +"family "
        MethodAttributes.FamORAssem -> +"famorassem "
        MethodAttributes.Public -> +"public "
        else -> throw IllegalArgumentException("Unknown member access: $memberAccess")
    }

    if (attrs.static) +"static "
    if (attrs.final) +"final "
    if (attrs.virtual) +"virtual "
    if (attrs.hideBySig) +"hidebysig "

    when (val vtableLayout = attrs.vtableLayout) {
        MethodAttributes.ReuseSlot -> {}
        MethodAttributes.NewSlot -> +"newslot "
        else -> throw IllegalArgumentException("Unknown vtable layout: $vtableLayout")
    }

    if (attrs.strict) +"strict "
    if (attrs.abstract) +"abstract "
    if (attrs.specialName) +"specialname "

    if (attrs.pInvokeImpl) TODO()
    if (attrs.unmanagedExport) throw IllegalArgumentException("Unmanaged export is unused")
    if (attrs.rTSpecialName) +"rtspecialname "
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
fun WriteScope.param(param: MethodParameter) {
    paramAttr(param.flags)
    type(param.type)
    param.name?.let { +' '; identifier(it) }
}

// ECMA-335 II.15.4
fun WriteScope.params(parameters: List<MethodParameter>) {
    +'('
    parameters.forEachIndexed { index, param ->
        if (index > 0) +", "
        param(param)
    }
    +')'
}
