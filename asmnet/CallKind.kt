/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.15.3
sealed interface CallKind {
    // ECMA-335 II.23.2.1
    data class Managed(
        val vararg: Boolean = false,
    ) : CallKind {
        override fun toString() = if (vararg) "vararg" else "default"
    }

    enum class Unmanaged(val assemblyName: String) : CallKind {
        Cdecl("unmanaged cdecl"),
        Fastcall("unmanaged fastcall"),
        Stdcall("unmanaged stdcall"),
        Thiscall("unmanaged thiscall");

        override fun toString() = assemblyName
    }
}