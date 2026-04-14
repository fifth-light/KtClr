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
