.assembly extern System.Runtime {}
.assembly Hello {}
.method public static void Main() {
    .entrypoint
    ldstr "Hello, world!"
    call void [System.Runtime]System.Console::WriteLine(string)
    ret
}
