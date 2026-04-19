/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

// ECMA-335 II.23.1.2
@JvmInline
value class AssemblyFlags(val value: Int) {
    constructor(vararg flags: Int) : this(flags.fold(0) { acc, flag -> acc or flag })

    val publicKey: Boolean
        get() = (value and PublicKey) != 0

    val retargetable: Boolean
        get() = (value and Retargetable) != 0

    val disableJITcompileOptimizer: Boolean
        get() = (value and DisableJITcompileOptimizer) != 0

    val enableJITcompileTracking: Boolean
        get() = (value and EnableJITcompileTracking) != 0

    companion object {
        const val PublicKey: Int = 0x0001
        const val Retargetable: Int = 0x0100
        const val DisableJITcompileOptimizer: Int = 0x4000
        const val EnableJITcompileTracking: Int = 0x8000
    }
}
