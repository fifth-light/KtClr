/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet

internal infix fun Byte.or(other: Byte) = (this.toInt() or other.toInt()).toByte()

internal fun ByteArray.or() = fold(0.toByte()) { acc, i -> acc or i }

internal infix fun Short.and(other: Short) = (this.toInt() and other.toInt()).toShort()

internal infix fun Short.or(other: Short) = (this.toInt() or other.toInt()).toShort()

internal fun ShortArray.or() = fold(0.toShort()) { acc, i -> acc or i }

internal fun IntArray.or() = fold(0) { acc, i -> acc or i }
