# Don't let gazelle modify this file.
# gazelle:ignore

load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")
load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")

package(default_visibility = ["//visibility:public"])

proto_library(
    name = "myprotos_proto",
    srcs = ["starlark_parsing.proto"],
    visibility = ["//visibility:public"],
)

go_proto_library(
    name = "myprotos_go_proto",
    importpath = "example.com/myprotos",
    proto = ":myprotos_proto",
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "myprotos_java_proto",
    deps = [":myprotos_proto"],
)
