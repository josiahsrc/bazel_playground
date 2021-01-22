package main

import "fmt"
import "C"

//export SayHello
func SayHello() {
    fmt.Println("Hello from the Go code!")
}

func main() {}
