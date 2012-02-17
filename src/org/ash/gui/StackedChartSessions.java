/*
 *-------------------
 * The StackedChart.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */

package org.ash.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ash.database.ASHDatabase;
import org.ash.util.Options;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer3;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import com.sleepycat.je.DatabaseException;

/**
 * The Class StackedXYAreaChart.
 * 
 */
public class StackedChartSessions {

	/** The database. */
	private ASHDatabase database;
	
	/** The dataset. */
	private CategoryTableXYDataset dataset;
	
	/** The chart. */
	private JFreeChart chart;
	
	/** The chart panel. */
	private ChartPanel chartPanel;
	
	/** The date format. */
	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	/** The current date. */
	private Date currentDate;	
    
    /** The x axis. */
    private DateAxis xAxis;
	
	/** The threshold max cpu. */
	private ValueMarker thresholdMaxCpu;
	
	/** The max cpu. */
	private double maxCpu;
	
	/** The plot. */
	private XYPlot plot;
	
	/** The current end. */
	private Marker currentEnd;
	
	/** The flag threshold begin time auto selection. */
   	private boolean flagThresholdBeginTimeAutoSelection = false;
   	
   	/** The renderer end. */
   	private StackedXYAreaRenderer3 renderer;
   	
	/**
	 * Instantiates a new stacked xy area chart.
	 * 
	 * @param database0 the database0
	 */
    public StackedChartSessions(ASHDatabase database0) {
       this.database = database0;
    }
    
    /**
     * Creates the chart panel.
     * 
     * @return the chart panel
     * 
     * @throws DatabaseException the database exception
     */
    public ChartPanel createChartPanel() throws DatabaseException {
    	createDataset();
        JFreeChart chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setRangeZoomable(false);
        chartPanel.setDomainZoomable(false);
        return chartPanel;
    }
    
 	/**
	  * Update xaxis label.
	  * 
	  * @param time the time
	  */
    public void updatexAxisLabel(double time){  	
      if (!xAxis.getLabel().equalsIgnoreCase(dateFormat.format(time))){
    	  xAxis.setLabel(dateFormat.format(time));
      }      
    }
    
    /**
     * Sets the threshold max cpu.
     * 
     * @param maxCpu the new threshold max cpu
     */
    public void setThresholdMaxCpu(double maxCpu){  	
    	this.maxCpu = maxCpu;
      }
    
    /**
     * Sets the flag threshold begin time auto selection.
     * 
     * @param flag0 the new flag threshold begin time auto selection
     */
    public void setFlagThresholdBeginTimeAutoSelection(boolean flag0){
    	this.flagThresholdBeginTimeAutoSelection = flag0;
    }
    
    /**
     * Checks if is flag threshold begin time auto selection.
     * 
     * @return true, if is flag threshold begin time auto selection
     */
    public boolean isFlagThresholdBeginTimeAutoSelection(){
    	return this.flagThresholdBeginTimeAutoSelection;
    }
    
    /**
     * Sets the threshold begin time auto selection.
     * 
     * @param beginTime the begin time
     * @param range the range
     */
    public synchronized void setThresholdBeginTimeAutoSelection(double beginTime, int range){
    	plot.removeDomainMarker(currentEnd);
        currentEnd = new ValueMarker(beginTime);
        currentEnd.setPaint(Color.red);
        currentEnd.setLabel(range+" min");
        currentEnd.setStroke(new BasicStroke(1.0f));
        currentEnd.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        currentEnd.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addDomainMarker(currentEnd);
    }
    
    /**
     * Removes the threshold begin time auto selection.
     */
    public void removeThresholdBeginTimeAutoSelection(){
        plot.removeDomainMarker(currentEnd);
    }
    
    /**
     * Adds the listener chart panel.
     * 
     * @param l the l
     */
    public void addListenerChartPanel(Object l){  	
    	chartPanel.addListenerReleaseMouse(l);
      }

    /**
     * Removes the listener chart panel.
     * 
     * @param l the l
     */
    public void removeListenerChartPanel(Object l){  	
    	chartPanel.removeListenerReleaseMouse(l);
      }
    
