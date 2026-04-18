package top.fifthlight.asmnet.il.writer.test.integration.pinvoke

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: PinvokeGenerator <output.il>" }
    val il = generatePinvokeTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val classType = TypeReference(name = "PinvokeTest")

private fun generatePinvokeTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externModule("kernel32.dll")
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("pinvoke_test", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("pinvoke_test.dll")
        class_("PinvokeTest",
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
            method("GetCurrentProcessId",
                attributes = listOf(
                    MethodAttribute.Private,
                    MethodAttribute.HideBySig,
                    MethodAttribute.Static,
                    MethodAttribute.PInvokeImpl(
                        moduleName = "kernel32.dll",
                        attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                    ),
                ),
                returnType = Type.UnsignedInt32,
                implAttributes = ImplementationAttributes(
                    ImplementationAttributes.IL,
                    ImplementationAttributes.Managed,
                    ImplementationAttributes.PreserveSig,
                ),
            ) {}
            method(".ctor",
                callConv = CallConv(instance = true),
                attributes = listOf(
                    MethodAttribute.Public,
                    MethodAttribute.HideBySig,
                    MethodAttribute.SpecialName,
                    MethodAttribute.RTSpecialName,
                ),
            ) {
                maxStack(8)
                code()
                insn(OpCode.Code.ldarg0)
                insn(OpCode.Code.call, MethodReference(
                    callConv = CallConv(instance = true),
                    declaringType = TypeReference(
                        resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                        name = "System.Object",
                    ),
                    name = ".ctor",
                    returnType = Type.Void,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.ret)
            }
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
                    LocalVariable(Type.UnsignedInt32, "pid1"),
                    LocalVariable(Type.UnsignedInt32, "pid2"),
                    LocalVariable(Type.Bool, "result"),
                )
                maxStack(2)
                code()
                insn(OpCode.Code.call, MethodReference(
                    declaringType = classType,
                    name = "GetCurrentProcessId",
                    returnType = Type.UnsignedInt32,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = classType,
                    name = "GetCurrentProcessId",
                    returnType = Type.UnsignedInt32,
                    parameterTypes = emptyList(),
                ))
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 0)
                val failZero = Label()
                insn(OpCode.Code.brfalseS, failZero)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.brfalseS, failZero)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.ceq)
                val storeResult = Label()
                insn(OpCode.Code.brS, storeResult)
                label(failZero)
                insn(OpCode.Code.ldcI40)
                label(storeResult)
                insn(OpCode.Code.stloc, 2)
                insn(OpCode.Code.ldloc, 2)
                val failBranch = Label()
                insn(OpCode.Code.brfalseS, failBranch)
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
                val endLabel = Label()
                insn(OpCode.Code.brS, endLabel)
                label(failBranch)
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
                label(endLabel)
                insn(OpCode.Code.ret)
            }
        }
    }
    it.toString()
}
