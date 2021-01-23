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

        // TODO: Figure out why it's impossible to get a reference to a file in java :)
        Path path = Paths.get("/dynamic.so");

        System.out.println("DLL PATH=" + path);
        System.out.println("DLL ABS path=" + path.toAbsolutePath()); 
        System.out.println("DLL exists=" + Files.exists(path)); 

        // Archive archive = (Archive) Native.loadLibrary(path.toString(), Archive.class);        

        // File file = Paths.get(uri).toFile();

        // URI uri;
        // try {
        //     uri = Main.class.getClassLoader().getResource("dynamic.so").toURI();
        // } catch(Exception e) {
        //     System.out.println("FAILED TO LOAD URI."); 
        //     System.out.println(e); 
        //     return;
        // }

        // // try {
        // //     Map<String, String> env = new HashMap<>(); 
        // //     env.put("create", "true");
        // //     FileSystem zipfs = FileSystems.newFileSystem(uri, env);
        // // }  catch(Exception e) {
        // //     System.out.println("FAILED TO LOAD FILE SYSTEM."); 
        // //     System.out.println(e); 
        // //     return;
        // // }

        // Path path = Paths.get(uri);
        // System.out.println("DLL ABS path=" + path.toAbsolutePath()); 
        // System.out.println("DLL exists=" + Files.exists(path)); 

        // // System.setProperty("jna.library.path", path.toString());
        // // System.out.println("library path: " + System.getProperty("jna.library.path"));

        // File absFile = path.toFile();
        // System.out.println("ABS PATH=" + absFile); 

        // Archive archive = (Archive) Native.loadLibrary(path.toString(), Archive.class);
    }
}
