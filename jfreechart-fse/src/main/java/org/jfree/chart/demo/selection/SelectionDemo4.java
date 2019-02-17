/* -------------------
 * SelectionDemo4.java
 * -------------------
 * (C) Copyright 2004-2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.FreePathSelectionHandler;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RefineryUtilities;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.extension.impl.XYDatasetSelectionExtension;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

/**
 * A demo of the {@link HistogramDataset} class.
 */
public class SelectionDemo4 extends ApplicationFrame implements
          SelectionChangeListener<XYCursor> {

    private SimpleHistogramDataset dataset;
    private DefaultTableModel model;
    private JTable table;

    /**
     * Creates a new demo.
     * 
     * @param title  the frame title.
     */
    public SelectionDemo4(String title) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        this.dataset = (SimpleHistogramDataset) plot.getDataset();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(chartPanel);
          
        this.model = new DefaultTableModel(new String[] { "Item:",
                    "Bin Start:", "Bin End:", "Value:" }, 0);
        this.table = new JTable(this.model);
        JPanel p = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(this.table);
        p.add(scroller);
        p.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(
                    "Selected Items: "), new EmptyBorder(4, 4, 4, 4)));
        split.add(p);
        setContentPane(split);
    }
     
    /**
     * The selection changed, so we change the table model.
     * 
     * @param event
     */
    public void selectionChanged(SelectionChangeEvent<XYCursor> event) {
        while (this.model.getRowCount() > 0) {
            this.model.removeRow(0);
        }

        XYDatasetSelectionExtension ext = (XYDatasetSelectionExtension) event
                .getSelectionExtension();
        DatasetIterator<XYCursor> iter = ext.getSelectionIterator(true);

        while (iter.hasNext()) {
            XYCursor dc = iter.next();
            this.model.addRow(new Object[] { new Integer(dc.item),
                    this.dataset.getStartX(dc.series, dc.item),
                    this.dataset.getEndX(dc.series, dc.item),
                    this.dataset.getY(dc.series, dc.item) });
        }
    }

    /**
     * Creates a sample {@link HistogramDataset}.
     * 
     * @return the dataset.
     */
    private static IntervalXYDataset createDataset() {
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("H1");
        double lower = 0.0;
        for (int i = 0; i < 100; i++) {
            double upper = (i + 1) / 10.0;
            SimpleHistogramBin bin = new SimpleHistogramBin(lower, upper, true,
                       false);
            dataset.addBin(bin);
            lower = upper;
        }
        double[] values = new double[1000];
        Random generator = new Random(12345678L);
        for (int i = 0; i < 1000; i++) {
             values[i] = generator.nextGaussian() + 5;
        }
        dataset.addObservations(values);
        return dataset;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(IntervalXYDataset dataset, 
            DatasetSelectionExtension<XYCursor> ext) {
        JFreeChart chart = ChartFactory.createHistogram("SelectionDemo4", null,
                  null, dataset);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setForegroundAlpha(0.85f);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setDefaultOutlinePaint(Color.red);
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setShadowVisible(false);
          
        //add selection specific rendering
        IRSUtilities.setSelectedItemPaint(renderer, ext, Color.white);
          
        //register plot as selection change listener
        ext.addChangeListener(plot);
          
        return chart;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public final JPanel createDemoPanel() {
        IntervalXYDataset xydataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<XYCursor> datasetExtension 
                = new XYDatasetSelectionExtension(xydataset);     
        datasetExtension.addChangeListener(this);
          
        //standard setup
        JFreeChart chart = createChart(xydataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        // add a selection handler
        RegionSelectionHandler selectionHandler 
                = new FreePathSelectionHandler();
        panel.addMouseHandler(selectionHandler);
        panel.addMouseHandler(new MouseClickSelectionHandler());
        panel.removeMouseHandler(panel.getZoomHandler());
          
        // add a selection manager with intersection selection
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);
          
        EntitySelectionManager selectionManager = new EntitySelectionManager(
                panel, new Dataset[] { xydataset }, dExManager);
        selectionManager.setIntersectionSelection(true);
        panel.setSelectionManager(selectionManager);
          
        return panel;
    }

    /**
     * The starting point for the demo.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        SelectionDemo4 demo = new SelectionDemo4(
                  "JFreeChart: SelectionDemo4.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
