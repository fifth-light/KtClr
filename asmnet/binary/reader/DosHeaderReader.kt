package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.uint
import top.fifthlight.asmnet.binary.ushort
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun DosHeader(buffer: ByteBuffer): DosHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    require(buf.remaining() >= DosHeader.SIZE) { "Buffer too small for DOS header: ${buf.remaining()} < ${DosHeader.SIZE}" }
    DosHeader(
        e_magic = buf.ushort.also { require(it == DosHeader.MAGIC_MZ) { "Invalid DOS header magic: 0x${it.toString(16)}" } },
        e_cblp = buf.ushort,
        e_cp = buf.ushort,
        e_crlc = buf.ushort,
        e_cparhdr = buf.ushort,
        e_minalloc = buf.ushort,
        e_maxalloc = buf.ushort,
        e_ss = buf.ushort,
        e_sp = buf.ushort,
        e_csum = buf.ushort,
        e_ip = buf.ushort,
        e_cs = buf.ushort,
        e_lfarlc = buf.ushort,
        e_ovno = buf.ushort,
        e_res = UShortArray(4) { buf.ushort },
        e_oemid = buf.ushort,
        e_oeminfo = buf.ushort,
        e_res2 = UShortArray(10) { buf.ushort },
        e_lfanew = buf.uint,
    )
}
