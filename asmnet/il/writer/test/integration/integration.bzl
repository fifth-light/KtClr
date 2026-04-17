load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_binary")
load("//rule/dotnet:il.bzl", "ilasm_exe")
load("@bazel_lib//lib:diff_test.bzl", "diff_test")
load("@bazel_skylib//rules:run_binary.bzl", "run_binary")
load("@bazel_skylib//rules:write_file.bzl", "write_file")

def _integration_test_impl(name, visibility, tags, runtimeconfig, expected, **kwargs):
    kt_jvm_binary(
        name = name + "_generator",
        **kwargs,
    )

    il_name = name + ".il"
    run_binary(
        name = name + "_il",
        outs = [il_name],
        args = ["$(location %s)" % il_name],
        tool = name + "_generator",
    )

    ilasm_exe(
        name = name + "_exe",
        input = name + "_il",
        runtimeconfig = runtimeconfig,
        visibility = visibility,
        tags = tags,
    )

    expected_name = name + "_expected.txt"
    write_file(
        name = name + "_expected",
        out = expected_name,
        content = expected,
    )

    actual_name = name + "_actual.txt"
    native.genrule(
        name = name + "_actual",
        srcs = [],
        outs = [actual_name],
        tools = [name + "_exe"],
        cmd = "$(location %s_exe) > $@" % name,
    )

    diff_test(
        name = name,
        file1 = name + "_actual",
        file2 = name + "_expected",
    )

integration_test = macro(
    inherit_attrs = kt_jvm_binary,
    implementation = _integration_test_impl,
    attrs = {
        "runtimeconfig": attr.label(
            mandatory = False,
            allow_single_file = [".json"],
            doc = "Optional runtimeconfig.json to copy alongside output",
        ),
        "expected": attr.string_list(
            mandatory = True,
            doc = "Expected output lines",
        )
    }
)
