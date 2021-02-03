# Notes

## General notes

- It says here that not all features are to bazel's standard. So it might be better just to use bazel.

## FINDINGS

- Starlark has predeclared variables. Bazel has these builtin. Using bazel would simplify this ten fold.
- We would have to implement the load method if we did this. This would involve redefining bazel's already build starlark load system. It would be easier if we could just use bazel right out.

## Things Needed

- Interpret load statements

## Starlark Scanner

- The scanner is derived from Russ Cox's buildifier tool.

## Starlark Resolver

- Used for validating starlark programs.
- Not all features of the Go implementation are "standard" (that is, supported by Bazel's Java implementation), at least for now, so non-standard features such as lambda, float, and set are flag-controlled. The resolver reports any uses of dialect features that have not been enabled.
    - Meaning that I should disable these flags

# Starlark Evaluation

TODO
