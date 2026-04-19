/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// PE Format § Section Table
data class SectionHeader(
    val name: String,
    val virtualSize: UInt,
    val virtualAddress: UInt,
    val sizeOfRawData: UInt,
    val pointerToRawData: UInt,
    val pointerToRelocations: UInt,
    val pointerToLinenumbers: UInt,
    val numberOfRelocations: UShort,
    val numberOfLinenumbers: UShort,
    val characteristics: SectionFlags,
) {
    companion object {
        const val SIZE = 40
    }
}
