load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

alias(
    name = "run",
    actual = ":00_golang_hello",
)

go_library(
    name = "00_golang_hello_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/00_golang_hello",
    visibility = ["//visibility:private"],
    deps = ["//00_golang_hello/another"],
)

go_binary(
    name = "00_golang_hello",
    embed = [":00_golang_hello_lib"],
    visibility = ["//visibility:public"],
)
