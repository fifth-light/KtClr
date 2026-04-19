/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.6.2.1.4
data class Version(
    val major: Int,
    val minor: Int,
    val build: Int,
    val revision: Int,
) {
    override fun toString() = "$major:$minor:$build:$revision"
}
