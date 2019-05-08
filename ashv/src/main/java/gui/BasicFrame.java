package gui;

import config.GUIConfig;
import gui.events.GlobalKeyBindings;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class BasicFrame extends JFrame implements LayoutManager {
    GUIConfig guiConfig;
    GlobalKeyBindings globalKeyBindings;

    private MainTabbedPane tabPane = null;

    public BasicFrame(GUIConfig guiConfig, GlobalKeyBindings globalKeyBindings) {
        log.info("Start instantiating new BasicFrame");

        this.guiConfig = guiConfig;
        this.globalKeyBindings = globalKeyBindings;

        this.setLayout(new BorderLayout());

        init_gui();
        init_controller();
    }

    private void init_gui(){ }
    private void init_controller(){ }

    private void initializeGlobalKeyBindings() {
        class PrevTab
                extends javax.swing.AbstractAction {
            final BasicFrame mainFrame;

            public PrevTab(BasicFrame mainFrame, String name) {
                super(name);
                this.mainFrame = mainFrame;
            }

            public void actionPerformed(java.awt.event.ActionEvent e) {
                tabPane.callPreviousTab();
            }
        }
        class NextTab
                extends javax.swing.AbstractAction {
            final BasicFrame mainFrame;

            public NextTab(BasicFrame mainFrame, String name) {
                super(name);
                this.mainFrame = mainFrame;
            }

            public void actionPerformed(java.awt.event.ActionEvent e) {
                tabPane.callNextTab();
            }
        }
        class ToggleTab
                extends javax.swing.AbstractAction {
            final BasicFrame mainFrame;

            public ToggleTab(BasicFrame mainFrame, String name) {
                super(name);
                this.mainFrame = mainFrame;
            }

            public void actionPerformed(java.awt.event.ActionEvent e) {
                tabPane.toggleTab();
            }
        }
        InputMap inputMap = globalKeyBindings.getInputMap();
        ActionMap actionMap = globalKeyBindings.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(192, 2), "toggleTab");
        actionMap.put("toggleTab", new ToggleTab(this, "toggleTab"));
        inputMap.put(KeyStroke.getKeyStroke(9, 2), "nextTab");
        actionMap.put("nextTab", new NextTab(this, "nextTab"));
        inputMap.put(KeyStroke.getKeyStroke(9, 3), "prevTab");
        actionMap.put("prevTab", new PrevTab(this, "prevTab"));
    }
    
    /**
     * Assemble all high level modules
     *
     * @param container
     * @param resides: BorderLayout.NORTH, BorderLayout.CENTER, BorderLayout.SOUTH
     */
    public void addProfileArea(Container container, String resides){
        this.add(container, resides);
        if (resides.equalsIgnoreCase(BorderLayout.CENTER)){
            tabPane = (MainTabbedPane) container;
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {}

}
