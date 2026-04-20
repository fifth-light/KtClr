/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.binary

// ECMA-335 II.25.3.3
data class CliHeader(
    val cb: UInt,
    val majorRuntimeVersion: UShort,
    val minorRuntimeVersion: UShort,
    val metaData: DataDirectory,
    val flags: UInt,
    val entryPointToken: UInt,
    val resources: DataDirectory,
    val strongNameSignature: DataDirectory,
    val codeManagerTable: DataDirectory,
    val vTableFixups: DataDirectory,
    val exportAddressTableJumps: DataDirectory,
    val managedNativeHeader: DataDirectory,
) {
    companion object {
        const val SIZE = 72
    }
}
