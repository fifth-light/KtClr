load("@bazel_lib//lib:copy_file.bzl", "COPY_FILE_TOOLCHAINS", "copy_file_action")

def ildasm_action(actions, input, output, ildasm_tool):
    args = actions.args()
    args.add(input)
    args.add(output, format = "/OUT=%s")
    actions.run(
        inputs = depset([input]),
        outputs = [output],
        executable = ildasm_tool,
        arguments = [args],
    )

def _ildasm_impl(ctx):
    output_file = ctx.actions.declare_file(ctx.label.name + ".il")
    ildasm_action(ctx.actions, ctx.file.input, output_file, ctx.executable._ildasm)
    return [DefaultInfo(files = depset([output_file]))]

ildasm = rule(
    implementation = _ildasm_impl,
    attrs = {
        "input": attr.label(
            mandatory = True,
            allow_single_file = ["dll", "exe"],
            doc = "Input file to disassembly",
        ),
        "_ildasm": attr.label(
            default = "//third_party/ildasm",
            executable = True,
            cfg = "exec",
        ),
    },
    doc = "Disassembly .net binary",
)

def ilasm_action(actions, input, output, is_dll, ilasm_tool):
    args = actions.args()
    args.add("/NOLOGO")
    args.add("/QUIET")
    if is_dll:
        args.add("/DLL")
    args.add(input)
    actions.run(
        inputs = depset([input]),
        outputs = [output],
        executable = ilasm_tool,
        arguments = [args],
    )

_COMMON_ILASM_ATTRS = {
    "input": attr.label(
        mandatory = True,
        allow_single_file = [".il"],
        doc = "Input IL file to assemble",
    ),
    "_ilasm": attr.label(
        default = "//third_party/ilasm",
        executable = True,
        cfg = "exec",
    ),
}

def _ilasm_exe_impl(ctx):
    output_file = ctx.actions.declare_file(ctx.label.name + ".exe")
    outputs = [output_file]

    ilasm_action(ctx.actions, ctx.file.input, output_file, False, ctx.executable._ilasm)

    runfile_outputs = []
    if ctx.file.runtimeconfig:
        rc_output = ctx.actions.declare_file(ctx.label.name + ".runtimeconfig.json")
        copy_file_action(ctx, ctx.file.runtimeconfig, rc_output)
        outputs.append(rc_output)
        runfile_outputs.append(rc_output)

    runfiles = ctx.runfiles(files = runfile_outputs)

    return DefaultInfo(
        files = depset(outputs),
        executable = output_file,
        runfiles = runfiles,
    )

ilasm_exe = rule(
    implementation = _ilasm_exe_impl,
    attrs = _COMMON_ILASM_ATTRS | {
        "runtimeconfig": attr.label(
            allow_single_file = [".json"],
            doc = "Optional runtimeconfig.json to copy alongside output",
        ),
    },
    toolchains = COPY_FILE_TOOLCHAINS,
    doc = "Assemble .il file into .net binary",
)

def _ilasm_dll_impl(ctx):
    output_file = ctx.actions.declare_file(ctx.label.name + ".dll")
    ilasm_action(ctx.actions, ctx.file.input, output_file, True, ctx.executable._ilasm)
    return DefaultInfo(files = depset([output_file]))

ilasm_dll = rule(
    implementation = _ilasm_dll_impl,
    attrs = _COMMON_ILASM_ATTRS,
    doc = "Assemble .il file into .net binary",
)
