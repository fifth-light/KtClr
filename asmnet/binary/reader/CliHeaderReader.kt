/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.CliHeader
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun CliHeader(buffer: ByteBuffer): CliHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    require(buf.remaining() >= CliHeader.SIZE) {
        "Buffer too small for CLI header: ${buf.remaining()} < ${CliHeader.SIZE}"
    }

    CliHeader(
        cb = buf.uint,
        majorRuntimeVersion = buf.ushort,
        minorRuntimeVersion = buf.ushort,
        metaData = DataDirectory(rva = buf.uint, size = buf.uint),
        flags = buf.uint,
        entryPointToken = buf.uint,
        resources = DataDirectory(rva = buf.uint, size = buf.uint),
        strongNameSignature = DataDirectory(rva = buf.uint, size = buf.uint),
        codeManagerTable = DataDirectory(rva = buf.uint, size = buf.uint),
        vTableFixups = DataDirectory(rva = buf.uint, size = buf.uint),
        exportAddressTableJumps = DataDirectory(rva = buf.uint, size = buf.uint),
        managedNativeHeader = DataDirectory(rva = buf.uint, size = buf.uint),
    )
}
