/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

data class MetadataRoot(
    val signature: UInt,
    val majorVersion: UShort,
    val minorVersion: UShort,
    val version: String,
    val flags: UShort,
    val streams: List<StreamHeader>,
) {
    companion object {
        const val SIGNATURE = 0x424A5342u
    }
}

data class StreamHeader(
    val offset: UInt,
    val size: UInt,
    val name: String,
)
