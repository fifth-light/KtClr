package top.fifthlight.asmnet

internal infix fun Short.and(other: Short) = (this.toInt() and other.toInt()).toShort()

internal infix fun Short.or(other: Short) = (this.toInt() or other.toInt()).toShort()

internal fun ShortArray.or() = fold(0.toShort()) { acc, i -> acc or i }

internal fun IntArray.or() = fold(0) { acc, i -> acc or i }
