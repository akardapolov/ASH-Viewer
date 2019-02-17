/* ----------------------------------
 * SelectionDemo7ScatterRenderer.java
 * ----------------------------------
 * (C) Copyright 2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.FreeRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.renderer.item.DefaultShapeIRS;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.RefineryUtilities;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.CategoryCursor;
import org.jfree.data.extension.impl.CategoryDatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.MultiValueCategoryDataset;

/*
 * based on ScatterRendererDemo1 
 */
public class SelectionDemo7ScatterRenderer extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     * 
     * @param title  the frame title.
     */
    public SelectionDemo7ScatterRenderer(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private static List<Number> listOfValues(double[] values) {
        List<Number> result = new ArrayList<Number>();
        for (int i = 0; i < values.length; i++) {
            result.add(new Double(values[i]));
        }
        return result;
    }

    /**
     * Creates a sample dataset.
     * 
     * @return A dataset.
     */
    private static MultiValueCategoryDataset createDataset() {
        DefaultMultiValueCategoryDataset dataset 
                = new DefaultMultiValueCategoryDataset();
        dataset.add(listOfValues(new double[] { 1.0, 2.0, 3.0 }), "Series 1",
                  "C1");
        dataset.add(listOfValues(new double[] { 1.2, 2.2, 3.2 }), "Series 1",
                  "C2");
        dataset.add(listOfValues(new double[] { 1.4, 2.4, 3.4 }), "Series 1",
                  "C3");
        dataset.add(listOfValues(new double[] { 1.0, 2.1, 3.2 }), "Series 1",
                  "C1");
        dataset.add(listOfValues(new double[] { 1.2, 2.15, 3.5 }), "Series 1",
                  "C2");
        dataset.add(listOfValues(new double[] { 1.4, 2.5, 3.2 }), "Series 1",
                  "C3");
        dataset.add(listOfValues(new double[] { 1.4, 3.0, 3.2 }), "Series 1",
                  "C3");
        dataset.add(listOfValues(new double[] { 1.4, 3.0 }), "Series 2", "C1");

        dataset.add(listOfValues(new double[] { 1.0, 3.0 }), "Series 2", "C1");
        dataset.add(listOfValues(new double[] { 1.2, 3.2 }), "Series 2", "C2");
        dataset.add(listOfValues(new double[] { 1.4, 3.6 }), "Series 2", "C3");
        dataset.add(listOfValues(new double[] { 1.2, 3.1 }), "Series 2", "C1");
        dataset.add(listOfValues(new double[] { 1.4, 3.4 }), "Series 2", "C2");
        dataset.add(listOfValues(new double[] { 1.5, 3.6 }), "Series 2", "C3");

        return dataset;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(final MultiValueCategoryDataset 
            dataset, final DatasetSelectionExtension<CategoryCursor
                    <String, String>> ext) {

        ScatterRenderer r = new ScatterRenderer();
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(
                "Category"), new NumberAxis("Value"), r);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        JFreeChart chart = new JFreeChart("ScatterRendererDemo1", plot);
        ChartUtilities.applyCurrentTheme(chart);
       
        //register the plot
        ext.addChangeListener(plot);          
          
        //illustrates the usage of a shape item rendering strategy
        final CategoryCursor<String, String> cursor = new CategoryCursor<String,
                String>();
        r.setShapeIRS(new DefaultShapeIRS(r) {
            private static final long serialVersionUID = 1L;

            @Override
            public Shape getItemShape(int row, int column) {
                cursor.setPosition((String)dataset.getRowKey(row), 
                        (String) dataset.getColumnKey(column));
                if (ext.isSelected(cursor)) {
                    return new Rectangle2D.Double(-10.0, -10.0, 20.0, 20.0);
                } else {
                    return super.getItemShape(row, column);
                }
            }
        });                    
          
        return chart;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        MultiValueCategoryDataset dataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<CategoryCursor<String, String>> 
                datasetExtension = new CategoryDatasetSelectionExtension
                <String, String>(dataset);
          
        //standard setup
        JFreeChart chart = createChart(dataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        //add a selection handler with shift modifier for clicking
        RegionSelectionHandler selectionHandler 
                = new FreeRegionSelectionHandler();
        panel.addMouseHandler(selectionHandler);
        panel.addMouseHandler(new MouseClickSelectionHandler());
        panel.removeMouseHandler(panel.getZoomHandler());

        // add a selection manager
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);          
        panel.setSelectionManager(new EntitySelectionManager(panel, 
                new Dataset[] { dataset }, dExManager));
        return panel;
    }

    /**
     * Starting point for the demonstration application.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        SelectionDemo7ScatterRenderer demo = new SelectionDemo7ScatterRenderer(
                "JFreeChart: ScatterRendererDemo1.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
