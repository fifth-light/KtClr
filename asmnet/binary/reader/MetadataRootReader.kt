/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun MetadataRoot(buffer: ByteBuffer): MetadataRoot = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    require(buf.remaining() >= 16) {
        "Buffer too small for metadata root header: ${buf.remaining()} < 16"
    }

    val signature = buf.uint
    require(signature == MetadataRoot.SIGNATURE) {
        "Invalid metadata root signature: expected 0x${MetadataRoot.SIGNATURE.toString(16)}, got 0x${
            signature.toString(
                16
            )
        }"
    }
    val majorVersion = buf.ushort
    val minorVersion = buf.ushort
    buf.position(buf.position() + 4) // reserved

    val versionLength = buf.uint.toInt()
    require(versionLength % 4 == 0) { "Version string length $versionLength is not multiple of 4" }
    require(versionLength in 0..255) { "Version string length $versionLength not in [0, 255)" }
    require(buf.remaining() >= versionLength) {
        "Buffer too small for version string: remaining ${buf.remaining()} < versionLength $versionLength"
    }

    val stringStartPos = buf.position()
    val version = buf.readString(versionLength, charset = Charsets.UTF_8, requireNullTerminator = true)
    buf.position(stringStartPos + versionLength)

    val flags = buf.ushort
    val streamCount = buf.ushort.toInt()

    val streams = mutableListOf<StreamHeader>()
    repeat(streamCount) {
        val remainingStreams = streamCount - it
        require(buf.remaining() >= remainingStreams * 12) {
            "Buffer too small for $remainingStreams stream headers: minimum required $remainingStreams * 12 bytes, got ${buf.remaining()}"
        }

        val offset = buf.uint
        val size = buf.uint
        val stringStartPos = buf.position()
        val name = buf.readString(32, forceMaxLength = false, requireNullTerminator = true)
        val stringLengthWithNull = buf.position() - stringStartPos
        val stringPaddedLength = ((stringLengthWithNull + 3) / 4) * 4
        buf.position(stringStartPos + stringPaddedLength)
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
