/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test.integration.type_forwarder

import top.fifthlight.asmnet.*
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.File
import java.io.StringWriter

fun main(args: Array<String>) {
    require(args.size == 1) { "Usage: ForwarderGenerator <output.il>" }
    File(args[0]).writeText(generateForwarder())
}

private fun generateForwarder(): String = StringWriter().use {
    ILTextModuleWriter(it).write {
        externAssembly("System.Runtime")
        externAssembly("Lib")
        assembly("Forwarder")
        module("Forwarder.dll")

        typeForwarder("MyHelper") {
            externAssembly("Lib")
        }

        exportedType(
            name = "NestedHelper",
            flags = TypeAttributes(TypeAttributes.NestedPublic),
        ) {
            parentType("MyHelper")
        }
    }
    it.toString()
}
