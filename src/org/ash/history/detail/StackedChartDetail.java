/*
 *-------------------
 * The StackedChartDetail.java is part of ASH Viewer
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
package org.ash.history.detail;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.ash.history.ASHDatabaseH;
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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import com.sleepycat.je.DatabaseException;

/**
 * The Class StackedXYAreaChartDetail.
 */
public class StackedChartDetail {

	/** The database. */
	private ASHDatabaseH database;
	
	/** The dataset. */
	private CategoryTableXYDataset dataset;
	
	/** The renderer. */
	private StackedXYAreaRenderer3 renderer;
	
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
   		
	/** The Active Session Working*/
	private String activeSessionWorking = "Active Session Working: ";
	
	/** The Active Session Waiting*/
	private String activeSessionWaiting = "Active Session Waiting: ";
	
	/** The cpu. */
	private String CPU  = "CPU used";
	
	/** The waitClass or CPU used */
	private String waitClass = "none";
	
	/** The store of series(int) and labels of wait event (details)*/
	private	LinkedHashMap<Integer, String> seriesIdName;
	
	/** The store labels of wait event for sum (details)*/
	private	HashMap<String, Double> seriesNameSum;
	
	/**
	 * Instantiates a new stacked xy area chart.
	 * 
	 * @param database0 the database0
	 */
    public StackedChartDetail(ASHDatabaseH database0, String waitClass0) {
       this.database = database0;
       this.waitClass = waitClass0;
       
       // Order is important
       this.seriesIdName = new LinkedHashMap<Integer, String>();
       
       this.seriesNameSum = new HashMap<String, Double>();
    }

