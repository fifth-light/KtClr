package top.fifthlight.asmnet.il.writer.test.integration.type_insn

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: TypeInsnGenerator <output.il>" }
    val il = generateTypeInsnTest()
    File(args[0]).writeText(il)
}

private val runtimeToken = byteArrayOf(
    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
    0x11, 0xD5.toByte(), 0x0A, 0x3A,
)

private val systemRuntimeInt32 = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.Int32",
)

private val systemRuntimeString = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
    name = "System.String",
)

private val systemConsole = TypeReference(
    resolutionScope = ResolutionScope.Assembly("System.Console"),
    name = "System.Console",
)

private fun generateTypeInsnTest(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        externAssembly("System.Console", declaration = ExternAssemblyDeclaration(
            publicKeyToken = runtimeToken,
            version = Version(10, 0, 0, 0),
        ))
        assembly("type_insn", declaration = AssemblyDeclaration(
            hash = HashAlgorithm.SHA1,
            version = Version(0, 0, 0, 0),
        ))
        module("type_insn.dll")
        class_("TypeTest/Program",
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
                    LocalVariable(Type.Array(Type.Int32), "arr"),
                    LocalVariable(Type.Object, "obj"),
                    LocalVariable(Type.Int32, "unboxed"),
                    LocalVariable(Type.String, "str"),
                    LocalVariable(Type.Object, "obj2"),
                    LocalVariable(Type.String, "casted"),
                )
                maxStack(3)
                code()
                insn(OpCode.Code.ldcI43)
                insn(OpCode.Code.newarr, systemRuntimeInt32)
                insn(OpCode.Code.stloc, 0)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI40)
                ldc(10)
                insn(OpCode.Code.stelemI4)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI41)
                ldc(20)
                insn(OpCode.Code.stelemI4)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI42)
                ldc(30)
                insn(OpCode.Code.stelemI4)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI40)
                insn(OpCode.Code.ldelemI4)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI41)
                insn(OpCode.Code.ldelemI4)
                insn(OpCode.Code.add)
                insn(OpCode.Code.ldloc, 0)
                insn(OpCode.Code.ldcI42)
                insn(OpCode.Code.ldelemI4)
                insn(OpCode.Code.add)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                ldc(42)
                insn(OpCode.Code.box, systemRuntimeInt32)
                insn(OpCode.Code.stloc, 1)
                insn(OpCode.Code.ldloc, 1)
                insn(OpCode.Code.unboxAny, systemRuntimeInt32)
                insn(OpCode.Code.stloc, 2)
                insn(OpCode.Code.ldloc, 2)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
                    name = "WriteLine",
                    returnType = Type.Void,
                    parameterTypes = listOf(Type.Int32),
                ))
                ldc("hello")
                insn(OpCode.Code.stloc, 3)
                insn(OpCode.Code.ldloc, 3)
                insn(OpCode.Code.stloc, 4)
                insn(OpCode.Code.ldloc, 4)
                insn(OpCode.Code.isinst, systemRuntimeString)
                insn(OpCode.Code.stloc, 5)
                insn(OpCode.Code.ldloc, 5)
                insn(OpCode.Code.call, MethodReference(
                    declaringType = systemConsole,
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
