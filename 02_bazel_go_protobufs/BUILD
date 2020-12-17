load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")
load("@bazel_gazelle//:def.bzl", "gazelle")

# gazelle:prefix github.com/josiahsrc/bazel_playground/01_bazel_gazelle
# gazelle:build_file_name BUILD,BUILD.bazel
gazelle(
    name = "gazelle",
)

go_library(
    name = "01_bazel_gazelle_lib",
    srcs = ["main.go"],
    importpath = "github.com/josiahsrc/bazel_playground/01_bazel_gazelle",
    visibility = ["//visibility:private"],
    deps = ["@com_github_golang_protobuf//:protobuf"],
)

go_binary(
    name = "01_bazel_gazelle",
    embed = [":01_bazel_gazelle_lib"],
    visibility = ["//visibility:public"],
)
