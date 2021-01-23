package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class Main {
    public interface Archive extends Library {
        public void SayHello();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from the Java code!"); 
        System.out.println("This is a dynamically linked example."); 

        File file = Native.extractFromResourcePath("/dynamic.so");
        System.out.println("EXTRACTED=" + file.toString()); 
        Archive archive = (Archive) Native.loadLibrary(file.toString(), Archive.class);

        archive.SayHello();
    }
}
