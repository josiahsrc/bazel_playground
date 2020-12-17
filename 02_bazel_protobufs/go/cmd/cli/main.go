package main

import (
	"fmt"

	"github.com/josiahsrc/bazel_playground/02_bazel_protobufs/go/internal/something"
	"github.com/josiahsrc/bazel_playground/02_bazel_protobufs/go/pkg/net/rpc"
)

func main() {
	fmt.Println("Hello example!!!")
	something.DoSomething()
	rpc.DoHandler()
}
