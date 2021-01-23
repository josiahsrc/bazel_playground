package main

import (
	"fmt"
	"math"
	"sort"
	"sync"
	// star "go.starlark.net/starlark"
)

import "C"

var count int
var mtx sync.Mutex

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

//export Sort
func Sort(vals []int) {
	sort.Ints(vals)
}

//export Log
func Log(msg string) int {
	mtx.Lock()
	defer mtx.Unlock()
	fmt.Println(msg)
	count++
	return count
}

func main() {}
