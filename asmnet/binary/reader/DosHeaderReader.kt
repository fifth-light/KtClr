/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import io.netty.buffer.ByteBuf
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.reader.util.readUIntLE
import top.fifthlight.asmnet.binary.reader.util.readUShortLE

internal fun DosHeader(buffer: ByteBuf): DosHeader = buffer.slice().let { buf ->
    require(buf.readableBytes() >= DosHeader.SIZE) { "Buffer too small for DOS header: ${buf.readableBytes()} < ${DosHeader.SIZE}" }
    DosHeader(
        e_magic = buf.readUShortLE().also { require(it == DosHeader.MAGIC_MZ) { "Invalid DOS header magic: 0x${it.toString(16)}" } },
        e_cblp = buf.readUShortLE(),
        e_cp = buf.readUShortLE(),
        e_crlc = buf.readUShortLE(),
        e_cparhdr = buf.readUShortLE(),
        e_minalloc = buf.readUShortLE(),
        e_maxalloc = buf.readUShortLE(),
        e_ss = buf.readUShortLE(),
        e_sp = buf.readUShortLE(),
        e_csum = buf.readUShortLE(),
        e_ip = buf.readUShortLE(),
        e_cs = buf.readUShortLE(),
        e_lfarlc = buf.readUShortLE(),
        e_ovno = buf.readUShortLE(),
        e_res = UShortArray(4) { buf.readUShortLE() },
        e_oemid = buf.readUShortLE(),
        e_oeminfo = buf.readUShortLE(),
        e_res2 = UShortArray(10) { buf.readUShortLE() },
        e_lfanew = buf.readUIntLE(),
    )
}
