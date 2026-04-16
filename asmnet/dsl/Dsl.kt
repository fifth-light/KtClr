package top.fifthlight.asmnet.dsl

import top.fifthlight.asmnet.*

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
