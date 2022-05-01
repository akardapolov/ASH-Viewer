package config.dagger;

import java.util.Locale;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import org.yaml.snakeyaml.Yaml;

import config.FileConfig;
import config.GUIConfig;
import config.profile.ConfigProfile;
import core.manager.ColorManager;
import gui.events.GlobalKeyBindings;
import lombok.Getter;

public final class Config {
    private Preferences preferences;
    private static final String DEFAULT_LANGUAGE = "en";

    @Getter private GUIConfig guiConfig;
    @Getter private FileConfig fileConfig;
    @Getter private GlobalKeyBindings globalKeyBindings;
    @Getter private ColorManager colorManager;
    @Getter private Yaml yaml;
    @Getter private TreeMap<String, ConfigProfile> configTreeMap;

    public Config() {
        preferences = Preferences.userRoot().node("ASHViewer");
        guiConfig = new GUIConfig(preferences);
        fileConfig = new FileConfig();

        globalKeyBindings = new GlobalKeyBindings();
        colorManager = new ColorManager();

        yaml = new Yaml();
        configTreeMap = new TreeMap<>();
    }

    public void store() {
        preferences.put("language", DEFAULT_LANGUAGE);
        guiConfig.store();
    }

    public Locale getLocale() {
        return new Locale(DEFAULT_LANGUAGE);
    }
}
