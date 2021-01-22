load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_binary(
    name = "go_code",
    srcs = ["go_hello.go"],
    cgo = True,
    linkmode = "c-shared",
    out = "go_code.so",
)

cc_binary(
    name = "cc_code",
    srcs = ["cc_hello.c"],
    deps = [":go_code.cc"],
)
