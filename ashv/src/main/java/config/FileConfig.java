package config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileConfig {
    public static String FILE_SEPARATOR = System.getProperty("file.separator");
    private static String ABS_DIR = Paths.get(".").toAbsolutePath().normalize().toString();

    public static String REPOSITORY_DIR = ABS_DIR + FILE_SEPARATOR + "repository";
    public static String DATABASE_DIR = ABS_DIR + FILE_SEPARATOR + "database";

    public FileConfig() {
        try {
            setUpDirectory(DATABASE_DIR);
            setUpDirectory(REPOSITORY_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUpDirectory(String dirName) throws IOException {
        Files.createDirectories(Paths.get(dirName));
    }

    public void setUpFile(String profileFile) throws IOException {
        Path newFilePath = Paths.get(profileFile);
        if (!Files.exists(newFilePath)) {
            Files.createFile(newFilePath);
        }
    }

    public void removeFile(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
    }

    public void removeDirectory(String dirName) throws IOException {
        File tempFile = new File(dirName);
        tempFile.delete();
    }
}
