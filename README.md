# Bazel Playground

Testing grounds for bazel projects. Below are details on how to run each project.

## 00_golang_hello

```
bazel run 00_golang_hello/cmd
```

## 01_bazel_gazelle

```
bazel run 01_bazel_gazelle/cmd/cli
```

## 02_bazel_protobufs

This one doesn't work at the moment. There is structure here for protobufs, but couldn't get autocomplete working with protobufs.

## 03_bazel_go_dlls

```
bazel run 03_bazel_go_dlls:java_prog
```

or

```
bazel run 03_bazel_go_dlls:cc_prog
```

## 04_bazel_java2go_starlark_dlls

```
bazel run 04_bazel_java2go_starlark_dlls:java_prog
```

## 05_go_starlark_interp

```
bazel run 05_go_starlark_interp:prog
```