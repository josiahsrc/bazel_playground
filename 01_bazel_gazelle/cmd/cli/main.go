package main

import (
	"fmt"

	"github.com/josiahsrc/bazel_playground/01_bazel_gazelle/pkg/hello"
	"github.com/josiahsrc/bazel_playground/01_bazel_gazelle/pkg/something"
)

func main() {
	fmt.Println("Example CLI")
	hello.PrintHello()
	something.DoSomething()
}
