/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.15.3
data class CallConv(
    val instance: Boolean = false,
    val explicit: Boolean = false,
    val callKind: CallKind = CallKind.Managed(),
) {
    override fun toString() = buildString {
        if (instance) {
            append("instance ")
        }
        if (explicit) {
            append("explicit ")
        }
        append(callKind)
    }
}