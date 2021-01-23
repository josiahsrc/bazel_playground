package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class Main {
    public interface Archive extends Library {
        public void SayHello();

        public long Add(long a, long b);

        public double Cosine(double value);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from the Java code!"); 
        System.out.println("This is a dynamically linked example."); 

        File file = Native.extractFromResourcePath("/dynamic.so");
        System.out.println("EXTRACTED=" + file.toString()); 
        Archive archive = (Archive) Native.loadLibrary(file.toString(), Archive.class);

        archive.SayHello();

        System.out.println("GO says 5+6=" + archive.Add(5, 6)); 
        System.out.println("GO says Cos(5)=" + archive.Cosine(5)); 
    }
}
