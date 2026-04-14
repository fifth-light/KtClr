package top.fifthlight.asmnet

internal infix fun Short.and(other: Short) = (this.toInt() and other.toInt()).toShort()
