/* ---------------------------
 * SelectionDemo5Category.java
 * ---------------------------
 * (C) Copyright 2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

/**
 * based on BarChartDemo1 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RectangularRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RefineryUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.CategoryCursor;
import org.jfree.data.extension.impl.CategoryDatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;

public class SelectionDemo5Category extends ApplicationFrame 
        implements SelectionChangeListener<CategoryCursor<String, String>> {

    private JTable table;

    private DefaultTableModel model;

    private CategoryDataset dataset;
      
    public SelectionDemo5Category(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(500, 270));
             
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(chartPanel);
               
        this.model = new DefaultTableModel(new String[] { "row:", "column:",
                "value:"}, 0);
        this.table = new JTable(this.model);
        TableColumnModel tcm = this.table.getColumnModel();
        JPanel p = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(this.table);
        p.add(scroller);
        p.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(
                "Selected Items: "), new EmptyBorder(4, 4, 4, 4)));
        split.add(p);
        setContentPane(split);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset() {

        // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);

        return dataset;
    }
         
    private static JFreeChart createChart(CategoryDataset dataset, DatasetSelectionExtension<CategoryCursor<String, String>> ext) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo 1", 
                 "Category", "Value", dataset);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairPaint(Color.blue);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.BLUE,
                0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.GREEN,
                0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.RED,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        renderer.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                             Math.PI / 6.0));
 
        //add selection specific rendering
        IRSUtilities.setSelectedItemPaint(renderer, ext, Color.WHITE);
               
        //register plot as selection change listener
        ext.addChangeListener(plot);
             
        return chart;
    }

    public final JPanel createDemoPanel() {
        this.dataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<CategoryCursor<String, String>> 
                datasetExtension = new CategoryDatasetSelectionExtension<String, String>(this.dataset);     
        datasetExtension.addChangeListener(this);
               
        //standard setup
        JFreeChart chart = createChart(this.dataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        // add a selection handler
        RegionSelectionHandler selectionHandler 
                = new RectangularRegionSelectionHandler();
        panel.addMouseHandler(selectionHandler);
        panel.addMouseHandler(new MouseClickSelectionHandler());
        panel.removeMouseHandler(panel.getZoomHandler());
               
        // add a selection manager
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);
        panel.setSelectionManager(new EntitySelectionManager(panel,     new Dataset[] { this.dataset }, dExManager));
               
        return panel;
    }

    public void selectionChanged(SelectionChangeEvent<CategoryCursor<String, 
            String>> event) {
        while (this.model.getRowCount() > 0) {
            this.model.removeRow(0);
        }

        CategoryDatasetSelectionExtension<String, String> ext 
                = (CategoryDatasetSelectionExtension<String, String>) 
                event.getSelectionExtension(); 
        DatasetIterator<CategoryCursor<String, String>> iter 
                = ext.getSelectionIterator(true);
              
        while (iter.hasNext()) {
            CategoryCursor<String, String> dc = iter.next();
            this.model.addRow(new Object[] { dc.rowKey, dc.columnKey, 
                dataset.getValue(dc.rowKey, dc.columnKey)});
        }
    }

    public static void main(String[] args) {
        SelectionDemo5Category demo = new SelectionDemo5Category(
                "JFreeChart: SelectionDemo5.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}

