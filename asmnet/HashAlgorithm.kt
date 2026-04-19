/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.6.2.1.1, with extensions in System.Reflection.Metadata
enum class HashAlgorithm(val value: Int) {
    MD5(0x00008003),
    SHA1(0x00008004),
    SHA256(0x0000800c),
    SHA384(0x0000800d),
    SHA512(0x0000800e),
}