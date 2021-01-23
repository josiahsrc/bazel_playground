package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

public class Main {
    public interface Archive extends Library {
        public class GoString extends Structure {
            public static class ByValue extends GoString implements Structure.ByValue { }

            public String p;
            public long n;
            
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
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from the Java code!"); 
        System.out.println("This is a dynamically linked example."); 

        File file = Native.extractFromResourcePath("/dynamic.so");
        System.out.println("EXTRACTED=" + file.toString()); 
        Archive dll = (Archive) Native.load(file.toString(), Archive.class);

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
            Archive.GoString.ByValue stringToLog = new Archive.GoString.ByValue();
            stringToLog.p = "Hello, message sent to Go from Java!";
            stringToLog.n = stringToLog.p.length();

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
            long[] sorted = slice.data.getLongArray(0,nums.length);

            for(int i = 0; i < sorted.length; i++){
                System.out.print(sorted[i] + " ");
            }

            System.out.println("]");
        }
    }
}