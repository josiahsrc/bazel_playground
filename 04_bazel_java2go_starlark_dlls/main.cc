#include <iostream>
#include "04_bazel_java2go_starlark_dlls/archive.h"

int main()
{
    std::cout << "Hello from the C code!" << std::endl;
    std::cout << "This is a statically linked example." << std::endl;

    GoInt a = 12;
    std::cout << "This is a GoInt: " << a << std::endl;

    SayHello();

    return 0;
}
