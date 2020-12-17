load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "cli_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/01_bazel_gazelle/cmd/cli",
    visibility = ["//visibility:private"],
    deps = [
        "//01_bazel_gazelle/pkg/hello",
        "//01_bazel_gazelle/pkg/something",
    ],
)

go_binary(
    name = "cli",
    embed = [":cli_lib"],
    visibility = ["//visibility:public"],
)
