package gui.actions;


import core.manager.ConfigurationManager;
import gui.BasicFrame;
import gui.connect.ConnectToDbArea;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@Singleton
public class ConnectToDbAction implements ActionListener {
    private BasicFrame jFrame;
    private ConfigurationManager configurationManager;
    private JButton connectToDbButton;

    private ConnectToDbArea connectToDbArea;

    @Inject
    public ConnectToDbAction(BasicFrame jFrame,
                             ConfigurationManager configurationManager,
                             ConnectToDbArea connectToDbArea){
        this.jFrame = jFrame;
        this.configurationManager = configurationManager;
        this.connectToDbArea = connectToDbArea;
    }

    public void setConnectToDbButton(JButton connectToDbButton){
        this.connectToDbButton = connectToDbButton;
        this.connectToDbButton.addActionListener(this);
    }

    public void closeConnection(){
        this.configurationManager.closeCurrentProfile();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.connectToDbArea.setLocationRelativeTo(this.jFrame);
        this.connectToDbArea.setVisible(true);
        log.info("ConnectToDbAction button pressed");
    }
}
