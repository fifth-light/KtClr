/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.1.14
@JvmInline
value class PropertyAttributes(val value: Short) {
    constructor(vararg flags: Short) : this(flags.or())

    val specialName: Boolean
        get() = (value and SpecialName) != 0.toShort()
    val rtSpecialName: Boolean
        get() = (value and RTSpecialName) != 0.toShort()
    val hasDefault: Boolean
        get() = (value and HasDefault) != 0.toShort()

    companion object {
        const val SpecialName: Short = 0x0200
        const val RTSpecialName: Short = 0x0400
        const val HasDefault: Short = 0x1000
        const val Unused: Short = 0xE9ff.toShort()
    }
}
