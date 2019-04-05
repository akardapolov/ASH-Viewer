package config.dagger;

import config.FileConfig;
import config.GUIConfig;
import core.ColorManager;
import gui.events.GlobalKeyBindings;
import lombok.Getter;
import store.BerkleyDB;

import java.util.Locale;
import java.util.prefs.Preferences;

public final class Config {
    private Preferences preferences;
    public String language = "en";

    @Getter
    private GUIConfig guiConfig;
    @Getter
    private FileConfig fileConfig;
    @Getter
    private BerkleyDB bdbForRepository;
    @Getter
    private GlobalKeyBindings globalKeyBindings;
    @Getter
    private ColorManager colorManager;

    public Config() {
        preferences = Preferences.userRoot().node("ASHViewer");
        guiConfig = new GUIConfig(preferences);
        fileConfig = new FileConfig();

        bdbForRepository = new BerkleyDB(FileConfig.REPOSITORY_DIR);
        bdbForRepository.getEnvConfig().setCachePercent(10);

        globalKeyBindings = new GlobalKeyBindings();
        colorManager = new ColorManager();
    }

    public void store() {
        preferences.put("language", language);
        guiConfig.store();
    }

    public Locale getLocale() {
        return new Locale(language);
    }

}
