load("@io_bazel_rules_go//go:def.bzl", "go_library")

go_library(
    name = "rpc",
    srcs = ["handler.go"],
    importpath = "github.com/josiahsrc/bazel_playground/02_bazel_protobufs/go/pkg/net/rpc",
    visibility = ["//visibility:public"],
    deps = [
        "//02_bazel_protobufs/proto",
        "@org_golang_google_protobuf//proto",
    ],
)
