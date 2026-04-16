package top.fifthlight.asmnet.il.writer.test.integration

import top.fifthlight.asmnet.ExternAssemblyDeclaration
import top.fifthlight.asmnet.HashAlgorithm
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import top.fifthlight.asmnet.MethodAttributes
import top.fifthlight.asmnet.MethodParameter
import top.fifthlight.asmnet.MethodReference
import top.fifthlight.asmnet.OpCode
import top.fifthlight.asmnet.ResolutionScope
import top.fifthlight.asmnet.Type
import top.fifthlight.asmnet.TypeAttributes
import top.fifthlight.asmnet.TypeReference
import top.fifthlight.asmnet.Version
import top.fifthlight.asmnet.AssemblyDeclaration
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: HelloWorldGenerator <output.il>" }
    val il = generateHelloWorld()
    File(args[0]).writeText(il)
}

private fun generateHelloWorld(): String = StringWriter().use {
    ILTextModuleWriter(it).apply {
        visitExternAssembly(
            name = "System.Runtime",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = byteArrayOf(
                    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
                    0x11, 0xD5.toByte(), 0x0A, 0x3A,
                ),
                version = Version(10, 0, 0, 0),
            ),
        )
        visitExternAssembly(
            name = "System.Console",
            declaration = ExternAssemblyDeclaration(
                publicKeyToken = byteArrayOf(
                    0xB0.toByte(), 0x3F, 0x5F.toByte(), 0x7F.toByte(),
                    0x11, 0xD5.toByte(), 0x0A, 0x3A,
                ),
                version = Version(10, 0, 0, 0),
            ),
        )
        visitAssembly(
            name = "hello_world",
            declaration = AssemblyDeclaration(
                hash = HashAlgorithm.SHA1,
                version = Version(0, 0, 0, 0),
            ),
        )
        visitModule("hello_world.dll", null)
        visitClass("Hello")!!.apply {
            visit(
                attrs = TypeAttributes(
                    TypeAttributes.Public,
                    TypeAttributes.BeforeFieldInit,
                ),
                extends = TypeReference(
                    resolutionScope = ResolutionScope.Assembly("System.Runtime"),
                    name = "System.Object",
                ),
            )
            visitMethod(
                name = "Main",
                attributes = MethodAttributes(
                    MethodAttributes.Public,
                    MethodAttributes.Static,
                    MethodAttributes.HideBySig,
                ),
                parameters = listOf(
                    MethodParameter(
                        type = Type.Array(Type.String),
                        name = "args",
                    ),
                ),
                entryPoint = true,
            )!!.apply {
                visitMaxStack(8)
                visitLdc("Hello, world!")
                visitMethodInsn(
                    opcode = OpCode.CALL,
                    MethodReference(
                        declaringType = TypeReference(
                            resolutionScope = ResolutionScope.Assembly("System.Console"),
                            name = "System.Console",
                        ),
                        name = "WriteLine",
                        returnType = Type.Void,
                        parameterTypes = listOf(Type.String),
                    ),
                )
                visitInsn(OpCode.RET)
                visitEnd()
            }
            visitEnd()
        }
        visitEnd()
    }
    it.toString()
}
