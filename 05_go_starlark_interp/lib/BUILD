load("@io_bazel_rules_go//go:def.bzl", "go_binary")

package(default_visibility = ["//visibility:public"])

go_binary(
    name = "prog",
    importpath = "github.com/josiahsrc/bazel_playground/05_go_starlark_interp/lib",
    srcs = [
        "main.go",
    ],
    deps = [
        "@net_starlark_go//syntax",
        "@net_starlark_go//resolve",
        "@net_starlark_go//starlark",
    ],
    data = [
        "starlark.star",
        "fib.star",
    ],
)
