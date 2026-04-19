/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.1.9
@JvmInline
value class ManifestResourceAttributes(val value: Int) {
    constructor(vararg flags: Int) : this(flags.or())

    val visibility: Int
        get() = value and VisibilityMask

    val isPublic: Boolean
        get() = (value and Public) != 0

    val isPrivate: Boolean
        get() = (value and Private) != 0

    companion object {
        const val VisibilityMask = 0x00000007
        const val Public = 0x00000001
        const val Private = 0x00000002
    }
}
