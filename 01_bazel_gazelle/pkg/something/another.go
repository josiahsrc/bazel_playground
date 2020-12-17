package something

import (
	"fmt"
)

// DoAnother does something
func DoAnother() {
	fmt.Println("I did another thing")
	DoSomething();
}
