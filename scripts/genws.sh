# Only works if have go.mod file
bazel run //:gazelle -- update-repos -from_file=go.mod