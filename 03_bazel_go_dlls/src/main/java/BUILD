load("@rules_cc//cc:defs.bzl", "cc_binary", "cc_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_library")

package(default_visibility = ["//visibility:public"])

# A java target to call the shared libs
java_binary(
    name = "binary",
    srcs = [
        "Main.java",
    ],
    main_class = "main.Main",
    resources = [
        "//03_bazel_go_dlls/src/main/resources",
    ],
    deps = [
        "@maven//:net_java_dev_jna_jna",
    ],
)
