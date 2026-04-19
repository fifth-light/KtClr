/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// PE Format § Optional Header Data Directories
data class DataDirectory(
    val rva: UInt,
    val size: UInt,
)
