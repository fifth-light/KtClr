/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// PE Format § Section Table - Section Flags
@JvmInline
value class SectionFlags(val value: UInt) {
    val containsCode: Boolean get() = (value and 0x00000020u) != 0u
    val containsInitializedData: Boolean get() = (value and 0x00000040u) != 0u
    val containsUninitializedData: Boolean get() = (value and 0x00000080u) != 0u
    val isDiscardable: Boolean get() = (value and 0x02000000u) != 0u
    val isNotCached: Boolean get() = (value and 0x04000000u) != 0u
    val isNotPaged: Boolean get() = (value and 0x08000000u) != 0u
    val isShared: Boolean get() = (value and 0x10000000u) != 0u
    val isExecute: Boolean get() = (value and 0x20000000u) != 0u
    val isRead: Boolean get() = (value and 0x40000000u) != 0u
    val isWrite: Boolean get() = (value and 0x80000000u) != 0u

    companion object {
        const val CNT_CODE: UInt = 0x00000020u
        const val CNT_INITIALIZED_DATA: UInt = 0x00000040u
        const val CNT_UNINITIALIZED_DATA: UInt = 0x00000080u
        const val MEM_DISCARDABLE: UInt = 0x02000000u
        const val MEM_NOT_CACHED: UInt = 0x04000000u
        const val MEM_NOT_PAGED: UInt = 0x08000000u
        const val MEM_SHARED: UInt = 0x10000000u
        const val MEM_EXECUTE: UInt = 0x20000000u
        const val MEM_READ: UInt = 0x40000000u
        const val MEM_WRITE: UInt = 0x80000000u
    }
}
