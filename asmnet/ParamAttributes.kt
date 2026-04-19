/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.1.13
@JvmInline
value class ParamAttributes(val value: Short) {
    constructor(vararg flags: Short): this(flags.or())

    val `in`: Boolean
        get() = (value and In) != 0.toShort()

    val out: Boolean
        get() = (value and Out) != 0.toShort()

    val optional: Boolean
        get() = (value and Optional) != 0.toShort()

    val hasDefault: Boolean
        get() = (value and HasDefault) != 0.toShort()

    val hasFieldMarshal: Boolean
        get() = (value and HasFieldMarshal) != 0.toShort()

    companion object {
        const val In: Short = 0x0001
        const val Out: Short = 0x0002
        const val Optional: Short = 0x0010
        const val HasDefault: Short = 0x1000
        const val HasFieldMarshal: Short = 0x2000
    }
}