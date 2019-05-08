package config;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.prefs.Preferences;

@Slf4j
public class GUIConfig {

    private Preferences preferences;
    private Dimension mainWindowSize;
    private int mainSplitPanelLoc;

    public GUIConfig(Preferences preferences) {
        this.preferences = preferences;
        load();
    }

    private void load() {
        mainWindowSize = new Dimension(
                preferences.getInt("windowWidth", 800),
                preferences.getInt("windowHeight", 460));
        mainSplitPanelLoc = preferences.getInt("mainSplitPanelLoc", 200);

    }

    public void store() {
        preferences.putInt("windowWidth", (int) mainWindowSize.getWidth());
        preferences.putInt("windowHeight", (int) mainWindowSize.getHeight());
    }

    public Dimension getMainWindowSize() { return mainWindowSize; }

    public void setMainWindowSize(Dimension size) {
        mainWindowSize = size;
        store();
    }

    public int getMainSplitPanelLoc(){ return mainSplitPanelLoc;}

    public void setMainSplitPanelLoc(int loc){
        mainSplitPanelLoc = loc;
        preferences.putInt("mainSplitPanelLoc", loc);
    }

}
