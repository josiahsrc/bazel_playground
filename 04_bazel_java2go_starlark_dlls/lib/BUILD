# Don't let gazelle modify this file. It messes up the clibs.
# gazelle:ignore

load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

package(default_visibility = ["//visibility:public"])

# The shared libs
go_library(
    name = "bridge",
    srcs = ["hello.go"],
    importpath = "github.com/josiahsrc/bazel_playground/04_bazel_java2go_starlark_dlls/lib",
    deps = [
        "@net_starlark_go//syntax",
        "@com_github_golang_protobuf//proto",
        "//04_bazel_java2go_starlark_dlls/proto:myprotos_go_proto",
    ],
)

# This is a statically linked binary and can be referenced with archive.cc
go_binary(
    name = "archive",
    cgo = True,
    embed = [
        ":bridge",
    ],
    linkmode = "c-archive",
)
