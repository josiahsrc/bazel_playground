load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "example_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/01_bazel_gazelle/cmd/example",
    visibility = ["//visibility:private"],
)

go_binary(
    name = "example",
    embed = [":example_lib"],
    visibility = ["//visibility:public"],
)
