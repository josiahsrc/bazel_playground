load("@io_bazel_rules_go//go:def.bzl", "go_binary")

go_binary(
    name = "awesome",
    srcs = glob(["*.go"]),
    cgo = True,
    out = "awesome.so",
    linkmode = "c-shared",
    visibility = ["//visibility:public"],
)

cc_import(
    name = "awesome_cc",
    hdrs = [":awesome.c_hdrs"],
    shared_library = ":awesome",
    visibility = ["//visibility:public"],
)
