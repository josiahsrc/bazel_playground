package main

import "fmt"
import "C"

//export SayHello
func SayHello() {
    fmt.Println("hello")
}

func main() {}