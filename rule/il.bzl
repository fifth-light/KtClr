load("@bazel_lib//lib:copy_file.bzl", "COPY_FILE_TOOLCHAINS", "copy_file_action")

def ildasm_action(actions, input, output, ildasm_tool, is_windows):
    args = actions.args()
    args.add(input)
    if is_windows:
        args.add(output, format = "/OUT=%s")
    else:
        args.add(output, format = "-OUT=%s")
    actions.run(
        inputs = depset([input]),
        outputs = [output],
        executable = ildasm_tool,
        arguments = [args],
    )

def _ildasm_impl(ctx):
    output_file = ctx.actions.declare_file(ctx.label.name + ".il")
    is_windows = ctx.target_platform_has_constraint(ctx.attr._windows_constraint[platform_common.ConstraintValueInfo])
    ildasm_action(ctx.actions, ctx.file.input, output_file, ctx.executable._ildasm, is_windows)
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
        "_windows_constraint": attr.label(
            default = "@platforms//os:windows",
        ),
    },
    doc = "Disassembly .net binary",
)

def ilasm_action(actions, input, output, is_dll, ilasm_tool, is_windows):
    args = actions.args()
    if is_windows:
        args.add("/NOLOGO")
        args.add("/QUIET")
        if is_dll:
            args.add("/DLL")
    else:
        args.add("-NOLOGO")
        args.add("-QUIET")
        if is_dll:
            args.add("-DLL")
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
    "_windows_constraint": attr.label(
        default = "@platforms//os:windows",
    ),
}

def _ilasm_exe_impl(ctx):
    output_file = ctx.actions.declare_file(ctx.label.name + ".exe")
    outputs = [output_file]

    is_windows = ctx.target_platform_has_constraint(ctx.attr._windows_constraint[platform_common.ConstraintValueInfo])
    ilasm_action(ctx.actions, ctx.file.input, output_file, False, ctx.executable._ilasm, is_windows)

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
    is_windows = ctx.target_platform_has_constraint(ctx.attr._windows_constraint[platform_common.ConstraintValueInfo])
    ilasm_action(ctx.actions, ctx.file.input, output_file, True, ctx.executable._ilasm, is_windows)
    return DefaultInfo(files = depset([output_file]))

ilasm_dll = rule(
    implementation = _ilasm_dll_impl,
    attrs = _COMMON_ILASM_ATTRS,
    doc = "Assemble .il file into .net binary",
)
