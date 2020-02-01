import config.dagger.DaggerMainComponent;
import config.dagger.MainComponent;
import gui.MainWindow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String... args) {
        System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");

        log.info("Start application");

        MainComponent mainComponent = DaggerMainComponent.create();

        // create the main window using dependency injection
        MainWindow mainWindow = mainComponent.createMainWindow();
    }
}
