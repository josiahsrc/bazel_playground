package main;

import com.sun.jna.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class Main {
    // public interface Archive extends Library {
    //     public void SayHello();
    // }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from the Java code!"); 
        System.out.println("This is a dynamically linked example."); 

        // {
        //     Path path = Paths.get("/dynamic.so");

        //     System.out.println("DLL PATH=" + path);
        //     System.out.println("DLL ABS path=" + path.toAbsolutePath()); 
        //     System.out.println("DLL exists=" + Files.exists(path)); 
        // }

        {
            // URI uri = Main.class.getClassLoader().getResource("dynamic.so").toURI();

            // Map<String, String> env = new HashMap<>();
            // String[] array = uri.toString().split("!");
            // FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);

            // for (Path p : fs.getRootDirectories()) {
            //     System.out.println("LOOP p=" + p.toString()); 
            // }

            // Path path = fs.getPath(array[1]);
            // System.out.println("URI=" + array[0]);
            // System.out.println("DLL PATH=" + path);
            // System.out.println("DLL ABS path=" + path.toAbsolutePath()); 
            // System.out.println("DLL exists=" + Files.exists(path)); 

            // String stupid = "/Users/josiahsaunders/school/CS481/repos/bazel_playground/03_bazel_go_starlark_dlls/src/main/resources";
            // System.setProperty("jna.boot.library.path", stupid + "/dynamic.so");
            // System.out.println("JNA PATH=" + System.getProperty("jna.boot.library.path"));

            // Main.loadLibraryFromJar("/dynamic.so");

            // Archive archive = (Archive) Native.loadLibrary("dynamic.so", Archive.class);     
        }

        // String stupidPath = "src/main/resources/dynamic.so";
        // Archive archive = (Archive) Native.loadLibrary(stupidPath, Archive.class);        

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

        // File file = Native.extractFromResourcePath("/dynamic.so");
        // System.out.println("EXTRACTED=" + file.toString()); 
        // Archive archive = (Archive) Native.loadLibrary(file.toPath().toString(), Archive.class);
    }

    /**
     * The minimum length a prefix for a file has to have according to {@link File#createTempFile(String, String)}}.
     */
    private static final int MIN_PREFIX_LENGTH = 3;
    public static final String NATIVE_FOLDER_PATH_PREFIX = "nativeutils";

    /**
     * Temporary directory which will contain the DLLs.
     */
    private static File temporaryDir;

    /**
     * Loads library from current JAR archive
     * 
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after
     * exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     * 
     * @param path The path of file inside JAR as absolute path (beginning with '/'), e.g. /package/File.ext
     * @throws IOException If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters
     * (restriction of {@link File#createTempFile(java.lang.String, java.lang.String)}).
     * @throws FileNotFoundException If the file could not be found inside the JAR.
     */
    public static String loadLibraryFromJar(String path) throws IOException {
 
        if (null == path || !path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
 
        // Obtain filename from path
        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
 
        // Check if the filename is okay
        if (filename == null || filename.length() < MIN_PREFIX_LENGTH) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }
 
        // Prepare temporary file
        if (temporaryDir == null) {
            temporaryDir = createTempDirectory(NATIVE_FOLDER_PATH_PREFIX);
            temporaryDir.deleteOnExit();
        }

        File temp = new File(temporaryDir, filename);

        try (InputStream is = Main.class.getResourceAsStream(path)) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            temp.delete();
            throw e;
        } catch (NullPointerException e) {
            temp.delete();
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }

        try {
            System.load(temp.getAbsolutePath());
        } finally {
            if (isPosixCompliant()) {
                // Assume POSIX compliant file system, can be deleted after loading
                temp.delete();
            } else {
                // Assume non-POSIX, and don't delete until last file descriptor closed
                temp.deleteOnExit();
            }
        }

        return temp.toString();
    }

    private static boolean isPosixCompliant() {
        try {
            return FileSystems.getDefault()
                    .supportedFileAttributeViews()
                    .contains("posix");
        } catch (FileSystemNotFoundException
                | ProviderNotFoundException
                | SecurityException e) {
            return false;
        }
    }

    private static File createTempDirectory(String prefix) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File generatedDir = new File(tempDir, prefix + System.nanoTime());
        
        if (!generatedDir.mkdir())
            throw new IOException("Failed to create temp directory " + generatedDir.getName());
        
        return generatedDir;
    }
}
