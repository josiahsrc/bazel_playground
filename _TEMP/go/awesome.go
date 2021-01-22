package main

import "C"
import (
	"math"
	"sync"
)

var count int
var mtx sync.Mutex

//export Add
func Add(a, b int) int { return a + b }

//export Cosine
func Cosine(x float64) float64 { return math.Cos(x) }

func main() {}
