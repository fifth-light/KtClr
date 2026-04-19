package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.PeSignature
import top.fifthlight.asmnet.ModuleVisitor
import top.fifthlight.asmnet.binary.OptionalHeader
import java.nio.ByteBuffer

class ILBinaryReader(private val bytes: ByteBuffer) {
    fun accept(visitor: ModuleVisitor) {
        var offset = 0

        val dosHeader = DosHeader(bytes.slice(offset, DosHeader.SIZE))

        val lfanew = dosHeader.e_lfanew
        require(lfanew <= Int.MAX_VALUE.toUInt()) {
            "e_lfanew (0x${lfanew.toString(16)}) exceeds Int.MAX_VALUE"
        }
        offset = lfanew.toInt()

        require(offset + PeSignature.SIZE + CoffHeader.SIZE <= bytes.remaining()) {
            "PE headers at offset 0x${offset.toString(16)} out of bounds for buffer capacity ${bytes.remaining()}"
        }
        val coffHeader = PeHeader(bytes.slice(offset, PeSignature.SIZE + CoffHeader.SIZE))
        offset += PeSignature.SIZE + CoffHeader.SIZE

        require(bytes.remaining() - offset >= 2) {
            "Optional header at offset 0x${offset.toString(16)} out of bounds for buffer capacity ${bytes.remaining()}"
        }
        val optionalHeader = OptionalHeader(bytes.slice(offset, bytes.limit() - offset))

        TODO("Not yet implemented")
    }
}
