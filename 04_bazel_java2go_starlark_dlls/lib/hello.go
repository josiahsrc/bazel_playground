package main

import (
	"fmt"
	"math"
	"sort"
	"sync"
	"encoding/base64"
	"strconv" // Convert string to int
	// "unsafe" // Used for accessing pointers

	myprotos "example.com/myprotos"
	proto "github.com/golang/protobuf/proto"
	star "go.starlark.net/syntax"
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
// *C.char and free it explicitly.

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
func ParseStarlarkCode(in string) (out *C.char) {
	fmt.Println("")
	fmt.Println("##################################")
	fmt.Println("## PARSING STARLARK FROM GOLANG ##")
	fmt.Println("##################################")

	fmt.Println("")
	fmt.Println("MEMORY (Golang):")
	fmt.Printf("length: %d\n", len(in))

	fmt.Println("")
	fmt.Println("--UNMARSHAL INPUT------------------------")
	fmt.Printf("Protobuf message length (Golang): %d\n", len(in))

	bytes, err := base64.StdEncoding.DecodeString(in)
	if err != nil {
		fmt.Printf("Failed to deode input: %s\n", err)
	} else {
		fmt.Println("Decode was successful.")
	}

	parseinput := &myprotos.ParseInput{}
	err = proto.Unmarshal(bytes, parseinput)
	if err != nil {
		fmt.Printf("Fatal error: %s\n", err)
	} else {
		fmt.Println("Unmarshal was successful.")
	}

	fmt.Println("")
	fmt.Println("FIELDS (Golang):")
	fmt.Printf("filename: %s\n", parseinput.Filename)
	fmt.Printf("content: %s\n", parseinput.Content)
	fmt.Printf("id: %d\n", parseinput.Id)
	fmt.Println("-----------------------------------------")

	fmt.Println("--ATTEMPTING TO PARSE STARLARK CONTENT---")
	// A Mode value is a set of flags (or 0) that controls optional parser functionality.
	// This is the third argument for starlark's parse function.
	// star.Mode()
	
	// Parse the content
	f, err := star.Parse(parseinput.Filename, parseinput.Content, 0)
	parseoutput := &myprotos.ParseOutput{}

	// Package response
	if err != nil {
		parseoutput.Success = false
		parseoutput.Message = "There was an error during parsing."
		fmt.Printf("Starlark parse error: %s\n", err)
	} else {
		parseoutput.Success = true
		parseoutput.Message = "Successfully parsed with " + strconv.Itoa(len(f.Stmts)) + " statements."
		fmt.Println("Parse was successful!")
	}

	fmt.Println("STATEMENTS:")
	for idx, stmt := range f.Stmts {
		fmt.Println(idx, "=>", stmt)
	}

	fmt.Println("-----------------------------------------")
	fmt.Println("")

	fmt.Println("##########")
	fmt.Println("## DONE ##")
	fmt.Println("##########")

	bytes, err = proto.Marshal(parseoutput)
	resstr := base64.StdEncoding.EncodeToString(bytes)
	return C.CString(resstr)
}

func main() {}
