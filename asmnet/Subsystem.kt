/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.25.2.3.2
@JvmInline
value class Subsystem(val value: Short) {
    companion object {
        val WINDOWS_GUI = Subsystem(0x0002)
        val WINDOWS_CUI = Subsystem(0x0003)
    }
}
