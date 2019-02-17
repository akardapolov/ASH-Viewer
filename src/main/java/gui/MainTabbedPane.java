package gui;

import lombok.extern.slf4j.Slf4j;

import javax.accessibility.AccessibleContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

@Slf4j
@Singleton
public class MainTabbedPane extends JTabbedPane {
    private final BasicFrame jFrame;

    private int currentSelectedIndex = 0;
    private int previousIndexSelected = 0;

    @Inject
    public MainTabbedPane(BasicFrame jFrame) {
        this.jFrame = jFrame;

        this.addChangeListener(e -> {
            MainTabbedPane.prevN(MainTabbedPane.this, MainTabbedPane.this.currentSelectedIndex);
            MainTabbedPane.currN(MainTabbedPane.this, MainTabbedPane.this.getSelectedIndex());
        });
    }

    public void toggleTab() {
        this.setSelectedIndex(this.previousIndexSelected);
    }

    public void callNextTab() {
        int selected = this.getSelectedIndex();
        if (selected < this.getComponentCount() - 1) {
            this.setSelectedIndex(++selected);
        } else {
            this.setSelectedIndex(0);
        }
    }

    public void callPreviousTab() {
        int selected = this.getSelectedIndex();
        int size = this.getComponentCount();
        if (selected > 0) {
            this.setSelectedIndex(--selected);
        } else {
            this.setSelectedIndex(size - 1);
        }
    }

    public AccessibleContext getAccessibleContext() {
        return this.accessibleContext;
    }

    static void prevN(MainTabbedPane mainTabbedPane, int n) { mainTabbedPane.previousIndexSelected = n; }
    static void currN(MainTabbedPane mainTabbedPane, int n) { mainTabbedPane.currentSelectedIndex = n; }
}
