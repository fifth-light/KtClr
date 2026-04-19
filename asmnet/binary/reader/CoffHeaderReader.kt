package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.ImageCharacteristics
import top.fifthlight.asmnet.binary.MachineType
import top.fifthlight.asmnet.binary.uint
import top.fifthlight.asmnet.binary.ushort
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal fun CoffHeader(buffer: ByteBuffer): CoffHeader = buffer.slice().order(ByteOrder.LITTLE_ENDIAN).let { buf ->
    require(buf.remaining() >= CoffHeader.SIZE) { "Buffer too small for COFF header: ${buf.remaining()} < ${CoffHeader.SIZE}" }
    CoffHeader(
        machine = MachineType(buf.ushort),
        numberOfSections = buf.ushort,
        timeDateStamp = buf.uint,
        pointerToSymbolTable = buf.uint,
        numberOfSymbols = buf.uint,
        sizeOfOptionalHeader = buf.ushort,
        characteristics = ImageCharacteristics(buf.ushort),
    )
}
