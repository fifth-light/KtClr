package top.fifthlight.asmnet.il.writer.test.integration.marshal

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: MarshalGenerator <output.il>" }
    val il = generateMarshalTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val classType = TypeReference(name = "MarshalTest")
private val pointType = Type.ValueType(TypeReference("MarshalTest", "POINT"))

private fun generateMarshalTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externModule("kernel32.dll")
        externModule("user32.dll")
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("marshal_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("marshal_test.dll")
        class_("MarshalTest",
            attrs = TypeAttributes(
                TypeAttributes.Public,
                TypeAttributes.AutoLayout,
                TypeAttributes.AnsiClass,
                TypeAttributes.BeforeFieldInit,
            ),
            extends = TypeReference(
                resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                name = "System.Object",
            ),
        ) {
            class_("POINT",
                attrs = TypeAttributes(
                    TypeAttributes.NestedPublic,
                    TypeAttributes.SequentialLayout,
                    TypeAttributes.Sealed,
                    TypeAttributes.AnsiClass,
                ),
                extends = TypeReference(
                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                    name = "System.ValueType",
                ),
            ) {
                field(
                    name = "X",
                    type = Type.Int32,
                    attributes = FieldAttributes(FieldAttributes.Public),
                    marshal = NativeType.UnsignedInt32,
                )
                field(
                    name = "Y",
                    type = Type.Int32,
                    attributes = FieldAttributes(FieldAttributes.Public),
                    marshal = NativeType.UnsignedInt32,
                )
            }
            method("GetCurrentProcessId",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                    MethodAttribute.PInvokeImpl(
                        moduleName = "kernel32.dll",
                        attributes = PInvokeAttributes(PInvokeAttributes.CallConvPlatformApi),
                    ),
                ),
                returnType = Type.UnsignedInt32,
                returnMarshal = NativeType.UnsignedInt32,
                implAttributes = ImplementationAttributes(
                    ImplementationAttributes.IL,
                    ImplementationAttributes.Managed,
                    ImplementationAttributes.PreserveSig,
                ),
            ) {}
            method("GetCurrentDirectoryW",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                    MethodAttribute.PInvokeImpl(
                        moduleName = "kernel32.dll",
                        attributes = PInvokeAttributes(PInvokeAttributes.CallConvPlatformApi),
                    ),
                ),
                returnType = Type.UnsignedInt32,
                parameters = listOf(
                    Parameter(Type.UnsignedInt32, "nBufferLength"),
                    Parameter(
                        TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                            name = "System.Text.StringBuilder",
                        ),
                        "lpBuffer",
                        marshal = NativeType.LPWStr,
                    ),
                ),
                implAttributes = ImplementationAttributes(
                    ImplementationAttributes.IL,
                    ImplementationAttributes.Managed,
                    ImplementationAttributes.PreserveSig,
                ),
            ) {}
            method("GetCursorPos",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                    MethodAttribute.PInvokeImpl(
                        moduleName = "user32.dll",
                        attributes = PInvokeAttributes(PInvokeAttributes.CallConvPlatformApi),
                    ),
                ),
                returnType = Type.Bool,
                parameters = listOf(
                    Parameter(
                        Type.ManagedTypePointer(pointType),
                        "lpPoint",
                    ),
                ),
                implAttributes = ImplementationAttributes(
                    ImplementationAttributes.IL,
                    ImplementationAttributes.Managed,
                    ImplementationAttributes.PreserveSig,
                ),
            ) {}
            method("Main",
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                ),
                parameters = listOf(Parameter(Type.Array(Type.String), "args")),
                entryPoint = true,
            ) {
                locals(
                    LocalVariable(Type.UnsignedInt32, "pid"),
                    LocalVariable(Type.Bool, "cursorOk"),
                    LocalVariable(pointType, "pt"),
                )
                maxStack(8)
                code()
                val fail = Label()
                insn(OpCode.Code.call, MethodReference(
                    declaringType = classType,
                    name = "GetCurrentProcessId",
                    returnType = Type.UnsignedInt32,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.ldloca, 2)
                insn(OpCode.Code.initobj, pointType)
                insn(OpCode.Code.ldloca, 2)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = classType,
                    name = "GetCursorPos",
                    returnType = Type.Bool,
                    parameterTypes = listOf(Type.ManagedTypePointer(pointType)),
                ))
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI40)
                insn(OpCode.Code.bleUnS, fail)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.brfalseS, fail)
                ldc("PASS")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                insn(OpCode.Code.ret)
                label(fail)
                ldc("FAIL")
                insn(OpCode.Code.call, MethodReference(
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Console"),
                        name = "System.Console",
                    ),
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.String),
                ))
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