    /**
     * Sets the selection chart.
     * 
     * @param flag the new selection chart
     */
    public void setSelectionChart(boolean flag){
    	chartPanel.setDomainZoomable(flag);
    }
    
    /**
     * Checks if is mouse dragged.
     * 
     * @return true, if is mouse dragged
     */
    public boolean isMouseDragged(){
    	return chartPanel.isMouseDragged();
    }
    
    /**
     * Set upper bound of range axis
     * 
     * @param bound
     */
    public void setUpperBoundOfRangeAxis(double bound){
    	if (bound == 0.0){
    		plot.getRangeAxis().setAutoRange(true);
    	}
    	else {
    		plot.getRangeAxis().setAutoRange(false);
    		plot.getRangeAxis().setUpperBound(bound*this.maxCpu);
    	}
    }
    
    /**
     * Creates the chart.
     * 
     * @return the j free chart
     */
    private JFreeChart createChart() {

        xAxis = new DateAxis("time");
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
        
        currentDate = new Date();	
    	updatexAxisLabel(new Long(currentDate.getTime()).doubleValue());
            	
        chart = ChartFactory.createStackedXYAreaChart(
            "Number of sessions",  				 // chart title
            "X Value",                       // domain axis label
            "Sessions",               // range axis label
            dataset,                         // data
            PlotOrientation.VERTICAL,        // the plot orientation
            xAxis,
            false,                           // legend
            true,                            // tooltips
            false                            // urls
        );
        
        plot = (XYPlot) chart.getPlot();
        renderer = new StackedXYAreaRenderer3(); 
        renderer.setRoundXCoordinates(true);
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator
        		("{0} ({1}, {2})",
        		 new SimpleDateFormat("HH:mm"),
        		 new DecimalFormat("0.0")));
        plot.setRenderer(0, renderer);   
        plot.getRangeAxis().setLowerBound(0.0);
        plot.getRangeAxis().setAutoRange(true);
        
        
        // add a labelled marker for the cpu_count
        /*thresholdMaxCpu = new ValueMarker(this.maxCpu);
        thresholdMaxCpu.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        thresholdMaxCpu.setPaint(Color.red);
        thresholdMaxCpu.setStroke(new BasicStroke(1.0f));
        thresholdMaxCpu.setLabel("Maximum CPU");
        thresholdMaxCpu.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
        thresholdMaxCpu.setLabelPaint(Color.red);
        thresholdMaxCpu.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        thresholdMaxCpu.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addRangeMarker(thresholdMaxCpu);*/
        
        renderer.setSeriesPaint(0, Options.getInstance().getColor(
						Options.getInstance().getResource("AllSessionLabel.text")), true);
        
        
        // Set format for x axis
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
        // Add legend to right
        LegendTitle legend = new LegendTitle(chart.getPlot());
        
        BlockContainer wrapper = new BlockContainer(new BorderArrangement());
        wrapper.setFrame(new BlockBorder(1.0, 1.0, 1.0, 1.0));
        
        BlockContainer itemss = legend.getItemContainer();
        itemss.setPadding(2, 10, 5, 2);
        wrapper.add(itemss);
        legend.setWrapper(wrapper);
        
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        chart.addSubtitle(legend);
        
        return chart;
    }

    /**
	 * Delete values from dataset (details)
	 * 
	 */
	public void deleteValuesFromDatasetDetail(Double beginRange){
		
		// Clear values from dataset when it's not empty
		if (this.renderer.getLegendItems().getItemCount()!=0){
			for (int i=0;i<50;i++) {
				
				Double xValue = (Double)dataset.getX(0, i);
				
				if(xValue>beginRange){
					break;
				}
				
				try {
					dataset.removeRow(xValue);
				} catch (Exception e){
					e.printStackTrace();
				}
			  }
			}
		}
    
    /**
     * Creates the dataset.
     * 
     * @throws DatabaseException the database exception
     */
    private void createDataset() throws DatabaseException {
    	dataset = new CategoryTableXYDataset();
        this.database.loadDataToChartPanelDataSet_Sessions_All(dataset);
    }
    
}

