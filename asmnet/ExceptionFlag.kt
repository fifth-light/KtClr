/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

@JvmInline
value class ExceptionFlag(val value: Short) {
    companion object {
        val Exception = ExceptionFlag(0x0000)
        val Filter = ExceptionFlag(0x0001)
        val Finally = ExceptionFlag(0x0002)
        val Fault = ExceptionFlag(0x0004)
    }
}
