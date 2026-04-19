/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

data class SourceLineInfo(
    val line: Int,
    val column: Int? = null,
    val filename: String? = null,
)
