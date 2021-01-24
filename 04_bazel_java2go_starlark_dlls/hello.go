package main

import (
	"fmt"
	"math"
	"sort"
	"sync"

	// star "go.starlark.net/syntax"
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

// NOTE: Careful here! You can't return a string from a
// function. It will not be freed. You must return a
// *C.char somehow and free it explicitely.

// NOTE: You can return two values from a function and
// the consumer can interpret it. See following example:
//
// Go functions can be exported for use by C code in the following way:
//
// //export MyFunction
// func MyFunction(arg1, arg2 int, arg3 string) int64 {...}
//
// //export MyFunction2
// func MyFunction2(arg1, arg2 int, arg3 string) (int64, *C.char) {...}
// They will be available in the C code as:
///
// extern GoInt64 MyFunction(int arg1, int arg2, GoString arg3);
// extern struct MyFunction2_return MyFunction2(int arg1, int arg2, GoString arg3);

//export ParseStarlarkCode
func ParseStarlarkCode(content string) *C.char {
	fmt.Println("Parsing starlark...")

	fmt.Println("--GO's INPUT CONTENT---------------------")
	fmt.Printf("Content=%s\n", content)
	fmt.Println("-----------------------------------------")
	fmt.Println("")

	fmt.Println("--ATTEMPTING TO PARSE STARLARK CONTENT---")

	// filename := "example.star"

	// A Mode value is a set of flags (or 0) that controls optional parser functionality.
	// This is the third argument for this function.
	// star.Mode()
	
	// f, err := star.Parse(filename, content, 0)
	// fmt.Println("Results:")
	// fmt.Println(f)
	// fmt.Println(err)
	
	fmt.Println("-----------------------------------------")
	fmt.Println("")

	return C.CString("SUCCESS")
}

func main() {}
