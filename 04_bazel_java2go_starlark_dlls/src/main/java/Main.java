package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;
import com.example.myprotos.*;

// Examples taken from this helpful link:
// https://github.com/vladimirvivien/go-cshared-examples/blob/master/Client.java
//
// NOTE:
// It may also be possible to do complex communication without using protobufs.
// The JNA api allows for complex structures. The downside is that it looks hard
// to manage and you'll have to define the structures on both the consumer and
// producer side. Yuck! This is worth considering though. It could allow for a
// speed up, assuming speed is an issue. See link in readme.
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

        public Pointer ParseStarlarkCode(GoString.ByValue in);
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
            // Load a starlark file's content
            String fileName = "/example.star";
            String content;
            {
                InputStream fileStream = Main.class.getResourceAsStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }

                content = builder.toString();
            }

            System.out.println("Raw " + fileName + " starlark file content:");
            System.out.println("---START-------------------------------------"); 
            System.out.println(content); 
            System.out.println("---END---------------------------------------"); 

            // Prepare the input for protobufs.
            StarlarkParsing.ParseInput parseInput;
            {
                StarlarkParsing.ParseInput.Builder inputBuilder = StarlarkParsing.ParseInput.newBuilder();
                inputBuilder.setFilename(fileName);
                inputBuilder.setContent(content);
                inputBuilder.setId(-300);

                parseInput = inputBuilder.build();
            }
            
            // Create a shared memory byte array and pass it to the go program.
            // String protobufMessage = new String(parseInput.toByteArray());
            int length = parseInput.toString().length();
            System.out.println("Protobuf message length (Java): " + length);
            System.out.println("FIELDS (Java):");
            System.out.println("filename: " + parseInput.getFilename());
            System.out.println("content: " + parseInput.getContent());
            System.out.println("id: " + parseInput.getId());

            /*
            // Setup the input. This memory must be allocated because we are sending
            // a byte array to CGo. This byte array is the marshalled version of our
            // protobuffer. Using a byte array is necessary because the lenght must
            // be explicitly defined. Otherwise, if we treat it as a string, the proto
            // may exit early when marshaling because an "\0" was reached.
            byte[] inputArr = parseInput.toByteArray();
            int inputSize = inputArr.length;
            Pointer inputPtr = new Memory(inputSize);
            inputPtr.write(0, inputArr, 0, inputSize);
            */

            // Setup the input. Encode as a base64 to avoid null-terminating characters.
            // Using bytes would be more performant. The base64 encoding will be 133% the
            // size had we just used bytes.
            String rawInput = Base64.getEncoder().encodeToString(parseInput.toByteArray());

            // The message we are sending is for cgo.
            Archive.GoString.ByValue goRawInput = Archive.GoString.fromValue(rawInput);

            System.out.println(""); 
            System.out.println("MEMORY (Java):"); 
            System.out.println("length: " + rawInput.length()); 

            // Call the parser. The output will be a pointer to an already allocated string.
            // We must "dereference" the string and store it locally.
            Pointer rawPtrOutput = dll.ParseStarlarkCode(goRawInput);
            String rawOutput = rawPtrOutput.getString(0);

            // NOTICE:
            // Golang allocated some memory, it's our job to free it. This isn't
            // automatic memory managed because C instantiated the variable! Freeing
            // the memory here.
            Native.free(Pointer.nativeValue(rawPtrOutput));

            System.out.println(""); 
            System.out.println("Loading protobuf output sent from Golang to Java:");
            System.out.println("---START-------------------------------------"); 

            // Get our output. This was sent from Golang.
            byte[] outputBytes = Base64.getDecoder().decode(rawOutput);
            StarlarkParsing.ParseOutput output = StarlarkParsing.ParseOutput.parseFrom(outputBytes);

            System.out.println("output:"); 
            System.out.println("message: " + output.getMessage()); 
            System.out.println("success: " + output.getSuccess()); 

            System.out.println("---END---------------------------------------"); 
            System.out.println(""); 
            System.out.println("SUCCESSFULLY PARSED STARLARK FILE!"); 
        }
    }
}
