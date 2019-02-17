/* -------------------
 * SelectionDemo6.java
 * -------------------
 * (C) Copyright 2013, by Object Refinery Limited.
 *
 */

package org.jfree.chart.demo.selection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.InputEvent;

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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.panel.AbstractMouseHandler;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.FreeRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RefineryUtilities;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.extension.impl.PieCursor;
import org.jfree.data.extension.impl.PieDatasetSelectionExtension;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;

/*
 * based on PieChartDemo2
 */
public class SelectionDemo6Pie extends ApplicationFrame 
        implements SelectionChangeListener<PieCursor<String>> {

    private JTable table;

    private DefaultTableModel model;

    private PieDataset dataset;
    
    public SelectionDemo6Pie(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 500));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(chartPanel);

        this.model = new DefaultTableModel(new String[] { "section", "value:"}, 
                0);
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

    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("One", 43.2);
        dataset.setValue("Two", 10.0);
        dataset.setValue("Three", 27.5);
        dataset.setValue("Four", 17.5);
        dataset.setValue("Five", 11.0);
        dataset.setValue("Six", 19.4);
        return dataset;
    }

    private static JFreeChart createChart(final PieDataset dataset, DatasetSelectionExtension<PieCursor<String>> ext) {
        JFreeChart chart = ChartFactory.createPieChart("Pie Chart Demo 2",
                dataset);
          
        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("One", new Color(160, 160, 255));
        plot.setSectionPaint("Two", new Color(128, 128, 255 - 32));
        plot.setSectionPaint("Three", new Color(96, 96, 255 - 64));
        plot.setSectionPaint("Four", new Color(64, 64, 255 - 96));
        plot.setSectionPaint("Five", new Color(32, 32, 255 - 128));
        plot.setSectionPaint("Six", new Color(0, 0, 255 - 144));

        plot.setNoDataMessage("No data available");

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                  "{0} ({2} percent)"));
        plot.setLabelBackgroundPaint(new Color(220, 220, 220));

        plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator(
                  "Tooltip for legend item {0}"));
        plot.setSimpleLabels(true);
        plot.setInteriorGap(0.1);
                    
        //pie plots done use abstract renderers need to react to selection on our own
        final PieCursor<String> cursor = new PieCursor<String>();

        ext.addChangeListener(new SelectionChangeListener<PieCursor<String>>() {
            public void selectionChanged(SelectionChangeEvent<PieCursor<String>> event) {                    
                for (int i = 0; i < dataset.getItemCount(); i++) {
                    cursor.setPosition((String)dataset.getKey(i));
                    if (event.getSelectionExtension().isSelected(cursor)) {
                        plot.setExplodePercent(cursor.key, 0.15);
                    } else {
                        plot.setExplodePercent(cursor.key, 0.0);
                    }
                }
            }
        });

        return chart;
    }

    public final JPanel createDemoPanel() {
        this.dataset = createDataset();
        //extend dataset and add selection change listener for the demo
        DatasetSelectionExtension<PieCursor<String>> datasetExtension 
                = new PieDatasetSelectionExtension<String>(this.dataset);
        datasetExtension.addChangeListener(this);
          
        //standard setup
        JFreeChart chart = createChart(this.dataset, datasetExtension);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        //add a selection handler with shift modifier for clicking
        RegionSelectionHandler selectionHandler 
                = new FreeRegionSelectionHandler();
        AbstractMouseHandler clickHandler = new MouseClickSelectionHandler(
                InputEvent.SHIFT_MASK);
        panel.addMouseHandler(selectionHandler);
        panel.addMouseHandler(clickHandler);
        panel.removeMouseHandler(panel.getZoomHandler());

        // add a selection manager
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);          
        panel.setSelectionManager(new EntitySelectionManager(panel, 
                new Dataset[] { dataset }, dExManager));
          
        return panel;
    }
     
    public void selectionChanged(SelectionChangeEvent<PieCursor<String>> 
            event) {
        while (this.model.getRowCount() > 0) {
            this.model.removeRow(0);
        }

        PieDatasetSelectionExtension<String> ext 
                = (PieDatasetSelectionExtension<String>) 
                event.getSelectionExtension(); 
        DatasetIterator<PieCursor<String>> iter = ext.getSelectionIterator(
                true);
         
        while (iter.hasNext()) {
            PieCursor<String> dc = iter.next();
            this.model.addRow(new Object[] {dc.key, this.dataset.getValue(
                    dc.key)});
        }
    }

    public static void main(String[] args) {
        SelectionDemo6Pie demo = new SelectionDemo6Pie(
                "JFreeChart: SelectionDemo6Pie.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
