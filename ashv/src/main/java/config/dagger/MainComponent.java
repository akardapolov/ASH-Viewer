package config.dagger;

import config.dagger.module.ModuleComponent;
import config.dagger.module.ModuleConfig;
import dagger.Component;
import gui.MainWindow;

import javax.inject.Singleton;

@Component(modules = {
        ModuleConfig.class,
        ModuleComponent.class
})

@Singleton
public interface MainComponent {
    MainWindow createMainWindow();
}
