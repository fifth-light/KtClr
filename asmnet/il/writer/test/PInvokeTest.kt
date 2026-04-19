/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.asmnet.il.writer.test

import org.junit.Test
import top.fifthlight.asmnet.*

class PInvokeTest {
    @Test
    fun testPInvokeWithStdCall() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" stdcall) unsigned int32 GetCurrentProcessId() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetCurrentProcessId",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.UnsignedInt32,
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithCdecl() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("mylib.dll" cdecl) void MyFunc() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MyFunc",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "mylib.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvCdecl),
                        ),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithThisCall() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("mylib.dll" thiscall) void MyFunc() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MyFunc",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "mylib.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvThisCall),
                        ),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithFastCall() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("mylib.dll" fastcall) void MyFunc() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MyFunc",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "mylib.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvFastCall),
                        ),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithPlatformApi() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("mylib.dll" winapi) void MyFunc() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MyFunc",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "mylib.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvPlatformApi),
                        ),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithMethodAlias() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" as "GetCurrentProcessId" stdcall) unsigned int32 GetPID() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetPID",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            methodName = "GetCurrentProcessId",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.UnsignedInt32,
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithAnsi() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("user32.dll" ansi stdcall) bool MessageBeep(unsigned int32) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MessageBeep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "user32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.CharSetAnsi,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(Parameter(Type.UnsignedInt32)),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithUnicode() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("user32.dll" unicode stdcall) bool MessageBeep(unsigned int32) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MessageBeep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "user32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.CharSetUnicode,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(Parameter(Type.UnsignedInt32)),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithAutoChar() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("user32.dll" autochar stdcall) bool MessageBeep(unsigned int32) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MessageBeep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "user32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.CharSetAuto,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(Parameter(Type.UnsignedInt32)),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithNoMangle() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("mylib.dll" nomangle stdcall) void MyFunc() cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MyFunc",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "mylib.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.NoMangle,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithSupportsLastError() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" lasterr stdcall) bool Beep(unsigned int32, unsigned int32) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Beep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.SupportsLastError,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(
                        Parameter(Type.UnsignedInt32),
                        Parameter(Type.UnsignedInt32),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithAllFlags() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" nomangle ansi lasterr stdcall) bool Beep(unsigned int32, unsigned int32) cil managed preservesig
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("Beep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.NoMangle,
                                PInvokeAttributes.CharSetAnsi,
                                PInvokeAttributes.SupportsLastError,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(
                        Parameter(Type.UnsignedInt32),
                        Parameter(Type.UnsignedInt32),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                        ImplementationAttributes.PreserveSig,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithNativeUnmanaged() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" stdcall) unsigned int32 GetCurrentProcessId() native unmanaged
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetCurrentProcessId",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.UnsignedInt32,
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.Native,
                        ImplementationAttributes.Unmanaged,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokePrivateAccess() {
        assertContentEquals(
            expected = """
                .method private hidebysig static pinvokeimpl("kernel32.dll" stdcall) unsigned int32 GetCurrentProcessId() cil managed preservesig
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetCurrentProcessId",
                    attributes = listOf(
                        MethodAttribute.Private,
                        MethodAttribute.HideBySig,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.UnsignedInt32,
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                        ImplementationAttributes.PreserveSig,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeInsideClass() {
        assertContentEquals(
            expected = """
                .class public auto ansi 'MyNamespace/MyClass'
                {
                  .method public static pinvokeimpl("kernel32.dll" stdcall) unsigned int32 GetCurrentProcessId() cil managed preservesig
                  {
                  } // end of method MyNamespace/MyClass::GetCurrentProcessId
                  .method public static hidebysig void Main() cil managed
                  {
                    .maxstack 8
                    call unsigned int32 'MyNamespace/MyClass'::GetCurrentProcessId()
                    pop
                    ret
                  } // end of method MyNamespace/MyClass::Main
                } // end of class MyNamespace/MyClass
            """.trimIndent(),
            actual = generateText {
                class_("MyNamespace/MyClass") {
                    method("GetCurrentProcessId",
                        attributes = listOf(
                            MethodAttribute.Public,
                            MethodAttribute.Static,
                            MethodAttribute.PInvokeImpl(
                                moduleName = "kernel32.dll",
                                attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                            ),
                        ),
                        returnType = Type.UnsignedInt32,
                        implAttributes = ImplementationAttributes(
                            ImplementationAttributes.IL,
                            ImplementationAttributes.Managed,
                            ImplementationAttributes.PreserveSig,
                        ),
                    ) {}
                    method("Main",
                        attributes = listOf(
                            MethodAttribute.Public,
                            MethodAttribute.Static,
                            MethodAttribute.HideBySig,
                        ),
                    ) {
                        maxStack(8)
                        code()
                        insn(OpCode.Code.call, MethodReference(
                            declaringType = TypeReference(name = "MyNamespace/MyClass"),
                            name = "GetCurrentProcessId",
                            returnType = Type.UnsignedInt32,
                            parameterTypes = emptyList(),
                        ))
                        insn(OpCode.Code.pop)
                        insn(OpCode.Code.ret)
                    }
                }
            }
        )
    }

    @Test
    fun testPInvokeWithMarshalParam() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("user32.dll" stdcall) int32 MessageBox(unsigned int32, string marshal(lpstr), string marshal(lpstr)) cil managed preservesig
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MessageBox",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "user32.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.Int32,
                    parameters = listOf(
                        Parameter(Type.UnsignedInt32),
                        Parameter(Type.String, marshal = NativeType.LPStr),
                        Parameter(Type.String, marshal = NativeType.LPStr),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                        ImplementationAttributes.PreserveSig,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithMarshalReturn() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("kernel32.dll" stdcall) string marshal(lpstr) GetCurrentDirectory(unsigned int32, string) cil managed preservesig
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("GetCurrentDirectory",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "kernel32.dll",
                            attributes = PInvokeAttributes(PInvokeAttributes.CallConvStdCall),
                        ),
                    ),
                    returnType = Type.String,
                    returnMarshal = NativeType.LPStr,
                    parameters = listOf(
                        Parameter(Type.UnsignedInt32),
                        Parameter(Type.String),
                    ),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                        ImplementationAttributes.PreserveSig,
                    ),
                ) {}
            }
        )
    }

    @Test
    fun testPInvokeWithAnsiAndMarshalParam() {
        assertContentEquals(
            expected = """
                .method public static pinvokeimpl("user32.dll" ansi stdcall) bool MessageBeep(unsigned int32) cil managed
                {
                }
            """.trimIndent(),
            actual = generateText {
                method("MessageBeep",
                    attributes = listOf(
                        MethodAttribute.Public,
                        MethodAttribute.Static,
                        MethodAttribute.PInvokeImpl(
                            moduleName = "user32.dll",
                            attributes = PInvokeAttributes(
                                PInvokeAttributes.CharSetAnsi,
                                PInvokeAttributes.CallConvStdCall,
                            ),
                        ),
                    ),
                    returnType = Type.Bool,
                    parameters = listOf(Parameter(Type.UnsignedInt32)),
                    implAttributes = ImplementationAttributes(
                        ImplementationAttributes.IL,
                        ImplementationAttributes.Managed,
                    ),
                ) {}
            }
        )
    }
}
