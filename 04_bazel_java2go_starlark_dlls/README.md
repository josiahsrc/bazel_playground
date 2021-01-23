# DLLs With Bazel

Examples of how to use DLLs in bazel.

## CGo producer

The hello.go file is the go code whih will generate the `.so` library to be used.

## C++ Consumer (static)

The main.cc is using that generated example statically.

## Java Consumer (dynamic)

Uses the jna library. The src/* directory is using the `.so` library to dynamically load an the generated cgo code.

## Resources

- Here is a very helpful resource for loading dynamic libs: https://medium.com/learning-the-go-programming-language/calling-go-functions-from-other-languages-4c7d8bcc69bf
- Helpful comment: https://github.com/bazelbuild/rules_go/issues/2782#issuecomment-765615482
- Great repository: https://github.com/TheOpenDictionary/odict/blob/master/java/main/cpp/BUILD.bazel


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
