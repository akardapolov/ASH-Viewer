/* -------------------
 * SelectionDemo1.java
 * -------------------
 * (C) Copyright 2009-2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.demo.selection.stacked.CategoryTableXYDatasetRTV;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RectangularHeightRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer3;
import org.jfree.chart.title.LegendTitle;
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
import org.jfree.data.xy.CategoryTableXYDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class SelectionDemo9 extends ApplicationFrame implements
          SelectionChangeListener<XYCursor> {

    private JTable table;
    private DefaultTableModel model;
    //private TimeSeriesCollection dataset;

    // Components (ChartPanel)
    private ChartPanel chartPanel;
    private JFreeChart chart;
    private XYPlot plot;
    private StackedXYAreaRenderer3 renderer;

    private CategoryTableXYDataset dataset;
    private DateAxis xAxis;
    private String	dateTimeAxisLabel;

    private LegendTitle legend;
    private BlockContainer blockContWrapper;
    private BlockContainer itemss;

    private Marker currentMarker;

    /**
     * A demonstration application showing how to create a simple time series
     * chart. This example uses monthly data.
     *
     * @param title  the frame title.
     */
    public SelectionDemo9(String title) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setRangeZoomable(false);

        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();

        this.dataset = new CategoryTableXYDataset();

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
            //RegularTimePeriod p = this.dataset.getSeries(dc.series).getTimePeriod(dc.item);
            Number value = this.dataset.getY(dc.series, dc.item);
              
            this.model.addRow(new Object[] { seriesKey, new Integer(dc.item),
                    dc.series, value});
            System.out.println(dc.series+"--"+dc.item);
        }

    }

    /**
     * Creates a chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(CategoryTableXYDatasetRTV dataset,
            DatasetSelectionExtension<XYCursor> ext) {

        /*ChartFactory.createTimeSeriesChart("Stock Prices",
                "Date", "Price Per Unit", dataset);*/

        JFreeChart chart = ChartFactory.createStackedXYAreaChart(
                "asflkasf;lkjasdljf",  				 		     // chart title
                "Кол-во",               		 // range axis label
                createDataset(),               // data
                PlotOrientation.VERTICAL,        // the plot orientation
                new DateAxis("time"),				 // the axis
                false,                           // legend
                true,                            // tooltips
                false                            // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        StackedXYAreaRenderer3 r = (StackedXYAreaRenderer3) plot.getRenderer();

        //r.setBaseShapesVisible(true);
        //r.setBaseShapesFilled(true);
        //r.setUseFillPaint(true);
        r.setSeriesFillPaint(0, r.lookupSeriesPaint(0));
        r.setSeriesFillPaint(1, r.lookupSeriesPaint(1));

        //r.setDrawOutlines(true);

        //add selection specific rendering
        IRSUtilities.setSelectedItemFillPaint(r, ext, Color.black);
          
        //register plot as selection change listener
        ext.addChangeListener(plot);

        return chart;
    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     * 
     * @return The dataset.
     */
    private static CategoryTableXYDatasetRTV createDataset() {

        CategoryTableXYDatasetRTV out = new CategoryTableXYDatasetRTV();

        for (int i = 0; i < 100; i++) {
            out.add(i,i*0.1,"test1");
        }
        for (int i = 0; i < 100; i++) {
            out.add(i,i*0.1,"test2");
        }

        return out;
    }

    public final JPanel createDemoPanel() {
        CategoryTableXYDatasetRTV xydataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<XYCursor> datasetExtension
                = new XYDatasetSelectionExtension(xydataset);
        datasetExtension.addChangeListener(this);
          
        //standard setup
        JFreeChart chart = createChart(xydataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        // add a selection handler
        RegionSelectionHandler selectionHandler = new RectangularHeightRegionSelectionHandler();
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
        SelectionDemo9 demo = new SelectionDemo9("JFreeChart: SelectionDemo9");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
