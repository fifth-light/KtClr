/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.DataLabel

class DataLabelRegistry {
    private val labels = mutableMapOf<DataLabel, Int>()

    fun getOrCreateLabelIndex(label: DataLabel?): Int? {
        if (label == null) return null
        return labels.getOrPut(label) { labels.size }
    }
}
