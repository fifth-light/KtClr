package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*

@DslMarker
annotation class AsmDsl

fun ModuleVisitor.write(block: ModuleDslScope.() -> Unit) = ModuleDslScope(this).block()

fun ClassVisitor.write(block: ClassDslScope.() -> Unit) = ClassDslScope(this).block()

fun MethodVisitor.write(block: MethodDslScope.() -> Unit) = MethodDslScope(this).block()
