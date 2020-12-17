package rpc

import (
	"fmt"
	"google.golang.org/protobuf/proto"
)

type something struct {
	
}

// DoHandler does something
func DoHandler() {
	fmt.Println("Doing handler...")

	if true == false {
		proto.Clone(nil)
	}

	fmt.Println("Handler done :)")
}
