/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

data class FieldReference(
    val declaringType: TypeSpec,
    val name: String,
    val fieldType: TypeSpec,
) {
    override fun toString() = buildString {
        append(fieldType)
        append(' ')
        append(declaringType)
        append("::$name")
    }
}
