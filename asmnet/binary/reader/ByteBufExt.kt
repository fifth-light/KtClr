/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import java.nio.charset.Charset

internal fun ByteBuf.readString(
    maxLength: Int,
    charset: Charset = Charsets.US_ASCII,
    forceMaxLength: Boolean = true,
    requireNullTerminator: Boolean = true,
): String = (0 until maxLength)
    .also {
        if (forceMaxLength) {
            require(readableBytes() >= maxLength) { "Buffer remaining ${readableBytes()} too small for string of length $maxLength" }
        }
    }
    .firstOrNull { getByte(readerIndex() + it) == 0.toByte() }
    .let { nullPosition ->
        if (nullPosition != null) {
            slice(readerIndex(), nullPosition).also {
                readerIndex(readerIndex() + nullPosition + 1)
            }
        } else if (!requireNullTerminator) {
            slice(readerIndex(), maxLength).also {
                readerIndex(readerIndex() + maxLength)
            }
        } else {
            error("String exceeds max length $maxLength without null terminator")
        }
    }
    .let { buffer ->
        buffer.toString(charset)
    }

internal fun ByteBuf.readUShortLE(): UShort = readShortLE().toUShort()

internal fun ByteBuf.readUIntLE(): UInt = readIntLE().toUInt()

internal fun ByteBuf.readUByteLE(): UByte = readByte().toUByte()

internal fun ByteBuf.readULongLE(): ULong = readLongLE().toULong()
