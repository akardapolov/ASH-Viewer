package config.dagger.module;

import config.FileConfig;
import config.GUIConfig;
import config.dagger.Config;
import dagger.Module;
import dagger.Provides;
import gui.events.GlobalKeyBindings;
import store.BerkleyDB;

import javax.inject.Singleton;

@Module
public class ModuleConfig {
    @Provides @Singleton Config getConfig() { return new Config(); }
    @Provides @Singleton GUIConfig forGUI() { return getConfig().getGuiConfig(); }
    @Provides @Singleton FileConfig forFileConfig() { return getConfig().getFileConfig(); }
    @Provides @Singleton BerkleyDB bdbForRepository() { return getConfig().getBdbForRepository(); }
    @Provides @Singleton GlobalKeyBindings getGlobalKeyBindings() { return getConfig().getGlobalKeyBindings(); }

    /*@Provides @Singleton ProfileConfig forProfile() { return getConfig().getProfileConfig(); }
    @Provides @Singleton XstreamSerializer forXstreamSerializer() { return getConfig().getXstreamSerializer(); }
    @Provides @Singleton CacheManager forCacheManager() { return getConfig().getCacheManager(); }
    @Provides @Singleton ColorManager forColorManager() { return getConfig().getColorManager(); }

    @Provides @Singleton EnvironmentConfig forEnvironmentConfig() { return getConfig().getEnvConfig(); }
    @Provides @Singleton Environment forEnvironment() { return getConfig().getEnv(); }
    @Provides @Singleton StoreConfig forStoreConfig() { return getConfig().getStoreConfig(); }
    @Provides @Singleton EntityStore forStore() { return getConfig().getStore(); }*/
}
