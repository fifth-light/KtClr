package top.fifthlight.asmnet.il.writer

import top.fifthlight.asmnet.*
import java.io.Writer
import java.lang.AutoCloseable
import java.util.*

class ILTextModuleWriter internal constructor(
    private val writer: TextWriter,
) : ModuleVisitor, AutoCloseable {
    constructor(writer: Writer) : this(TextWriter(writer))

    override fun visitAssembly(name: String, declaration: AssemblyDeclaration) = writer.write {
        +".assembly "
        identifier(name)
        line()
        block {
            declaration.hash?.let {
                +".hash algorithm "
                hex(it.value)
                line()
            }
            declaration.culture?.let {
                +".culture "
                quoted(it)
                line()
            }
            declaration.publicKey?.let {
                +".publickey = "
                hex(it)
                line()
            }
            declaration.version?.let {
                +".ver "
                +"%d:%d:%d:%d".format(it.major, it.minor, it.build, it.revision)
                line()
            }
        }
    }

    override fun visitExternAssembly(name: String, declaration: ExternAssemblyDeclaration) = writer.write {
        +".assembly extern "
        identifier(name)
        line()
        block {
            declaration.culture?.let {
                +".culture "
                quoted(it)
                line()
            }
            declaration.publicKeyToken?.let {
                +".publickeytoken = ( "
                hex(it)
                +" )"
                line()
            }
            declaration.publicKey?.let {
                +".publickey = ( "
                hex(it)
                +" )"
                line()
            }
            declaration.version?.let {
                +".ver "
                +"%d:%d:%d:%d".format(it.major, it.minor, it.build, it.revision)
                line()
            }
        }
    }

    override fun visitFile(
        noMetadata: Boolean,
        fileName: String,
        hash: ByteArray,
        entryPoint: Boolean
    ) = writer.write {
        +".file "
        if (noMetadata) {
            +"nometadata "
        }
        identifier(fileName)
        +" .hash = ("
        hex(hash)
        +")"
        if (entryPoint) {
            +" .entrypoint"
        }
        line()
    }

    override fun visitModule(name: String, mvid: UUID?) = writer.write {
        +".module "
        identifier(name)
        line()
        mvid?.let {
            +"// MVID: "
            guid(it)
            line()
        }
    }

    override fun visitExternModule(fileName: String) = writer.write {
        +".module extern "
        identifier(fileName)
        line()
    }

    override fun visitImageBase(base: Int) = writer.write {
        +".imagebase "
        hex(base)
        line()
    }

    override fun visitFileAlignment(alignment: Int) = writer.write {
        +".file alignment "
        hex(alignment)
        line()
    }

    override fun visitStackReserve(stackReserve: Int) = writer.write {
        +".stackreserve "
        hex(stackReserve)
        line()
    }

    override fun visitSubsystem(subsystem: Subsystem) = writer.write {
        +".subsystem "
        hex(subsystem.value)
        +" // "
        +subsystem.name
        line()
    }

    override fun visitCorFlags(flag: RuntimeFlags) = writer.write {
        +".corflags "
        hex(flag.value)
        val comment = buildString {
            var sep = ""
            if (flag.ilOnly) {
                append(sep); append("ILONLY"); sep = " | "
            }
            if (flag._32BitRequired) {
                append(sep); append("32BITREQUIRED"); sep = " | "
            }
            if (flag.strongNameSigned) {
                append(sep); append("STRONGNAMESIGNED"); sep = " | "
            }
            if (flag.nativeEntryPoint) {
                append(sep); append("NATIVE_ENTRYPOINT"); sep = " | "
            }
            if (flag.trackDebugData) {
                append(sep); append("TRACKDEBUGDATA"); sep = " | "
            }
        }
        if (comment.isNotEmpty()) {
            +" // "
            +comment
        }
        line()
    }

    @Suppress("RedundantNullableReturnType")
    override fun visitClass(name: String): ClassVisitor? = ILTextClassWriter(writer, name)

    @Suppress("RedundantNullableReturnType")
    override fun visitMethod(
        name: String,
        returnType: TypeSpec,
        callConv: CallConv,
        attributes: MethodAttributes,
        implAttributes: ImplementationAttributes,
        entryPoint: Boolean,
        parameters: List<Parameter>
    ): MethodVisitor? = ILTextMethodWriter(
        writer = writer,
        className = null,
        name = name,
        returnType = returnType,
        callConv = callConv,
        attributes = attributes,
        implAttributes = implAttributes,
        entryPoint = entryPoint,
        parameters = parameters,
    )

    // ECMA-335 II.16
    override fun visitField(
        name: String,
        type: TypeSpec,
        attributes: FieldAttributes,
        offset: Int?,
        initValue: FieldInitValue?,
    ) = writer.write {
        fieldDecl(name, type, attributes, offset, initValue)
    }

    override fun visitEnd() {}

    override fun close() = writer.close()
}
