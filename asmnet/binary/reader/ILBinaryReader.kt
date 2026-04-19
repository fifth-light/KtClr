package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.ModuleVisitor
import top.fifthlight.asmnet.binary.reader.OptionalHeader
import java.nio.ByteBuffer

class ILBinaryReader(private val bytes: ByteBuffer) {
    fun accept(visitor: ModuleVisitor) {
        val dosHeader = DosHeader(bytes)
        val lfanew = dosHeader.e_lfanew
        require(lfanew <= Int.MAX_VALUE.toUInt()) {
            "e_lfanew (0x${lfanew.toString(16)}) exceeds Int.MAX_VALUE"
        }
        val peOffset = lfanew.toInt()
        require(peOffset + 4 + CoffHeader.SIZE <= bytes.capacity()) {
            "PE headers at offset 0x${lfanew.toString(16)} out of bounds for buffer capacity ${bytes.capacity()}"
        }
        val coffHeader = CoffHeader(bytes.slice(peOffset + 4, CoffHeader.SIZE))
        TODO("Not yet implemented")
    }
}
