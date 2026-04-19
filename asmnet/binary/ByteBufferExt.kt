package top.fifthlight.asmnet.binary

import java.nio.ByteBuffer

fun ByteBuffer.getUShort(): UShort = short.toUShort()

fun ByteBuffer.getUInt(): UInt = int.toUInt()

fun ByteBuffer.getUByte(): UByte = get().toUByte()

fun ByteBuffer.getULong(): ULong = long.toULong()

val ByteBuffer.ushort: UShort get() = getUShort()

val ByteBuffer.uint: UInt get() = getUInt()

val ByteBuffer.ubyte: UByte get() = getUByte()

val ByteBuffer.ulong: ULong get() = getULong()
