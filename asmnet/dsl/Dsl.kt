/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.ClassVisitor
import top.fifthlight.asmnet.MethodVisitor
import top.fifthlight.asmnet.ModuleVisitor

@DslMarker
annotation class AsmDsl

fun ModuleVisitor.write(block: ModuleDslScope.() -> Unit) {
    ModuleDslScope(this).block()
    visitEnd()
}

fun ClassVisitor.write(block: ClassDslScope.() -> Unit) {
    ClassDslScope(this).block()
    visitEnd()
}

fun MethodVisitor.write(block: MethodDslScope.() -> Unit) {
    MethodDslScope(this).block()
    visitEnd()
}
