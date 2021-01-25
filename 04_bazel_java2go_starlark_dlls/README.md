# Bazel Java - Parse Starlark Through DLL

The go code had to be placed in a separate directory because it created a circular dependency.

## Notes

- Very helpful docs for protobufs in golang: https://godoc.org/github.com/golang/protobuf/proto
- In java, the yourProto.toString() message converts a protobuf object into its marshalled type
- The string type of a protobuf seems like it's not performant
    - String message size: 639 bytes
    - Byte array size: 571 bytes (significant improvement)
- A gist of cgo things: https://gist.github.com/zchee/b9c99695463d8902cd33
- JNA complex structures: https://java-native-access.github.io/jna/3.5.0/javadoc/overview-summary.html#:~:text=To%20pass%20a%20pointer%20to,updated%20when%20the%20function%20returns.
