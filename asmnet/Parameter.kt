/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

data class Parameter(
    val type: TypeSpec,
    val name: String? = null,
    val flags: ParamAttributes = ParamAttributes(),
    val marshal: NativeType? = null,
)
