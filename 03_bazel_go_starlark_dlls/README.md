# DLLs With Bazel

The hello.go file is the go code whih will generate the `.so` library to be used.

The main.cc is using that generated example statically.

The src/* directory is using the `.so` library to dynamically load an the generated cgo code.

Here is a very helpful resource for loading dynamic libs: https://medium.com/learning-the-go-programming-language/calling-go-functions-from-other-languages-4c7d8bcc69bf

<!-- ```starlark
# Not sure what this target is supposed to do
go_binary(
    name = "shared",
    cgo = True,
    embed = [
        ":bridge",
    ],
    linkmode = "c-shared",
)
``` -->
