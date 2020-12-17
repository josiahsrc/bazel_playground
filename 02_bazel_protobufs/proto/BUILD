load("@rules_proto//proto:defs.bzl", "proto_library")
load("@io_bazel_rules_go//go:def.bzl", "go_library")
load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")

proto_library(
    name = "tutorial_proto",
    srcs = ["example.proto"],
    visibility = ["//visibility:public"],
)

go_proto_library(
    name = "tutorial_go_proto",
    importpath = "github.com/josiahsrc/bazel_playground/02_bazel_protobufs/proto",
    proto = ":tutorial_proto",
    visibility = ["//visibility:public"],
)

go_library(
    name = "proto",
    embed = [":tutorial_go_proto"],
    importpath = "github.com/josiahsrc/bazel_playground/02_bazel_protobufs/proto",
    visibility = ["//visibility:public"],
)
