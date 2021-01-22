load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")

cc_library(
    name = "ex_lib",
    srcs = [
        "example.cc",
    ],
    visibility = ["//visibility:public"],
    deps = [
        "//03_bazel_go_starlark_dlls/bridge:archive.cc",
    ],
    alwayslink = True,
)

cc_binary(
    name = "ex.so",
    linkshared = True,
    visibility = ["//visibility:public"],
    deps = [":ex_lib"],
)

cc_binary(
    name = "prog",
    srcs = [
        "main.cc",
    ],
    deps = [
        ":ex.so",
    ],
    visibility = ["//visibility:public"],
)
