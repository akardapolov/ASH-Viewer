package gui.actions;

import config.Labels;
import core.*;
import core.thread.ClearDatabaseThread;
import core.thread.SqlExecutorThread;
import gui.BasicFrame;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartStopAction implements ActionListener, StateTransitionListener {

    private BasicFrame jFrame;
    private JButton startStopButton;

    private StateMachine stateMachine;
    private SqlExecutorThread sqlExecutorThread;
    private ClearDatabaseThread clearDatabaseThread;

    String[] buttonTexts = new String[CurrrentState.values().length];

    @Inject
    public StartStopAction(BasicFrame jFrame,
                           StateMachine stateMachine,
                           SqlExecutorThread sqlExecutorThread,
                           ClearDatabaseThread clearDatabaseThread){
        this.jFrame = jFrame;
        this.stateMachine = stateMachine;
        this.sqlExecutorThread = sqlExecutorThread;
        this.clearDatabaseThread = clearDatabaseThread;

        stateMachine.addTransitionListener(this);

        buttonTexts[CurrrentState.START.ordinal()] = Labels.getLabel("button.stop");
        buttonTexts[CurrrentState.STOP.ordinal()] = Labels.getLabel("button.start");
    }

    public void setStartStopButton(JButton startStopButton){
        this.startStopButton = startStopButton;
        this.startStopButton.addActionListener(this);

        CurrrentState state = stateMachine.getState();
        this.startStopButton.setText(buttonTexts[state.ordinal()]);
        this.startStopButton.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) { stateMachine.transitionToNext(); }

    @Override
    public void transitionTo(CurrrentState state) {
        switch (state) {
            case START:
                this.startStopButton.setEnabled(false);
                this.stateMachine.startScanning();
                this.sqlExecutorThread.startIt();
                this.clearDatabaseThread.schedulerTimer();
                this.startStopButton.setEnabled(true);
                break;
            case STOP:
                this.startStopButton.setEnabled(false);
                this.stateMachine.stopScanning();
                this.sqlExecutorThread.stopIt();
                this.startStopButton.setEnabled(true);
                break;
        }

        this.startStopButton.setText(buttonTexts[state.ordinal()]);
    }

}
