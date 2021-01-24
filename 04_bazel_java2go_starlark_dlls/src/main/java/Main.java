package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;

// Examples taken from this helpful link:
// https://github.com/vladimirvivien/go-cshared-examples/blob/master/Client.java
public class Main {
    public static interface Archive extends Library {
        public static class GoString extends Structure {
            public static class ByValue extends GoString implements Structure.ByValue { }

            public String p;
            public long n;

            public static GoString.ByValue fromValue(String value) {
                GoString.ByValue res = new Archive.GoString.ByValue();
                res.p = value;
                res.n = value.length();
                return res;
            }
            
            protected List getFieldOrder() {
                return Arrays.asList(new String[]{ "p", "n" });
            }
        }

        public class GoSlice extends Structure {
            public static class ByValue extends GoSlice implements Structure.ByValue {}

            public Pointer data;
            public long len;
            public long cap;

            protected List getFieldOrder() {
                return Arrays.asList(new String[]{"data", "len", "cap"});
            }
        }

        public void SayHello();

        public long Add(long a, long b);

        public double Cosine(double value);

        public int Log(GoString.ByValue str);

        public void Sort(GoSlice.ByValue vals);

        public Pointer ParseStarlarkCode(GoString.ByValue content);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from the Java code!"); 
        System.out.println("This is a dynamically linked example."); 

        Archive dll;
        {
            File file = Native.extractFromResourcePath("/dynamic.so");
            System.out.println("EXTRACTED=" + file.toString()); 
            dll = (Archive) Native.load(file.toString(), Archive.class);
        }

        // Print some info about the library
        {
            Method[] methods = dll.getClass().getDeclaredMethods();
            System.out.println("\nAVAILABLE DLL METHODS:"); 
            for (Method method : methods) {
                System.out.println(method);
            }

            System.out.println(""); 
        }

        dll.SayHello();

        System.out.println("GO says 5+6=" + dll.Add(5, 6)); 
        System.out.println("GO says Cos(5)=" + dll.Cosine(5)); 

        System.out.println(""); 
        System.out.println("Testing out Golang logging...");
        {
            Archive.GoString.ByValue stringToLog = Archive.GoString.fromValue("Hello, message sent to Go from Java!");

            System.out.println("Logged with count=" + dll.Log(stringToLog));
            System.out.println("Logged with count=" + dll.Log(stringToLog));
            System.out.println("Logged with count=" + dll.Log(stringToLog));
        }

        System.out.println(""); 
        System.out.println("Testing out Golang sorting...");
        {
            // First, prepare data array 
            long[] nums = new long[]{53,11,5,2,88};
            Memory arr = new Memory(nums.length * Native.getNativeSize(Long.TYPE));
            arr.write(0, nums, 0, nums.length); 

            // Fill in the GoSlice class for type mapping
            Archive.GoSlice.ByValue slice = new Archive.GoSlice.ByValue();
            slice.data = arr;
            slice.len = nums.length;
            slice.cap = nums.length;

            dll.Sort(slice);

            System.out.print("GO Sort(53,11,5,2,88) = [");
            long[] sorted = slice.data.getLongArray(0, nums.length);

            for(int i = 0; i < sorted.length; i++){
                System.out.print(sorted[i] + " ");
            }

            System.out.println("]");
        }

        System.out.println(""); 
        System.out.println("Testing out Golang STARLARK PARSING...");
        {
            // Path fileName = Path.of("resources/example.star");
            // String content = Files.readString(fileName);
            // System.out.println("Starlark content:\n" + content);

            // Load a starlark file's content
            String fileName = "/example.star";
            String content;
            {
                InputStream fileStream = Main.class.getResourceAsStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
                content = reader.lines().collect(Collectors.joining());
            }

            System.out.println("Raw " + fileName + " starlark file content:");
            System.out.println("---START-------------------------------------"); 
            System.out.println(content); 
            System.out.println("---END---------------------------------------"); 

            Archive.GoString.ByValue input = Archive.GoString.fromValue(content);

            // Convert the response based on its pointer.
            Pointer ptrOutput = dll.ParseStarlarkCode(input);
            String strOutput = ptrOutput.getString(0);

            // REMEMBER:
            // Golang allocated the string, it's out job to free it. This isn't
            // automatic memory managed because C instantiated the variable! Freeing
            // the variable here.
            Native.free(Pointer.nativeValue(ptrOutput));

            System.out.println(""); 
            System.out.println("Parse function output sent from Golang:");
            System.out.println("---START-------------------------------------"); 
            System.out.println(strOutput); 
            System.out.println("---END---------------------------------------"); 

            System.out.println(""); 
            System.out.println("SUCCESSFULLY PARSED STARLARK FILE!"); 
        }
    }
}
