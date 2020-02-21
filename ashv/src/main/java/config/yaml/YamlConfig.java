package config.yaml;

import config.FileConfig;
import config.profile.ConfigProfile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

@Slf4j
@Singleton
public class YamlConfig {
    private FileConfig fileConfig;
    private Yaml yaml;

    private HashMap<String, ConfigProfile> configList;

    @Inject
    public YamlConfig(FileConfig fileConfig,
                      Yaml yaml,
                      @Named("ConfigList") HashMap<String, ConfigProfile> configList) {
        this.fileConfig = fileConfig;
        this.yaml = yaml;
        this.configList = configList;

        this.loadConfigsFromFs();
    }

    @SneakyThrows
    public void saveConfigToFile(ConfigProfile configuration) {
        FileWriter fileWriter = fileConfig.getFileWriter(FileConfig.CONFIGURATION_DIR,
                configuration.getConfigName());
        yaml.dump(configuration, fileWriter);

        fileWriter.close();

        loadConfigsFromFs();
    }

    @SneakyThrows
    public void deleteConfig(String configName) {
        fileConfig.removeFile(FileConfig.CONFIGURATION_DIR + FileConfig.FILE_SEPARATOR + configName);
    }

    public void loadConfigsFromFs() {
        configList.clear();

        try {
            fileConfig.listFilesInDirectory(FileConfig.CONFIGURATION_DIR).forEach(e -> {
                try {
                    this.configList
                            .put(e.getFileName().normalize().toString(), loadConfigurationFile(e));
                } catch (IOException ex) {
                    log.error(ex.getMessage());
                }
            });
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private ConfigProfile loadConfigurationFile(Path filePath) throws IOException {
        try (InputStream inStr = Files.newInputStream(filePath)) {
            return yaml.loadAs(inStr, ConfigProfile.class);
        }
    }

}
