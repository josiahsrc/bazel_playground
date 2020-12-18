# This works, but isn't integrated with bazel
SRC_DIR="02_bazel_protobufs/proto"
DST_DIR="02_bazel_protobufs/go/pkg/net/rpc"
protoc -I=$SRC_DIR --go_out=$DST_DIR $SRC_DIR/example.proto
