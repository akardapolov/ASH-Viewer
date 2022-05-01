package config.dagger.module;

import config.FileConfig;
import config.GUIConfig;
import config.dagger.Config;
import config.profile.ConfigProfile;
import dagger.Module;
import dagger.Provides;
import gui.events.GlobalKeyBindings;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.TreeMap;

@Module
public class ModuleConfig {
    @Provides @Singleton Config getConfig() { return new Config(); }
    @Provides @Singleton GUIConfig forGUI() { return getConfig().getGuiConfig(); }
    @Provides @Singleton FileConfig forFileConfig() { return getConfig().getFileConfig(); }
    @Provides @Singleton GlobalKeyBindings getGlobalKeyBindings() { return getConfig().getGlobalKeyBindings(); }

    @Provides @Singleton Yaml getYaml() { return getConfig().getYaml(); }
    @Provides @Singleton @Named("ConfigList") TreeMap<String, ConfigProfile> getConfigList() { return getConfig().getConfigTreeMap(); }
}
