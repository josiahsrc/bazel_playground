package main

import (
	"fmt"

	"github.com/josiahsrc/bazel_playground/00_golang_hello/another"
)

func add(a int, b int) (result int, message string) {
	return a + b, "it worked!"
}

func main() {
	const variable = "hello world!"
	fmt.Println(variable)

	another.DoSomething()

	a, b := add(1, 5)
	fmt.Printf("Add two numbers: %d %s\n", a, b)
}