    /**
     * Creates the chart panel.
     * 
     * @return the chart panel
     * 
     * @throws DatabaseException the database exception
     */
    public ChartPanel createChartPanel(){
    	dataset = new CategoryTableXYDataset();
        JFreeChart chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setRangeZoomable(false);
        return chartPanel;
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
    	this.setMarkerMaxCpu();
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
    		plot.getRangeAxis().setLowerBound(0.0);
    		plot.getRangeAxis().setAutoRange(true);
    	}
    	else {
    		plot.getRangeAxis().setAutoRange(false);
    		plot.getRangeAxis().setUpperBound(bound*this.maxCpu);
    	}
    }
    
    /***
     * Load data to dataset
     * 
     * @param waitClass - wait class or CPU used
     */
    public void setTitle() {
       
       if (this.waitClass.equalsIgnoreCase(CPU)){
        	chart.setTitle(this.activeSessionWorking
        		+this.waitClass);
        	}
        	else {
        	chart.setTitle(this.activeSessionWaiting
             	+this.waitClass);
        	}
    }
    
    /**
     * Save series id and name 
     * 
     * @param index
     * @param waitClass
     */
    public void setSeriesIdName(int index, String waitClass){
    	this.seriesIdName.put(index, waitClass);
    }
    
    /**
     * Save sum of series
     * 
     * @param eventName
     * @param sum
     */
    public void setSeriesNameSum(String eventName, Double sum){
    	this.seriesNameSum.put(eventName, sum);
    }
    
    /**
     * Get current sum value of event
     *  
     * @param waitClass
     * @return
     */
    public Double getSeriesNameSum(String event){
    	return this.seriesNameSum.get(event);
    }
    
    /**
     * Return true if event name already exist in seriesNameSum
     * 
     * @param event
     * @return
     */
    public boolean isSeriesContainName(String event){
    	return this.seriesNameSum.containsKey(event);
    }
    
    /**
     * Is seriesIdName is empty
     * 
     * @return
     */
    public boolean isSeriesIdNameEmpty(){
    	return this.seriesIdName.isEmpty();
    }
    
    /**
     * Is dataset empty
     * 
     * @return
     */
    public boolean isDatasetEmpty(){
    	if (this.dataset.getSeriesCount()==0){
        	return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Return size of series id name
     * 
     * @return
     */
    public int getSizeSeriesIdName(){
    	return this.seriesIdName.size();
    }
    
    /**
     * Set series paint
     * 
     * @param series
     * @param waitClass
     */
    public void setSeriesPaint(int series, String waitClass, String from){
    	if (waitClass != null){
    		this.renderer.setSeriesPaint(series, 
    				Options.getInstance().getColor(waitClass), true);
    	}
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
     * Calculate sum, save data to dataset and clear temp. array
     * 
     * @param rangeHalf delay/2
     * @param dd current time
     */
    public void calcSaveAndClear(int rangeHalf, double dd){
    	int rangeHalfSec = (rangeHalf*2)/1000;

    	double sumEvent = 0.0;
    	Set entries = this.seriesNameSum.entrySet();
    	Iterator iter = entries.iterator();
    	while (iter.hasNext()) {
    		Map.Entry entry = (Map.Entry) iter.next();
    		sumEvent = sumEvent + (Double)entry.getValue();
    	}
    	if (sumEvent == 0.0){
     		sumEvent = 0.000001;
     	}
    	
    	// Save System Event and CPU statistics
    	Set entrSeriesIdName = this.seriesIdName.entrySet();
    	Iterator iterSIM = entrSeriesIdName.iterator();
    	while (iterSIM.hasNext()) {
    		Map.Entry entry = (Map.Entry) iterSIM.next();
    		String eventName = (String)entry.getValue();
    		Double tmpValue = 
    			(((Double)this.seriesNameSum.get(eventName)/sumEvent)*sumEvent)/rangeHalfSec;
    		
    		if (eventName != null){
    			this.dataset.add(dd+rangeHalf, tmpValue, eventName);
    		}
    		
    	}
    	
    	// Clear values
    	Set entrClear = this.seriesIdName.entrySet();
    	Iterator iterClear = entrClear.iterator();
    	while (iterClear.hasNext()) {
    		Map.Entry entry = (Map.Entry) iterClear.next();
    		String eventName = (String)entry.getValue();
    		this.seriesNameSum.put(eventName, 0.0);
    	}
    }
    
    /**
     * Add points to left side.
     * 
     * @param rangeHalf delay/2
     * @param dd current time
     */
    public void addPointsToLeft(double beginTime, double endTime, int rangeHalf){
    	int rangeHalfSec = (rangeHalf*2)/1000;
    	
    	// Save System Event and CPU statistics
    	Set entrSeriesIdName = this.seriesIdName.entrySet();
    	Iterator iterSIM = entrSeriesIdName.iterator();
    	while (iterSIM.hasNext()) {
    		Map.Entry entry = (Map.Entry) iterSIM.next();
    		String eventName = (String)entry.getValue();
    		Double tmpValue = 0.0;
    		
    		for (double dd = beginTime; dd < endTime; dd += rangeHalf * 2) {
    			if (eventName != null){
    				this.dataset.add(dd+rangeHalf, tmpValue, eventName);
    			}
    		}
    	}
    }	
    
    
    /**
     * Creates the chart.
     * 
     * @return the jfreechart
     */
    private JFreeChart createChart() {

        xAxis = new DateAxis("time");
        xAxis.setLabel(null);
        
        chart = ChartFactory.createStackedXYAreaChart(
        	"", 		 					 // chart title
            "X Value",                       // domain axis label
            "Active Sessions",               // range axis label
            dataset,                         // data
            PlotOrientation.VERTICAL,        // the plot orientation
            xAxis,							 // xAxis
            false,                           // legend
            true,                            // tooltips
            false                            // urls
        );
        
        chart.getTitle().setFont(new Font(TextTitle.DEFAULT_FONT.getFontName(), 
				TextTitle.DEFAULT_FONT.getStyle(), 14));
        
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
        
        legend.setItemFont(new Font(LegendTitle.DEFAULT_ITEM_FONT.getFontName(), 
				LegendTitle.DEFAULT_ITEM_FONT.getStyle(), 10));
        
        chart.addSubtitle(legend);
        
        return chart;
    }
            
    /**
     * Add a marker Maximum CPU
     * 
     */
    private void setMarkerMaxCpu(){
   	   // add a labelled marker for the cpu_count
       thresholdMaxCpu = new ValueMarker(this.maxCpu);
       thresholdMaxCpu.setLabelOffsetType(LengthAdjustmentType.EXPAND);
       thresholdMaxCpu.setPaint(Color.red);
       thresholdMaxCpu.setStroke(new BasicStroke(1.0f));
       thresholdMaxCpu.setLabel("Maximum CPU");
       thresholdMaxCpu.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
       thresholdMaxCpu.setLabelPaint(Color.red);
       thresholdMaxCpu.setLabelAnchor(RectangleAnchor.TOP_LEFT);
       thresholdMaxCpu.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
       plot.addRangeMarker(thresholdMaxCpu);
    }
    
}

