/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// ECMA-335 II.25.2.2, PE Format § COFF File Header
data class CoffHeader(
    val machine: MachineType,
    val numberOfSections: UShort,
    val timeDateStamp: UInt,
    val pointerToSymbolTable: UInt,
    val numberOfSymbols: UInt,
    val sizeOfOptionalHeader: UShort,
    val characteristics: ImageCharacteristics,
) {
    companion object {
        const val SIZE = 20
    }
}
