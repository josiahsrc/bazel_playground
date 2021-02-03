# Starlark Interpretting

We need to be able to interpret starlark code when evaluating it for our language server. This will help us understand the code to provide highlighting and autocomplete. The goal of this project is to use the starlark-go repo to interpret the starlark code in a readable Go format.

Run this example using:

```
bazel_playground % bazel run //05_go_starlark_interp:prog
```
