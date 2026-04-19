/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// PE Format § COFF File Header - Characteristics
@JvmInline
value class ImageCharacteristics(val value: UShort) {
    constructor(vararg flags: UShort) : this(flags.or())

    val isRelocsStripped: Boolean get() = (value.toInt() and 0x0001) != 0
    val isExecutableImage: Boolean get() = (value.toInt() and 0x0002) != 0
    val isLineNumsStripped: Boolean get() = (value.toInt() and 0x0004) != 0
    val isLocalSymsStripped: Boolean get() = (value.toInt() and 0x0008) != 0
    val isAggressiveWsTrim: Boolean get() = (value.toInt() and 0x0010) != 0
    val isLargeAddressAware: Boolean get() = (value.toInt() and 0x0020) != 0
    val is32BitMachine: Boolean get() = (value.toInt() and 0x0100) != 0
    val isDebugStripped: Boolean get() = (value.toInt() and 0x0200) != 0
    val isRemovableRunFromSwap: Boolean get() = (value.toInt() and 0x0400) != 0
    val isNetRunFromSwap: Boolean get() = (value.toInt() and 0x0800) != 0
    val isSystem: Boolean get() = (value.toInt() and 0x1000) != 0
    val isDll: Boolean get() = (value.toInt() and 0x2000) != 0
    val isUpSystemOnly: Boolean get() = (value.toInt() and 0x4000) != 0

    companion object {
        const val RELOCS_STRIPPED: UShort = 0x0001u
        const val EXECUTABLE_IMAGE: UShort = 0x0002u
        const val LINE_NUMS_STRIPPED: UShort = 0x0004u
        const val LOCAL_SYMS_STRIPPED: UShort = 0x0008u
        const val AGGRESSIVE_WS_TRIM: UShort = 0x0010u
        const val LARGE_ADDRESS_AWARE: UShort = 0x0020u
        const val BYTES_REVERSED_LO: UShort = 0x0080u
        const val BIT_32_MACHINE: UShort = 0x0100u
        const val DEBUG_STRIPPED: UShort = 0x0200u
        const val REMOVABLE_RUN_FROM_SWAP: UShort = 0x0400u
        const val NET_RUN_FROM_SWAP: UShort = 0x0800u
        const val SYSTEM: UShort = 0x1000u
        const val DLL: UShort = 0x2000u
        const val UP_SYSTEM_ONLY: UShort = 0x4000u
        const val BYTES_REVERSED_HI: UShort = 0x8000u
    }
}
