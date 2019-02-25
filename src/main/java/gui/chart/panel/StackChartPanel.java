package gui.chart.panel;

import config.Labels;
import core.ColorManager;
import gui.chart.CategoryTableXYDatasetRDA;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.IDetailPanel;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class StackChartPanel extends JPanel {
    private ColorManager colorManager;
    @Getter @Setter private CategoryTableXYDatasetRDA xyDatasetRDA;
    @Getter @Setter private String xAxisLabel;
    @Getter @Setter private String yAxisLabel;

    private JFreeChart jFreeChart;
    private DateAxis dateAxis;
    private ChartPanel chartPanel;

    @Getter @Setter private StackedChart stackedChart;

    private String name;

    public StackChartPanel(String name, ColorManager colorManager) {
        this.name = name;
        this.colorManager = colorManager;
    }

    public void initialize() {
        stackedChart = new StackedChart(this.colorManager, getChartPanel(xyDatasetRDA));
        stackedChart.initialize();

        this.setLayout(new BorderLayout());
        this.add("Center", stackedChart.getChartPanel());
    }

    public void requestFocusToEditor() {
        this.requestFocus();
    }

    public void changeHeightRectangularHeightRegionSelectionHandler(int oldH, int newH){
        stackedChart.changeHeightRectangularHeightRegionSelectionHandler(oldH, newH);
    }

    private ChartPanel getChartPanel(CategoryTableXYDatasetRDA categoryTableXYDataset) {
        dateAxis = new DateAxis();
        jFreeChart =
                ChartFactory.createStackedXYAreaChart(name, xAxisLabel,
                        Labels.getLabel("chart.main.ash"),categoryTableXYDataset,
                        PlotOrientation.VERTICAL,dateAxis,false,true,false);
        chartPanel = new ChartPanel(jFreeChart);

        return chartPanel;
    }

    public void addChartListenerReleaseMouse(IDetailPanel l){
        stackedChart.addChartListenerReleaseMouse(l);
    }

    public void removeChartListenerReleaseMouse(IDetailPanel l){
        stackedChart.addChartListenerReleaseMouse(l);
    }
}
