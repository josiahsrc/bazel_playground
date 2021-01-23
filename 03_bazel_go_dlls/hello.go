package main

import (
	"fmt"
	"math"
)

import "C"

//export SayHello
func SayHello() {
	fmt.Println("Hello from the Go code!")
}

//export Add
func Add(a, b int) int {
	return a + b
}

//export Cosine
func Cosine(x float64) float64 {
	return math.Cos(x)
}

func main() {}
