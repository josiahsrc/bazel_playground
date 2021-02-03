package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"strings"

	resolve "go.starlark.net/resolve"
	starlark "go.starlark.net/starlark"
	syntax "go.starlark.net/syntax"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func bigline(s string) {
	fmt.Println("")
	fmt.Println("#############################")
	fmt.Printf("### %s\n", s)
	fmt.Println("#############################")
	fmt.Println("")
}

func printExpr(x syntax.Expr, tabs string) {
	switch ex := x.(type) {
	default:
		fmt.Printf("%s- This expression type [%T] hasn't been setup yet\n", tabs, ex)
	case *syntax.CallExpr:
		fmt.Printf("%s- This expression is a [%T]\n", tabs, ex)
		fmt.Printf("%s- Arguments:\n", tabs)
		for _, el := range ex.Args {
			printExpr(el, tabs+"\t")
		}
	case *syntax.ListExpr:
		fmt.Printf("%s- This expression is a [%T]\n", tabs, ex)
		fmt.Printf("%s- List:\n", tabs)
		for _, el := range ex.List {
			printExpr(el, tabs+"\t")
		}
	}
}

func printStarfile(f syntax.File) {
	fmt.Println("STATEMENTS:")
	for i := 0; i < len(f.Stmts); i++ {
		stmt := f.Stmts[i]

		fmt.Println("New statement:")
		switch v := stmt.(type) {
		default:
			fmt.Printf("- This type [%T] hasn't been setup yet\n", v)
		case *syntax.LoadStmt:
			fmt.Printf("- Statement is a [%T]\n", v)
			fmt.Printf("- Module name %s\n", v.ModuleName())
		case *syntax.ExprStmt:
			fmt.Printf("- Statement is a [%T]\n", v)
			printExpr(v.X, "\t")
		}
	}

	fmt.Println("")
	fmt.Println("MODULE:")
	switch m := f.Module.(type) {
	default:
		fmt.Printf("- This module type [%T] hasn't been setup yet\n", m)
	case *resolve.Module:
		fmt.Printf("- This module is from the binding file\n")
		fmt.Printf("- Locals\n")
		for _, el := range m.Locals {
			// NOTE: This prints out the name of the imported binaries.
			fmt.Printf("\t- Element=%v\n", el.First.Name)
			fmt.Printf("\t\t- Scope=%v\n", el.Scope)
			fmt.Printf("\t\t- Index=%v\n", el.Index)
		}
	}
}

func main() {
	const StarlarkFilePath = "05_go_starlark_interp/lib/starlark.star"
	const FibFilePath = "05_go_starlark_interp/lib/fib.star"

	fmt.Println("Hello world")
	fmt.Printf("Something from syntax to prevent gopls format: %v\n", syntax.PLUS_EQ)
	fmt.Printf("Something from resolve to prevent gopls format: %v\n", resolve.AllowBitwise)

	bigline("READ IN STARLARK FILE")
	srccode, err := ioutil.ReadFile(StarlarkFilePath)
	check(err)
	fmt.Print(string(srccode))

	bigline("PARSE STARLARK CODE")
	fmt.Println("Parsing some starlark stuff")
	// NOTE: Use syntax.RetainComments as mode for docs maybe?
	starfile, err := syntax.Parse(StarlarkFilePath, srccode, 0)
	check(err)
	fmt.Printf("Number of starlark statements found is %d\n", len(starfile.Stmts))
	printStarfile(*starfile)

	bigline("SET RESOLVER FLAGS")
	resolve.AllowLambda = false
	resolve.AllowFloat = false
	resolve.AllowGlobalReassign = false
	resolve.AllowLambda = false
	resolve.AllowNestedDef = false
	resolve.AllowRecursion = false
	resolve.AllowSet = false
	fmt.Println("Finished setting all resolver flags")

	bigline("RESOLVE STARLARK CODE")
	// NOTE: Resolver will set the module in the files.
	err = resolve.File(
		starfile,
		func(name string) bool { return false },
		func(name string) bool { return false },
	)
	check(err)
	fmt.Printf("Number of starlark statements found after resolve is %d\n", len(starfile.Stmts))
	printStarfile(*starfile)

	bigline("EXECUTE STARLARK CODE ^_^")

	// repeat(str, n=1) is a Go function called from Starlark.
	// It behaves like the 'string * int' operation.
	repeat := func(_ *starlark.Thread, b *starlark.Builtin, args starlark.Tuple, kwargs []starlark.Tuple) (starlark.Value, error) {
		var s string
		var n int = 1
		if err := starlark.UnpackArgs(b.Name(), args, kwargs, "s", &s, "n?", &n); err != nil {
			return nil, err
		}
		return starlark.String(strings.Repeat(s, n)), nil
	}

	// var load func(_ *starlark.Thread, module string) (starlark.StringDict, error)
	// load = func(_ *starlark.Thread, module string) (starlark.StringDict, error) {
	// 	// The issue here is that we would need to implement the load method, which would
	// 	// involve duplicating the bazel load statement functionality. Huge waste of time.
	// 	//
	// 	// This is a +1 for using bazel's java parser instead.
	// 	// return starlark.String("this should load stuff, but meh"), nil;
	// 	return nil, nil
	// }

	// Bazel delcares this file system and their load algorithm already. We shouldn't try
	// to redeclare it. That would involve loading in external repos and caching their
	// implementations. Right?
	fakeFilesystem := map[string]string{
		"c.star": `load("b.star", "b"); c = b + "!"`,
		"b.star": `load("a.star", "a"); b = a + ", world"`,
		"a.star": `a = "Hello"`,
	}

	type entry struct {
		globals starlark.StringDict
		err     error
	}

	cache := make(map[string]*entry)

	// This loads everyhing. This is the load implementation.
	var load func(_ *starlark.Thread, module string) (starlark.StringDict, error)
	load = func(_ *starlark.Thread, module string) (starlark.StringDict, error) {
		e, ok := cache[module]
		if e == nil {
			if ok {
				// request for package whose loading is in progress
				return nil, fmt.Errorf("cycle in load graph")
			}

			// Add a placeholder to indicate "load in progress".
			cache[module] = nil

			// Load and initialize the module in a new thread.
			data := fakeFilesystem[module]
			thread := &starlark.Thread{Name: "exec " + module, Load: load}
			globals, err := starlark.ExecFile(thread, module, data, nil)
			e = &entry{globals, err}

			// Update the cache.
			cache[module] = e
		}
		return e.globals, e.err
	}

	// The fak
	globals, err := load(nil, "c.star")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(globals["c"])

	// This dictionary defines the pre-declared environment.
	predeclared := starlark.StringDict{
		"repeat":   starlark.NewBuiltin("repeat", repeat),
	}

	// Execute Starlark program in a file.
	thread := &starlark.Thread{Name: "My magical starlark thread"}
	globals, err = starlark.ExecFile(thread, FibFilePath, nil, predeclared)
	check(err)

	// Retrieve a module global.
	fibonacci := globals["fibonacci"]
	fmt.Print(fibonacci)

	// Call Starlark function from Go.
	v, err := starlark.Call(thread, fibonacci, starlark.Tuple{starlark.MakeInt(10)}, nil)
	check(err)
	fmt.Printf("fibonacci(10) = %v\n", v) // fibonacci(10) = [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
}
