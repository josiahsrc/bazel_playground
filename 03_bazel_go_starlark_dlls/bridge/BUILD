load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "bridge",
    srcs = ["hello.go"],
    importpath = "github.com/josiahsrc/bazel_playground/03_bazel_go_starlark_dlls/bridge",
    visibility = ["//visibility:private"],
)

go_binary(
    name = "archive",
    cgo = True,
    embed = [":bridge"],
    linkmode = "c-archive",
    visibility = ["//visibility:public"],
)

go_binary(
    name = "shared",
    cgo = True,
    embed = [":bridge"],
    linkmode = "c-shared",
    visibility = ["//visibility:public"],
)
