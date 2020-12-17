# Notes

You have to run gazelle without update repos for it to work with generating build files `bazel run //:gazelle`.

You have to run gazelle with the repos option to update the dependencies automagically `bazel run //:gazelle -- update-repos -from_file=go.mod`.

The go lang server is slow sometimes it looks like. Give it a second to quit throwing a tantrum and it will probably correct its deps.

Domain driven development is what golang suggests (but not needed, you can structure your project however).

`go get google.golang.org/protobuf/proto` is the way to import protobufs...
