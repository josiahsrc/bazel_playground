load("@rules_proto//proto:defs.bzl", "proto_library")
load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")

proto_library(
    name = "myprotos_proto",
    srcs = ["example.proto"],
    visibility = ["//visibility:public"],
    deps = ["@com_google_protobuf//:timestamp_proto"],
)

go_proto_library(
    name = "myprotos_go_proto",
    importpath = "example.com/myprotos",
    proto = ":myprotos_proto",
    visibility = ["//visibility:public"],
)
