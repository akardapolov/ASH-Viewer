package gui.events;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@Slf4j
public class GlobalKeyBindings extends EventQueue {

    private final InputMap keyStrokes = new InputMap();
    private final ActionMap actions = new ActionMap();

    public void GlobalKeyBindings() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(this);
    }

    public InputMap getInputMap() {
        return this.keyStrokes;
    }

    public ActionMap getActionMap() {
        return this.actions;
    }

    protected void dispatchEvent(AWTEvent event) {
        KeyStroke ks;
        String actionKey;
        KeyEvent e;
        Action action;
        if (event instanceof KeyEvent && (actionKey = (String)this.keyStrokes.get(ks = KeyStroke.getKeyStrokeForEvent(e = (KeyEvent)event))) != null && (action = this.actions.get(actionKey)) != null && action.isEnabled()) {
            ActionEvent actionEvent = new ActionEvent(e.getSource(), e.getID(), actionKey, e.getModifiers());
            action.actionPerformed(actionEvent);
            return;
        }
        super.dispatchEvent(event);
    }

}
