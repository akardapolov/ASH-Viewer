package gui;

import config.GUIConfig;
import gui.actions.ConnectToDbAction;
import gui.actions.StartStopAction;
import lombok.extern.slf4j.Slf4j;
import store.StoreManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
@Singleton
public class MainWindow {
    private final BasicFrame jFrame;
    private final GUIConfig guiConfig;

    private JToolBar mainToolBar;
    private JButton connectButton;
    private JButton startStopButton;
    private MainTabbedPane mainTabPane;

    private ConnectToDbAction connectToDbAction;
    private StartStopAction startStopAction;

    private StoreManager storeManager;

    private StatusBar mainStatusBar;

    @Inject
    public MainWindow(BasicFrame jFrame,
                      GUIConfig guiConfig,
                      ConnectToDbAction connectToDbAction,
                      StartStopAction startStopAction,
                      StoreManager storeManager,
                      @Named("mainJToolBar") JToolBar mainToolBar,
                      @Named("connectToDbButton") JButton connectButton,
                      @Named("startStopButton") JButton startStopButton,
                      @Named("mainTabPane") MainTabbedPane mainTabPane,
                      @Named("mainStatusBar") StatusBar mainStatusBar){
        this.jFrame = jFrame;
        this.guiConfig = guiConfig;
        this.connectToDbAction = connectToDbAction;
        this.startStopAction = startStopAction;
        this.storeManager = storeManager;

        this.initJFrame(this.jFrame);

        this.mainToolBar = mainToolBar;
        this.connectButton = connectButton;
        this.startStopButton = startStopButton;
        this.mainTabPane = mainTabPane;
        this.mainStatusBar = mainStatusBar;

        this.mainToolBar.add(connectButton);

        this.jFrame.addProfileArea(this.mainToolBar, BorderLayout.NORTH);
        this.jFrame.addProfileArea(this.mainTabPane, BorderLayout.CENTER);
        this.jFrame.addProfileArea(this.mainStatusBar, BorderLayout.SOUTH);

        this.connectToDbAction.setConnectToDbButton(this.connectButton);
        this.startStopAction.setStartStopButton(this.startStopButton);

        this.connectButton.doClick();
    }

    private void initJFrame(final BasicFrame jFrame){
        jFrame.setTitle("ASH Viewer");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(guiConfig.getMainWindowSize());

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                guiConfig.setMainWindowSize(jFrame.getSize());
                connectToDbAction.closeConnection();
                storeManager.syncBdb();
                storeManager.closeDb();
                System.exit(0);
            }
        });
    }
}
