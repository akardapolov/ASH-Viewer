package config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileConfig {
    public static String FILE_SEPARATOR = System.getProperty("file.separator");
    private static String ABS_DIR = Paths.get(".").toAbsolutePath().normalize().toString();

    public static String CONFIGURATION_DIR = ABS_DIR + FILE_SEPARATOR + "configuration";
    public static String DATABASE_DIR = ABS_DIR + FILE_SEPARATOR + "database";

    public FileConfig() {
        try {
            setUpDirectory(CONFIGURATION_DIR);
            setUpDirectory(DATABASE_DIR);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(String.valueOf(e.getStackTrace()));
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

    public List<Path> listFilesInDirectory(String dirName) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            return paths.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    public FileWriter getFileWriter(String dirName, String fileName) throws IOException {
        return new FileWriter(dirName + FILE_SEPARATOR + fileName);
    }

    public void removeFile(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
    }

    public void removeDirectory(String dirName) throws IOException {
        File tempFile = new File(dirName);
        tempFile.delete();
    }
}
