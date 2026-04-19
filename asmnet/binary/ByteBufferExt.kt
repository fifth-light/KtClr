package top.fifthlight.asmnet.binary

import java.nio.ByteBuffer

fun ByteBuffer.getUShort(): UShort = short.toUShort()

fun ByteBuffer.getUInt(): UInt = int.toUInt()

val ByteBuffer.ushort: UShort get() = getUShort()

val ByteBuffer.uint: UInt get() = getUInt()
