# Don't let gazelle modify this file. It messes up the clibs.
# gazelle:ignore

load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

package(default_visibility = ["//visibility:public"])

# The shared libs
go_library(
    name = "bridge",
    srcs = [
        "hello.go",
    ],
    importpath = "github.com/josiahsrc/bazel_playground/03_bazel_go_dlls",
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

# A c++ target to call the shared libs
cc_binary(
    name = "cc_prog",
    srcs = [
        "main.cc",
    ],
    deps = [
        ":archive.cc",
    ],
)

# A java target to call the shared libs
alias(
    name = "java_prog",
    actual = "//03_bazel_go_dlls/src/main/java:binary",
)

alias(
    name = "java_prog_deploy.jar",
    actual = "//03_bazel_go_dlls/src/main/java:binary_deploy.jar",
)
