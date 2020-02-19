package config.yaml;

import config.FileConfig;
import config.profile.ConfigProfile;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
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

        this.loadConfigFromFs();
    }

    public void loadConfigToFile(ConfigProfile configuration) {
        try {
            yaml.dump(configuration, fileConfig.getFileWriter(FileConfig.CONFIGURATION_DIR,
                    configuration.getConfigName()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loadConfigFromFs() {
        try {
                fileConfig.listFilesInDirectory(FileConfig.CONFIGURATION_DIR).forEach(e -> {
                    try {
                        this.configList
                                .put(e.getFileName().normalize().toString(), loadConfigurationFile(e));
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                });
            } catch(IOException ex){
                log.error(ex.getMessage());
        }
    }

    private ConfigProfile loadConfigurationFile(Path filePath) throws IOException {
        try (InputStream inStr = Files.newInputStream(filePath)) {
            return yaml.loadAs(inStr, ConfigProfile.class);
        }
    }

}
