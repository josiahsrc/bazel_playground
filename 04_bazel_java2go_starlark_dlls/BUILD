load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")

package(default_visibility = ["//visibility:public"])

# A c++ target to call the shared libs
cc_binary(
    name = "cc_prog",
    srcs = [
        "main.cc",
    ],
    deps = [
        "//04_bazel_java2go_starlark_dlls/lib:archive.cc",
        "@net_starlark_go_syntax",
    ],
)

# A java target to call the shared libs
alias(
    name = "java_prog",
    actual = "//04_bazel_java2go_starlark_dlls/src/main/java:binary",
)

alias(
    name = "java_prog_deploy.jar",
    actual = "//04_bazel_java2go_starlark_dlls/src/main/java:binary_deploy.jar",
)
