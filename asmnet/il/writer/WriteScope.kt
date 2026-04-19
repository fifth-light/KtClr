/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer

import java.util.*

@WriterDsl
interface WriteScope {
    fun indent()
    fun unindent()

    fun write(text: String)
    fun write(text: Char)

    fun num(int8: Byte)
    fun num(int16: Short)
    fun num(int32: Int)
    fun num(int64: Long)
    fun num(int8: UByte)
    fun num(int16: UShort)
    fun num(int32: UInt)
    fun num(int64: ULong)

    fun hex(int8: Byte)
    fun hex(int16: Short)
    fun hex(int32: Int)
    fun hex(int64: Long)
    fun hex(int8: UByte)
    fun hex(int16: UShort)
    fun hex(int32: UInt)
    fun hex(int64: ULong)
    fun hex(bytes: ByteArray)

    fun guid(guid: UUID)

    fun line()
    fun line(text: String)

    fun quoted(text: String)
    fun singleQuoted(text: String)

    fun identifier(identifier: String)
    fun comment(text: String)

    operator fun Char.unaryPlus() = write(this)
    operator fun String.unaryPlus() = write(this)
}

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class WriterDsl

@JvmInline
value class WriteScopeImpl(private val writer: TextWriter) : WriteScope {
    override fun indent() = writer.indent()
    override fun unindent() = writer.unindent()

    override fun write(text: String) = writer.write(text)
    override fun write(text: Char) = writer.write(text)

    override fun num(int8: Byte) = writer.write(int8)
    override fun num(int16: Short) = writer.write(int16)
    override fun num(int32: Int) = writer.write(int32)
    override fun num(int64: Long) = writer.write(int64)
    override fun num(int8: UByte) = writer.write(int8)
    override fun num(int16: UShort) = writer.write(int16)
    override fun num(int32: UInt) = writer.write(int32)
    override fun num(int64: ULong) = writer.write(int64)

    override fun hex(int8: Byte) = writer.writeHex(int8)
    override fun hex(int16: Short) = writer.writeHex(int16)
    override fun hex(int32: Int) = writer.writeHex(int32)
    override fun hex(int64: Long) = writer.writeHex(int64)
    override fun hex(int8: UByte) = writer.writeHex(int8)
    override fun hex(int16: UShort) = writer.writeHex(int16)
    override fun hex(int32: UInt) = writer.writeHex(int32)
    override fun hex(int64: ULong) = writer.writeHex(int64)
    override fun hex(bytes: ByteArray) = writer.writeHex(bytes)

    override fun guid(guid: UUID) = writer.writeGuid(guid)

    override fun line() = writer.newLine()
    override fun line(text: String) = writer.writeln(text)

    override fun quoted(text: String) = writer.writeQuotedString(text, singleQuote = false)
    override fun singleQuoted(text: String) = writer.writeQuotedString(text, singleQuote = true)

    override fun identifier(identifier: String) = writer.writeIdentifier(identifier)
    override fun comment(text: String) = writer.writeComment(text)
}

inline fun TextWriter.write(crossinline block: WriteScope.() -> Unit) = WriteScopeImpl(this).block()

inline fun WriteScope.block(crossinline block: WriteScope.() -> Unit) {
    +"{"
    indent()
    line()
    block()
    unindent()
    line()
    +"}"
    line()
}
