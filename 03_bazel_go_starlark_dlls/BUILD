load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

package(default_visibility = ["//visibility:public"])

# The shared libs
go_library(
    name = "bridge",
    srcs = ["hello.go"],
    importpath = "github.com/josiahsrc/bazel_playground/03_bazel_go_starlark_dlls",
)

go_binary(
    name = "archive",
    cgo = True,
    embed = [
        ":bridge",
    ],
    linkmode = "c-archive",
)

go_binary(
    name = "shared",
    cgo = True,
    embed = [
        ":bridge",
    ],
    linkmode = "c-shared",
)

# A c++ target to call the shared libs
cc_binary(
    name = "cc_prog",
    srcs = [
        "main.cc",
    ],
    deps = [
        "//03_bazel_go_starlark_dlls:archive.cc",
    ],
)

# A java target to call the shared libs
java_binary(
    name = "java_prog",
    main_class = "main.Main",
    srcs = [
        "Main.java",
    ],
    deps = [
        "@maven//:com_sun_jna_jna",
    ],
)
