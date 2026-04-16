package top.fifthlight.asmnet.il.writer.test

import top.fifthlight.asmnet.dsl.ModuleDslScope
import top.fifthlight.asmnet.dsl.write
import top.fifthlight.asmnet.il.writer.ILTextModuleWriter
import java.io.StringWriter
import kotlin.test.assertEquals

fun generateText(block: ModuleDslScope.() -> Unit) = StringWriter().use {
    ILTextModuleWriter(it).write(block)
    it.toString()
}

private val whiteSpaces = Regex("[ \t]+")
private val emptyLines = Regex("(\n([ \t]*\n)*)+")
private val trailingSpaces = Regex("[ \t]+\n")

private fun normalize(string: String) = string
    .replace(trailingSpaces, "\n")
    .replace(whiteSpaces, " ")
    .replace(emptyLines, "\n")
    .trim()

fun assertContentEquals(
    expected: String,
    actual: String,
    message: String? = null,
) = assertEquals(
    expected = normalize(expected),
    actual = normalize(actual),
    message = message,
)
