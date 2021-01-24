load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "cmd_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/00_golang_hello",
    visibility = ["//visibility:private"],
)

go_binary(
    name = "cmd",
    embed = [":cmd_lib"],
    visibility = ["//visibility:public"],
)
