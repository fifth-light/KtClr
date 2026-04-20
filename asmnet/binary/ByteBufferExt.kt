/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

internal fun ByteBuffer.readString(
    maxLength: Int,
    charset: Charset = Charsets.US_ASCII,
    forceMaxLength: Boolean = true,
    requireNullTerminator: Boolean = true,
): String = (0 until maxLength)
    .also {
        if (forceMaxLength) {
            require(remaining() >= maxLength) { "Buffer remaining ${remaining()} too small for string of length $maxLength" }
        }
    }
    .firstOrNull { get(position() + it) == 0.toByte() }
    .let { nullPosition ->
        if (nullPosition != null) {
            slice(position(), nullPosition).also {
                position(position() + nullPosition + 1)
            }
        } else if (!requireNullTerminator) {
            slice(position(), maxLength).also {
                position(position() + maxLength)
            }
        } else {
            error("String exceeds max length $maxLength without null terminator")
        }
    }
    .let { buffer ->
        charset
            .newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
            .decode(buffer).toString()
    }

internal fun ByteBuffer.getUShort(): UShort = short.toUShort()

internal fun ByteBuffer.getUInt(): UInt = int.toUInt()

internal fun ByteBuffer.getUByte(): UByte = get().toUByte()

internal fun ByteBuffer.getULong(): ULong = long.toULong()

internal val ByteBuffer.ushort: UShort
    get() = getUShort()

internal val ByteBuffer.uint: UInt
    get() = getUInt()

internal val ByteBuffer.ubyte: UByte
    get() = getUByte()

internal val ByteBuffer.ulong: ULong
    get() = getULong()
