package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.ModuleVisitor
import java.nio.ByteBuffer

class ILBinaryReader(private val bytes: ByteBuffer) {
    fun accept(visitor: ModuleVisitor) {
        val dosHeader = DosHeader(bytes)
        val lfanew = dosHeader.e_lfanew
        require(lfanew <= Int.MAX_VALUE.toUInt()) {
            "e_lfanew (0x${lfanew.toString(16)}) exceeds Int.MAX_VALUE"
        }
        require(lfanew.toInt() <= bytes.capacity() - 20) {
            "e_lfanew (0x${lfanew.toString(16)}) out of bounds for buffer capacity ${bytes.capacity()}"
        }
        bytes.position(lfanew.toInt())
        val coffHeader = CoffHeader(bytes)
        TODO("Not yet implemented")
    }
}
