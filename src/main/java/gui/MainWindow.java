package gui;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import config.GUIConfig;
import gui.actions.ConnectToDbAction;
import gui.actions.StartStopAction;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
@AutoFactory
public class MainWindow {
    private final BasicFrame jFrame;
    private final GUIConfig guiConfig;

    private JToolBar mainToolBar;
    private JButton connectButton;
    private JButton startStopButton;
    private MainTabbedPane mainTabPane;

    private ConnectToDbAction connectToDbAction;
    private StartStopAction startStopAction;

    private StatusBar mainStatusBar;

    @Inject
    public MainWindow(BasicFrame jFrame,
                      GUIConfig guiConfig,
                      ConnectToDbAction connectToDbAction,
                      StartStopAction startStopAction,
                      @Provided @Named("mainJToolBar") JToolBar mainToolBar,
                      @Provided @Named("connectToDbButton") JButton connectButton,
                      @Provided @Named("startStopButton") JButton startStopButton,
                      @Provided @Named("mainTabPane") MainTabbedPane mainTabPane,
                      @Provided @Named("mainStatusBar") StatusBar mainStatusBar){
        this.jFrame = jFrame;
        this.guiConfig = guiConfig;
        this.connectToDbAction = connectToDbAction;
        this.startStopAction = startStopAction;

        this.initJFrame(this.jFrame);

        this.mainToolBar = mainToolBar;
        this.connectButton = connectButton;
        this.startStopButton = startStopButton;
        this.mainTabPane = mainTabPane;
        this.mainStatusBar = mainStatusBar;

        this.mainToolBar.add(connectButton);
        this.mainToolBar.add(startStopButton);

        this.jFrame.addProfileArea(this.mainToolBar, BorderLayout.NORTH);
        this.jFrame.addProfileArea(this.mainTabPane, BorderLayout.CENTER);
        this.jFrame.addProfileArea(this.mainStatusBar, BorderLayout.SOUTH);

        this.connectToDbAction.setConnectToDbButton(this.connectButton);
        this.startStopAction.setStartStopButton(this.startStopButton);

        this.connectButton.doClick();
    }

    private void initJFrame(final BasicFrame jFrame){
        //jFrame.setLayout(new MigLayout("", "[fill, grow]", "[fill, grow]"));
        jFrame.setTitle("ASH Viewer");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(guiConfig.getMainWindowSize());

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                guiConfig.setMainWindowSize(jFrame.getSize());
                //////storeManager.close();
                System.exit(0);
            }
        });
    }
}
