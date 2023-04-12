package config.dagger;

import config.FileConfig;
import config.GUIConfig;
import config.profile.ConfigProfile;
import core.manager.ColorManager;
import gui.events.GlobalKeyBindings;
import java.util.Locale;
import java.util.TreeMap;
import java.util.prefs.Preferences;
import lombok.Getter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

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

        Constructor constructor = new Constructor(new LoaderOptions());
        constructor.addTypeDescription(new TypeDescription(ConfigProfile.class, "!ConfigProfile"));

        DumperOptions dumperOptions = new DumperOptions();
        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(ConfigProfile.class, new Tag("!ConfigProfile"));
        yaml = new Yaml(constructor, representer);

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
