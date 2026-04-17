package top.fifthlight.asmnet

import kotlin.String as KString

// ECMA-335 II.7.1
sealed interface TypeSpec

// ECMA-335 II.7.2
sealed interface ResolutionScope {
    data class Assembly(val name: KString): ResolutionScope {
        override fun toString() = name
    }

    data class Module(val name: KString): ResolutionScope {
        override fun toString() = ".module $name"
    }
}

// ECMA-335 II.7.2
data class TypeReference(
    val resolutionScope: ResolutionScope? = null,
    val names: List<KString>,
) : TypeSpec {
    constructor(
        resolutionScope: ResolutionScope? = null,
        name: KString
    ): this(
        resolutionScope = resolutionScope,
        names = listOf(name),
    )

    constructor(name: KString): this(names = listOf(name))

    init {
        require(names.isNotEmpty()) { "Type name cannot be empty" }
    }

    override fun toString() = names.joinToString("/").let { name ->
        if (resolutionScope != null) {
            "[$resolutionScope]$name"
        } else {
            name
        }
    }
}

// ECMA-335 II.7.1
sealed interface Type : TypeSpec {
    val hasType: Boolean
        get() = true

    // ECMA-335 II.7.2, I.8.2.2
    sealed class BuiltInType(
        val assemblyName: KString,
        val classLibraryName: KString,
    ) : Type {
        override fun toString() = assemblyName
    }

    object Bool : BuiltInType("bool", "System.Boolean")
    object Char : BuiltInType("char", "System.Char")
    object Object : BuiltInType("object", "System.Object")
    object String : BuiltInType("string", "System.String")
    object Float32 : BuiltInType("float32", "System.Single")
    object Float64 : BuiltInType("float64", "System.Double")
    object Int8 : BuiltInType("int8", "System.SByte")
    object Int16 : BuiltInType("int16", "System.Int16")
    object Int32 : BuiltInType("int32", "System.Int32")
    object Int64 : BuiltInType("int64", "System.Int64")
    object NativeInt : BuiltInType("native int", "System.IntPtr")
    object NativeUnsignedInt : BuiltInType("native unsigned int", "System.UIntPtr")
    object TypedRef : BuiltInType("typedref", "System.TypedReference")
    object UnsignedInt8 : BuiltInType("unsigned int8", "System.Byte")
    object UnsignedInt16 : BuiltInType("unsigned int16", "System.UInt16")
    object UnsignedInt32 : BuiltInType("unsigned int32", "System.UInt32")
    object UnsignedInt64 : BuiltInType("unsigned int64", "System.UInt64")

    object Void : Type {
        override fun toString() = "void"
        override val hasType
            get() = false
    }

    // ECMA-335 II.14.5
    data class MethodPointer(
        val callConv: CallConv,
        val returnType: Type,
        val parameterTypes: List<Parameter>,
    ) : Type {
        override fun toString() = buildString {
            append(callConv)
            append(' ')
            append(returnType)
            append("*(")
            append(parameterTypes.joinToString(", "))
            append(')')
        }
    }

    // ECMA-335 II.14.4.1
    data class UnmanagedTypePointer(
        val type: Type,
    ) : Type {
        override fun toString() = "$type*"
    }

    // ECMA-335 II.14.4.2
    data class ManagedTypePointer(
        val type: Type,
    ) : Type {
        override fun toString() = "$type&"
    }

    // ECMA-335 II.14.2
    data class Array(
        val type: Type,
        val bounds: List<IntRange?> = emptyList(),
    ) : Type {
        override fun toString() = buildString {
            append(type)
            append('[')
            append(bounds.joinToString(", ") {
                if (it == null || (it.first == 0 && it.last == Int.MAX_VALUE)) {
                    "..."
                } else if (it.first == 0) {
                    "${it.last}"
                } else if (it.last == Int.MAX_VALUE) {
                    "${it.first}..."
                } else {
                    "${it.first}...${it.last}"
                }
            })
            append(']')
        }
    }

    // ECMA-335 II.13
    data class ValueType(
        val type: Type,
    ) : Type {
        override fun toString() = "valuetype $type"
    }
}
