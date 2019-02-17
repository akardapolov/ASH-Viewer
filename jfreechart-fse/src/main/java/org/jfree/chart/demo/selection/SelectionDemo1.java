/* -------------------
 * SelectionDemo1.java
 * -------------------
 * (C) Copyright 2009-2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.FreeRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.NumberCellRenderer;
import org.jfree.chart.ui.RefineryUtilities;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.extension.impl.XYDatasetSelectionExtension;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class SelectionDemo1 extends ApplicationFrame implements
          SelectionChangeListener<XYCursor> {

    private JTable table;

    private DefaultTableModel model;

    private TimeSeriesCollection dataset;
     
    /**
     * A demonstration application showing how to create a simple time series
     * chart. This example uses monthly data.
     * 
     * @param title  the frame title.
     */
    public SelectionDemo1(String title) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        this.dataset = (TimeSeriesCollection) plot.getDataset();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(chartPanel);

        this.model = new DefaultTableModel(new String[] { "Series:", "Item:",
                  "Period:", "Value:" }, 0);
        this.table = new JTable(this.model);
        TableColumnModel tcm = this.table.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new NumberCellRenderer());
        JPanel p = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(this.table);
        p.add(scroller);
        p.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(
                  "Selected Items: "), new EmptyBorder(4, 4, 4, 4)));
        split.add(p);
        setContentPane(split);

    }

    /**
     * The selection changed, so we change the table model
     * 
     * @param event
     */
    public void selectionChanged(SelectionChangeEvent<XYCursor> event) {
        while (this.model.getRowCount() > 0) {
            this.model.removeRow(0);
        }

        XYDatasetSelectionExtension ext = (XYDatasetSelectionExtension) 
                event.getSelectionExtension(); 
        DatasetIterator<XYCursor> iter = ext.getSelectionIterator(true);
         
        while (iter.hasNext()) {
            XYCursor dc = iter.next();
            Comparable seriesKey = this.dataset.getSeriesKey(dc.series);
            RegularTimePeriod p = this.dataset.getSeries(dc.series)
                    .getTimePeriod(dc.item);
            Number value = this.dataset.getY(dc.series, dc.item);
              
            this.model.addRow(new Object[] { seriesKey, new Integer(dc.item), 
                p, value});
        }
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(XYDataset dataset, 
            DatasetSelectionExtension<XYCursor> ext) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Stock Prices", 
                "Date", "Price Per Unit", dataset);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        r.setBaseShapesVisible(true);
        r.setBaseShapesFilled(true);
        r.setUseFillPaint(true);
        r.setSeriesFillPaint(0, r.lookupSeriesPaint(0));
        r.setSeriesFillPaint(1, r.lookupSeriesPaint(1));
          
        //add selection specific rendering
        IRSUtilities.setSelectedItemFillPaint(r, ext, Color.white);
          
        //register plot as selection change listener
        ext.addChangeListener(plot);
          
        return chart;
    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     * 
     * @return The dataset.
     */
    private static TimeSeriesCollection createDataset() {

        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Month(1, 2009), 181.8);
        s1.add(new Month(2, 2009), 167.3);
        s1.add(new Month(3, 2009), 153.8);
        s1.add(new Month(4, 2009), 167.6);
        s1.add(new Month(5, 2009), 158.8);
        s1.add(new Month(6, 2009), 148.3);
        s1.add(new Month(7, 2009), 153.9);
        s1.add(new Month(8, 2009), 142.7);
        s1.add(new Month(9, 2009), 123.2);
        s1.add(new Month(10, 2009), 131.8);
        s1.add(new Month(11, 2009), 139.6);
        s1.add(new Month(12, 2009), 142.9);
        s1.add(new Month(1, 2010), 138.7);
        s1.add(new Month(2, 2010), 137.3);
        s1.add(new Month(3, 2010), 143.9);
        s1.add(new Month(4, 2010), 139.8);
        s1.add(new Month(5, 2010), 137.0);
        s1.add(new Month(6, 2010), 132.8);

        TimeSeries s2 = new TimeSeries("S2");
        s2.add(new Month(1, 2009), 129.6);
        s2.add(new Month(2, 2009), 123.2);
        s2.add(new Month(3, 2009), 117.2);
        s2.add(new Month(4, 2009), 124.1);
        s2.add(new Month(5, 2009), 122.6);
        s2.add(new Month(6, 2009), 119.2);
        s2.add(new Month(7, 2009), 116.5);
        s2.add(new Month(8, 2009), 112.7);
        s2.add(new Month(9, 2009), 101.5);
        s2.add(new Month(10, 2009), 106.1);
        s2.add(new Month(11, 2009), 110.3);
        s2.add(new Month(12, 2009), 111.7);
        s2.add(new Month(1, 2010), 111.0);
        s2.add(new Month(2, 2010), 109.6);
        s2.add(new Month(3, 2010), 113.2);
        s2.add(new Month(4, 2010), 111.6);
        s2.add(new Month(5, 2010), 108.8);
        s2.add(new Month(6, 2010), 101.6);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;
    }

    public final JPanel createDemoPanel() {
        XYDataset xydataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<XYCursor> datasetExtension 
                = new XYDatasetSelectionExtension(xydataset);     
        datasetExtension.addChangeListener(this);
          
        //standard setup
        JFreeChart chart = createChart(xydataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        // add a selection handler
        RegionSelectionHandler selectionHandler = new FreeRegionSelectionHandler();
        panel.addMouseHandler(selectionHandler);
        panel.addMouseHandler(new MouseClickSelectionHandler());
        panel.removeMouseHandler(panel.getZoomHandler());
          
        // add a selection manager
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);
        panel.setSelectionManager(new EntitySelectionManager(panel,
                new Dataset[] { xydataset }, dExManager));
          
        return panel;
    }
     
    /**
     * Starting point for the demonstration application.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        SelectionDemo1 demo = new SelectionDemo1("JFreeChart: SelectionDemo1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
