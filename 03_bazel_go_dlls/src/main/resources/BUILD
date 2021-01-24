load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")

package(default_visibility = ["//visibility:public"])

# This is the dynamically linked binary
cc_binary(
    name = "dynamic.so",
    linkshared = True,
    deps = [
        "//03_bazel_go_dlls:archive.cc",
    ],
)

filegroup(
    name = "resources",
    srcs = [
        "dynamic.so",
    ],
    data = [
        ":dynamic.so",
    ],
)
