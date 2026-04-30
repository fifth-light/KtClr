/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.util.readString
import top.fifthlight.asmnet.binary.reader.util.readUIntLE
import top.fifthlight.asmnet.binary.reader.util.readUShortLE

internal fun MetadataRoot(buffer: ByteBuf): MetadataRoot = buffer.slice().let { buf ->
    require(buf.readableBytes() >= 16) {
        "Buffer too small for metadata root header: ${buf.readableBytes()} < 16"
    }

    val signature = buf.readUIntLE()
    require(signature == MetadataRoot.SIGNATURE) {
        "Invalid metadata root signature: expected 0x${MetadataRoot.SIGNATURE.toString(16)}, got 0x${
            signature.toString(
                16
            )
        }"
    }
    val majorVersion = buf.readUShortLE()
    val minorVersion = buf.readUShortLE()
    buf.readerIndex(buf.readerIndex() + 4) // reserved

    val versionLength = buf.readUIntLE().toInt()
    require(versionLength % 4 == 0) { "Version string length $versionLength is not multiple of 4" }
    require(versionLength in 0..255) { "Version string length $versionLength not in [0, 255)" }
    require(buf.readableBytes() >= versionLength) {
        "Buffer too small for version string: remaining ${buf.readableBytes()} < versionLength $versionLength"
    }

    val stringStartPos = buf.readerIndex()
    val version = buf.readString(versionLength, charset = Charsets.UTF_8, requireNullTerminator = true)
    buf.readerIndex(stringStartPos + versionLength)

    val flags = buf.readUShortLE()
    val streamCount = buf.readUShortLE().toInt()

    val streams = mutableListOf<StreamHeader>()
    repeat(streamCount) {
        val remainingStreams = streamCount - it
        require(buf.readableBytes() >= remainingStreams * 12) {
            "Buffer too small for $remainingStreams stream headers: minimum required $remainingStreams * 12 bytes, got ${buf.readableBytes()}"
        }

        val offset = buf.readUIntLE()
        val size = buf.readUIntLE()
        val stringStartPos = buf.readerIndex()
        val name = buf.readString(32, forceMaxLength = false, requireNullTerminator = true)
        val stringLengthWithNull = buf.readerIndex() - stringStartPos
        val stringPaddedLength = ((stringLengthWithNull + 3) / 4) * 4
        buf.readerIndex(stringStartPos + stringPaddedLength)
        streams.add(StreamHeader(offset = offset, size = size, name = name))
    }

    MetadataRoot(
        signature = signature,
        majorVersion = majorVersion,
        minorVersion = minorVersion,
        version = version,
        flags = flags,
        streams = streams,
    )
}
