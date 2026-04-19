/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.AssemblyDeclaration
import top.fifthlight.asmnet.ExternAssemblyDeclaration
import top.fifthlight.asmnet.HashAlgorithm
import top.fifthlight.asmnet.Version

class AssemblyTest {
    @Test
    fun testSimpleAssembly() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = null,
                    culture = null,
                    publicKey = null,
                    version = null,
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithHash() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                  .hash algorithm 0x0000800C
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = HashAlgorithm.SHA256,
                    culture = null,
                    publicKey = null,
                    version = null,
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithCulture() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                  .culture "en-US"
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = null,
                    culture = "en-US",
                    publicKey = null,
                    version = null,
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithPublicKey() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                  .publickey = 00 01 02 AB CD
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = null,
                    culture = null,
                    publicKey = byteArrayOf(0x00, 0x01, 0x02, 0xAB.toByte(), 0xCD.toByte()),
                    version = null,
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithVersion() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                  .ver 1:2:3:4
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = null,
                    culture = null,
                    publicKey = null,
                    version = Version(1, 2, 3, 4),
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithAllFields() {
        assertContentEquals(
            expected = """
                .assembly name
                {
                  .hash algorithm 0x00008004
                  .culture "en-US"
                  .publickey = 00 01 02 AB CD
                  .ver 1:2:3:4
                }
            """.trimIndent(),
            actual = generateText {
                assembly("name", declaration = AssemblyDeclaration(
                    hash = HashAlgorithm.SHA1,
                    culture = "en-US",
                    publicKey = byteArrayOf(0x00, 0x01, 0x02, 0xAB.toByte(), 0xCD.toByte()),
                    version = Version(1, 2, 3, 4),
                ))
            }
        )
    }

    @Test
    fun testAssemblyWithSpecialName() {
        assertContentEquals(
            expected = """
                .assembly 'my-assembly'
                {
                }
            """.trimIndent(),
            actual = generateText {
                assembly("my-assembly")
            }
        )
    }

    @Test
    fun testSimpleExternAssembly() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name")
            }
        )
    }

    @Test
    fun testExternAssemblyWithCulture() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                  .culture "en-US"
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name", declaration = ExternAssemblyDeclaration(
                    culture = "en-US",
                ))
            }
        )
    }

    @Test
    fun testExternAssemblyWithPublicKeyToken() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                  .publickeytoken = ( 00 01 02 AB CD )
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name", declaration = ExternAssemblyDeclaration(
                    publicKeyToken = byteArrayOf(0x00, 0x01, 0x02, 0xAB.toByte(), 0xCD.toByte()),
                ))
            }
        )
    }

    @Test
    fun testExternAssemblyWithPublicKey() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                  .publickey = ( 00 01 02 AB CD )
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name", declaration = ExternAssemblyDeclaration(
                    publicKey = byteArrayOf(0x00, 0x01, 0x02, 0xAB.toByte(), 0xCD.toByte()),
                ))
            }
        )
    }

    @Test
    fun testExternAssemblyWithVersion() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                  .ver 4:0:0:0
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name", declaration = ExternAssemblyDeclaration(
                    version = Version(4, 0, 0, 0),
                ))
            }
        )
    }

    @Test
    fun testExternAssemblyWithAllFields() {
        assertContentEquals(
            expected = """
                .assembly extern name
                {
                  .culture "en-US"
                  .publickeytoken = ( 31 24 6B FD )
                  .publickey = ( 00 01 02 AB CD )
                  .ver 4:0:0:0
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("name", declaration = ExternAssemblyDeclaration(
                    culture = "en-US",
                    publicKeyToken = byteArrayOf(0x31, 0x24, 0x6B.toByte(), 0xFD.toByte()),
                    publicKey = byteArrayOf(0x00, 0x01, 0x02, 0xAB.toByte(), 0xCD.toByte()),
                    version = Version(4, 0, 0, 0),
                ))
            }
        )
    }

    @Test
    fun testExternAssemblyWithSpecialName() {
        assertContentEquals(
            expected = """
                .assembly extern 'my-assembly'
                {
                }
            """.trimIndent(),
            actual = generateText {
                externAssembly("my-assembly")
            }
        )
    }

    @Test
    fun testFile() {
        assertContentEquals(
            expected = """
                .file myfile .hash = (AB CD EF 12 34)
            """.trimIndent(),
            actual = generateText {
                file("myfile", byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(), 0x12, 0x34))
            }
        )
    }

    @Test
    fun testFileNoMetadata() {
        assertContentEquals(
            expected = """
                .file nometadata myfile .hash = (AB CD EF 12 34)
            """.trimIndent(),
            actual = generateText {
                file("myfile", byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(), 0x12, 0x34), noMetadata = true)
            }
        )
    }

    @Test
    fun testFileWithEntryPoint() {
        assertContentEquals(
            expected = """
                .file myfile .hash = (AB CD EF 12 34) .entrypoint
            """.trimIndent(),
            actual = generateText {
                file("myfile", byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(), 0x12, 0x34), entryPoint = true)
            }
        )
    }

    @Test
    fun testFileAllOptions() {
        assertContentEquals(
            expected = """
                .file nometadata myfile .hash = (AB CD EF 12 34) .entrypoint
            """.trimIndent(),
            actual = generateText {
                file("myfile", byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(), 0x12, 0x34), noMetadata = true, entryPoint = true)
            }
        )
    }

    @Test
    fun testFileWithSpecialName() {
        assertContentEquals(
            expected = """
                .file 'my-file' .hash = (AB CD)
            """.trimIndent(),
            actual = generateText {
                file("my-file", byteArrayOf(0xAB.toByte(), 0xCD.toByte()))
            }
        )
    }
}
