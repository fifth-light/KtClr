load("@bazel_lib//lib:copy_file.bzl", "COPY_FILE_TOOLCHAINS", "copy_file_action")
load("@bazel_lib//lib:paths.bzl", "to_rlocation_path")

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
    args.add(input)
    if is_windows:
        args.add("/NOLOGO")
        args.add("/QUIET")
        if is_dll:
            args.add("/DLL")
        args.add(output, format = "/OUTPUT=%s")
    else:
        args.add("-NOLOGO")
        args.add("-QUIET")
        if is_dll:
            args.add("-DLL")
        args.add(output, format = "-OUTPUT=%s")
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

    if ctx.file.runtimeconfig:
        rc_output = ctx.actions.declare_file(ctx.label.name + ".runtimeconfig.json")
        copy_file_action(ctx, ctx.file.runtimeconfig, rc_output)
        outputs.append(rc_output)

    toolchain = ctx.toolchains["@rules_dotnet//dotnet:toolchain_type"]
    runtime = toolchain.runtime
    dotnet_info = toolchain.dotnetinfo

    launcher = ctx.actions.declare_file("{}.{}".format(ctx.label.name, "bat" if is_windows else "sh"))
    if is_windows:
        ctx.actions.expand_template(
            template = ctx.file._launcher_bat,
            output = launcher,
            substitutions = {
                "TEMPLATED_dotnet": to_rlocation_path(ctx, runtime.files_to_run.executable),
                "TEMPLATED_executable": to_rlocation_path(ctx, output_file),
            },
            is_executable = True,
        )
    else:
        ctx.actions.expand_template(
            template = ctx.file._launcher_sh,
            output = launcher,
            substitutions = {
                "TEMPLATED_dotnet": to_rlocation_path(ctx, runtime.files_to_run.executable),
                "TEMPLATED_executable": to_rlocation_path(ctx, output_file),
            },
            is_executable = True,
        )

    runfiles = ctx.runfiles(files = outputs + dotnet_info.runtime_files)
    runfiles = runfiles.merge(ctx.attr._bash_runfiles[DefaultInfo].default_runfiles)

    return DefaultInfo(
        files = depset(outputs),
        executable = launcher,
        runfiles = runfiles,
    )

ilasm_exe = rule(
    implementation = _ilasm_exe_impl,
    executable = True,
    attrs = _COMMON_ILASM_ATTRS | {
        "runtimeconfig": attr.label(
            mandatory = False,
            allow_single_file = [".json"],
            doc = "Optional runtimeconfig.json to copy alongside output",
        ),
        "_launcher_sh": attr.label(
            doc = "A template file for the launcher on Linux/MacOS",
            default = "//rule/dotnet:launcher.sh.tpl",
            allow_single_file = True,
        ),
        "_launcher_bat": attr.label(
            doc = "A template file for the launcher on Windows",
            default = "//rule/dotnet:launcher.bat.tpl",
            allow_single_file = True,
        ),
        "_bash_runfiles": attr.label(default = "@rules_shell//shell/runfiles"),
    },
    toolchains = COPY_FILE_TOOLCHAINS + ["@rules_dotnet//dotnet:toolchain_type"],
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
