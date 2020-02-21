package config.dagger;

import config.FileConfig;
import config.GUIConfig;
import config.profile.ConfigProfile;
import core.manager.ColorManager;
import gui.events.GlobalKeyBindings;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Locale;
import java.util.prefs.Preferences;

public final class Config {
    private Preferences preferences;
    public String language = "en";

    @Getter private GUIConfig guiConfig;
    @Getter private FileConfig fileConfig;
    @Getter private GlobalKeyBindings globalKeyBindings;
    @Getter private ColorManager colorManager;
    @Getter private Yaml yaml;
    @Getter private HashMap<String, ConfigProfile> configHashMap;

    public Config() {
        preferences = Preferences.userRoot().node("ASHViewer");
        guiConfig = new GUIConfig(preferences);
        fileConfig = new FileConfig();

        globalKeyBindings = new GlobalKeyBindings();
        colorManager = new ColorManager();

        yaml = new Yaml();
        configHashMap = new HashMap<>();
    }

    public void store() {
        preferences.put("language", language);
        guiConfig.store();
    }

    public Locale getLocale() {
        return new Locale(language);
    }
}
