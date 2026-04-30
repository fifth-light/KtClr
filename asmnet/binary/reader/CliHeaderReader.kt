/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.*
import top.fifthlight.asmnet.binary.reader.util.readUIntLE
import top.fifthlight.asmnet.binary.reader.util.readUShortLE

internal fun CliHeader(buffer: ByteBuf): CliHeader = buffer.slice().let { buf ->
    require(buf.readableBytes() >= CliHeader.SIZE) {
        "Buffer too small for CLI header: ${buf.readableBytes()} < ${CliHeader.SIZE}"
    }

    CliHeader(
        cb = buf.readUIntLE(),
        majorRuntimeVersion = buf.readUShortLE(),
        minorRuntimeVersion = buf.readUShortLE(),
        metaData = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        flags = buf.readUIntLE(),
        entryPointToken = buf.readUIntLE(),
        resources = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        strongNameSignature = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        codeManagerTable = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        vTableFixups = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        exportAddressTableJumps = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
        managedNativeHeader = DataDirectory(rva = buf.readUIntLE(), size = buf.readUIntLE()),
    )
}
