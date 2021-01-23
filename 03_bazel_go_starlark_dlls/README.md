# DLLs With Bazel

```starlark
# Not sure what this target is supposed to do
go_binary(
    name = "shared",
    cgo = True,
    embed = [
        ":bridge",
    ],
    linkmode = "c-shared",
)
```
