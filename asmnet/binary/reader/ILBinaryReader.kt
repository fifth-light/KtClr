/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary.reader

import top.fifthlight.asmnet.ModuleVisitor
import top.fifthlight.asmnet.binary.CoffHeader
import top.fifthlight.asmnet.binary.DosHeader
import top.fifthlight.asmnet.binary.PeSignature
import top.fifthlight.asmnet.binary.SectionHeader
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

        require(optionalHeader.dataDirectories.size >= 15) {
            "Specified file is not a .NET module: there are only ${optionalHeader.dataDirectories.size} data directories"
        }

        offset += coffHeader.sizeOfOptionalHeader.toInt()

        val sections = mutableListOf<SectionHeader>()
        repeat(coffHeader.numberOfSections.toInt()) {
            require(bytes.remaining() - offset >= SectionHeader.SIZE) {
                "Section header at offset 0x${offset.toString(16)} out of bounds for buffer capacity ${bytes.remaining()}"
            }
            sections.add(SectionHeader(bytes.slice(offset, SectionHeader.SIZE)))
            offset += SectionHeader.SIZE
        }

        visitor.visitImageBase(optionalHeader.imageBase)
        visitor.visitFileAlignment(optionalHeader.fileAlignment)
        visitor.visitStackReserve(optionalHeader.sizeOfStackReserve)
        visitor.visitSubsystem(optionalHeader.subsystem)

        TODO("Not yet implemented")
    }
}
