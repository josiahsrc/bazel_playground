load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")
load("@rules_java//java:def.bzl", "java_library")

# package(default_visibility = ["//visibility:public"]) <-- It says this is undefined!

go_library(
    name = "lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/05_starlark_interp/lib",
    deps = [
        "@net_starlark_go//syntax",
    ],
    random = "anything",
)

go_binary(
    name = "prog",
    embed = [
        ":lib",
    ],
)
