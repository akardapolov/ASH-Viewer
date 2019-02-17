package gui;

import javax.swing.*;
import java.awt.*;

public class StatusBar
extends JLabel implements UICallback {
    private static final long serialVersionUID = 1;
    private float loadResultTime;
    private float executeTime;
    private int count;

    public StatusBar() {
        this("");
    }

    public StatusBar(String status) {
        super(" " + status);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void setStatus(String status) {
        this.setText(" " + status);
    }

    public String getStatus() {
        return this.getText().trim();
    }

    public Dimension getPreferredSize() {
        int height = super.getPreferredSize().height;
        int width = this.getParent() != null ? this.getParent().getWidth() : super.getPreferredSize().width;
        return new Dimension(width, height);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
        this.updateLabelString();
    }

    public float getExecuteTime() {
        return this.executeTime;
    }

    public void setExecuteTime(float executeTime) {
        this.executeTime = executeTime;
        this.updateLabelString();
    }

    public float getLoadResultTime() {
        return this.loadResultTime;
    }

    public void setLoadResultTime(float loadResultTime) {
        this.loadResultTime = loadResultTime;
        this.updateLabelString();
    }

    public float getTotalTime() {
        return this.executeTime + this.loadResultTime;
    }

    private void updateLabelString() {
        this.setStatus("Total Time: " + this.getTotalTime() + " sec - Process Time: " + this.executeTime + " sec" + " - Load Results Time: " + this.loadResultTime + " sec - Count for Query: " + this.count);
    }

    @Override
    public void startLoading() {
        this.setStatus("startLoading");
    }

    @Override
    public void stopLoading() {
        this.setStatus("stopLoading");
    }

    @Override
    public void setProgress(int progressPercent) { }

    @Override
    public void showError(String message) { }
}

