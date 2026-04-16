package top.fifthlight.asmnet.il.writer

import java.io.Writer
import java.util.*

class TextWriter(
    private val writer: Writer,
    private val indent: String = "  ",
) : AutoCloseable {
    companion object {
        private val acceptableIdentifierBeginning = setOf(
            '_', '$', "@", "`", "?", '.'
        ) + ('A' until 'Z') + ('a' until 'z')

        private val acceptableIdentifierChars = setOf(
            '_', '$', "@", "`", "?", '.'
        ) + ('0' until '9') + ('A' until 'Z') + ('a' until 'z')
    }

    private var indentLevel = 0

    fun write(text: Char) = writer.write(text.code)
    fun write(text: String) = writer.write(text)

    fun indent() {
        indentLevel++
    }

    fun unindent() {
        indentLevel--
    }

    fun newLine() {
        write("\n")
        repeat(indentLevel) {
            write(indent)
        }
    }

    fun writeHex(int8: Byte) = write(String.format("0x%02X", int8))
    fun writeHex(int16: Short) = write(String.format("0x%04X", int16))
    fun writeHex(int32: Int) = write(String.format("0x%08X", int32))
    fun writeHex(int64: Long) = write(String.format("0x%016X", int64))
    fun writeHex(int8: UByte) = write(String.format("0x%02X", int8.toInt()))
    fun writeHex(int16: UShort) = write(String.format("0x%04X", int16.toInt()))
    fun writeHex(int32: UInt) = write(String.format("0x%08X", int32.toLong()))
    fun writeHex(int64: ULong) = write(String.format("0x%016X", int64.toLong()))
    fun writeHex(bytes: ByteArray) = bytes.forEachIndexed { index, byte ->
        write(String.format("%02X", byte))
        if (index < bytes.size - 1) {
            write(' ')
        }
    }

    fun writeGuid(guid: UUID) {
        write('{')
        write(guid.toString())
        write('}')
    }

    fun writeQuotedString(text: String, singleQuote: Boolean) {
        val quote = if (singleQuote) '\'' else '"'
        write(quote)
        for (c in text) {
            when (c) {
                '"' -> write("\\\"")
                '\\' -> write("\\\\")
                '\n' -> write("\\n")
                '\t' -> write("\\t")
                else -> write(c)
            }
        }
        write(quote)
    }

    fun writeIdentifier(identifier: String) {
        val isValidIdentifier = identifier.first() in acceptableIdentifierBeginning &&
                identifier.all { it in acceptableIdentifierChars }
        if (isValidIdentifier) {
            write(identifier)
        } else {
            writeQuotedString(identifier, true)
        }
    }

    fun writeln(text: String) {
        writer.write(text)
        newLine()
    }

    fun writeComment(text: String) {
        write("// ")
        write(text)
        newLine()
    }

    override fun close() = writer.close()
}
