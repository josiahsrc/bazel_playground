load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

package(default_visibility = ["//visibility:public"])

# A java target to call the shared libs
java_binary(
    name = "binary",
    main_class = "main.Main",
    srcs = [
        "Main.java",
    ],
    deps = [
        "@maven//:net_java_dev_jna_jna",
    ],
    resources = [
        "//03_bazel_go_starlark_dlls/src/main/resources",
    ],
    jvm_flags = [
        "-Djava.library.path=//03_bazel_go_starlark_dlls/src/main/resources"
    ],
)
