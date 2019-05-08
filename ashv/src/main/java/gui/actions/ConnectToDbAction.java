package gui.actions;


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
    private JButton connectToDbButton;

    private ConnectToDbArea connectToDbArea;

    @Inject
    public ConnectToDbAction(BasicFrame jFrame,
                             ConnectToDbArea connectToDbArea){
        this.jFrame = jFrame;
        this.connectToDbArea = connectToDbArea;
    }

    public void setConnectToDbButton(JButton connectToDbButton){
        this.connectToDbButton = connectToDbButton;
        this.connectToDbButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.connectToDbArea.setLocationRelativeTo(this.jFrame);
        this.connectToDbArea.setVisible(true);
        log.info("ConnectToDbAction button pressed");
    }
}
