load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "cli_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/02_bazel_protobufs/go/cmd/cli",
    visibility = ["//visibility:private"],
    deps = [
        "//02_bazel_protobufs/go/internal/something",
        "//02_bazel_protobufs/go/pkg/net/rpc",
    ],
)

go_binary(
    name = "cli",
    embed = [":cli_lib"],
    visibility = ["//visibility:public"],
)
