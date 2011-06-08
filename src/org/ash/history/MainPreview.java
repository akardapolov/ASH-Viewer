/*
 *-------------------
 * The MainPreview.java is part of ASH Viewer
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
package org.ash.history;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import org.ash.database.ASHDatabase;
import org.ash.gui.StatusBar;
import org.ash.util.Options;
import org.ash.util.ProgressBarUtil;
import org.ash.util.Utils;
import org.jfree.chart.ChartPanel;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;

import com.sleepycat.je.DatabaseException;

/**
 * The Class StackedPanel (history).
 * 
 */
public class MainPreview extends JPanel {

	/** The MainFrame. */
	private JFrame mainFrame;
	
	/** The database. */
	private ASHDatabase database;
	
	/** The status bar. */
    private StatusBar statusBar;
    	
	/** The current window. */
	private Double currentWindow = 3900000.0;
	
	/** The listeners for update Calendar. */
	private List listeners = new ArrayList();
	
	/** The listeners for delete Node. */
	private List listenersNode = new ArrayList();
	
	/** The main. */
	private JPanel main;
	
	/** The date format of x Axis. */
	private String dateFormatXAxis = "";
	
	/** The date. */
	private String rangeData = "";
		
	/** The previous DB instance*/
	private ASHDatabaseH ashDBPrevPeriod = null;
		
	/** The splitCalendarPreviewStackedChart */
	private JSplitPane splitCalendarPreview;
	
	/** The splitMainPane */
	private JSplitPane splitMainPane;
	
	/** The splitCalendarPreviewDivlocation */
	private int splitCalendarPreviewDivLocation = 142;
	
	/** The splitCalendarPreviewDivlocation */
	private int splitMainPaneDivLocation = 190;
	
	/**
	 * Constructor HistoryStacked
	 * 
	 * @param mainFrame0
	 * @param database0
	 * @param statusBar0
	 */
	public MainPreview(JFrame mainFrame0, StatusBar statusBar0){
		this.mainFrame = mainFrame0;
		this.statusBar = statusBar0;
		this.initialize();
	}
	
	/**
	 * Load calendar
	 * 
	 * @param envDir
	 */
	void loadCalendarRun(final String envDir){
		
		this.main.removeAll();
        JPanel panel = createProgressBar("Loading, please wait...");
        this.main.add(panel);
        this.validate();
		
        Thread t = new Thread() {
            @Override
			public void run()
            {
            		// delay
                     try
                     {
                         Thread.sleep(50L);
                     }
                     catch(InterruptedException e) {
					e.printStackTrace();
				}
               loadCalendarPreview(envDir);
			}
		};
		t.start();
	}
	
	/**
	 * Load preview chart panel.
	 * 
	 * @param envDir
	 */
	void loadPreviewChartRun(final long start, final long end){
		
		//this.splitCalendarPreview.remove(2);
        JPanel panel = createProgressBar("Loading, please wait...");
        this.splitCalendarPreview.add(panel,JSplitPane.RIGHT);
        this.splitCalendarPreview.validate();
        this.splitMainPane.validate();
		
        Thread t = new Thread() {
            @Override
			public void run()
            {
            		// delay
                     try
                     {
                         Thread.sleep(50L);
                     }
                     catch(InterruptedException e) {
					e.printStackTrace();
				}
                     loadPreviewStacked(start, end);
			}
		};
		t.start();
	}
	
	/**
	 * Delete old rows and clear BDB logs
	 * 
	 * @param start
	 * @param end
	 * @param isDelAllData
	 */
	void deleteAndClearBDBLogsRun(final long start, final long end, 
			final String envDir, final boolean isDelAllData){
		
        JPanel panel = createProgressBar("Clear database logs, please wait...");
        this.splitCalendarPreview.add(panel,JSplitPane.RIGHT);
        this.splitMainPane.add(new JPanel(), JSplitPane.BOTTOM);
        splitMainPane.setDividerLocation(splitMainPaneDivLocation);
        this.validate();
        splitMainPane.setDividerLocation(splitMainPaneDivLocation);
        
        Thread t = new Thread() {
            @Override
			public void run()
            {
            		// delay
                     try
                     {
                         Thread.sleep(50L);
                     }
                     catch(InterruptedException e) {
					e.printStackTrace();
				}
                     deleteAndClearLogs(start, end, envDir, isDelAllData);
			}
		};
		t.start();
	}

	/**
	 * Load calendar and add to JSplitPane
	 * 
	 * @param envDir
	 */
	private void loadCalendarPreview(String envDir) {

		try {
			
			// Create new instance of CalendarH
			CalendarH calendarH = new CalendarH(envDir,this);
			
			// Close preview instance of ASHDatabaseH
			if (this.ashDBPrevPeriod != null)
				this.ashDBPrevPeriod.close();
			this.ashDBPrevPeriod = calendarH.getDatabaseHistory();
			
			splitCalendarPreview = new JSplitPane();
			splitCalendarPreview.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitCalendarPreview.add(calendarH, JSplitPane.LEFT);
			splitCalendarPreview.add(new JPanel(), JSplitPane.RIGHT);
			splitCalendarPreview.setDividerLocation(splitCalendarPreviewDivLocation);
			splitCalendarPreview.setOneTouchExpandable(true);
						
			this.main.removeAll();
			
			splitMainPane = new JSplitPane();
			splitMainPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitMainPane.add(splitCalendarPreview,JSplitPane.TOP);
			splitMainPane.add(new JPanel(),JSplitPane.BOTTOM);
			splitMainPane.setDividerLocation(splitMainPaneDivLocation);
			splitMainPane.setOneTouchExpandable(true);
			
			this.main.add(splitMainPane);
			this.validate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load preview stacked chart
	 * 
	 * @param envDir
	 */
	private void loadPreviewStacked(long start, long end) {
		try {
			
			TopActivityPreview topActivityPreview = new TopActivityPreview(
					this.ashDBPrevPeriod);
			
			// Set Max CPU
			topActivityPreview.setThresholdMaxCpu(getMaxCPUValue(this.ashDBPrevPeriod));
			
			// Set Title
			topActivityPreview.setTitle(getTitle(this.ashDBPrevPeriod));

			// Init chartPanel
			ChartPanel chartTAPreview = null;
			try {
				chartTAPreview = topActivityPreview.createDemoPanelTopActivity(start,end);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
			// Set format for x axis
			this.setDateFormatXAxis(start,end);
			topActivityPreview.setFormat(getDateFormatXAxis());
			topActivityPreview.updateTitle(getRangeData());

			TopActivityDetail topActivityDetail = new TopActivityDetail(this.mainFrame, this.ashDBPrevPeriod);
			chartTAPreview.addListenerReleaseMouse(topActivityDetail);
			
			// add TAPreview
			splitCalendarPreview.add(chartTAPreview, JSplitPane.RIGHT);
			splitMainPane.add(topActivityDetail, JSplitPane.BOTTOM);
			splitMainPane.setDividerLocation(splitMainPaneDivLocation);
			this.validate();
			splitMainPane.setDividerLocation(splitMainPaneDivLocation);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete data from BDB and clear logs.
	 * 
	 * @param envDir
	 */
	private void deleteAndClearLogs(long start, long end, String envDir,
			boolean isDelAllData) {

		if (isDelAllData) {
			this.ashDBPrevPeriod.close();
			this.ashDBPrevPeriod = null;
			deleteEnvDir(envDir);
			fireDeleteAllAction();
			fireDeleteNodeAction();
		} else {
			if (!Options.getInstance().getEnvDir().trim().equalsIgnoreCase(
					envDir.trim())) {
				this.ashDBPrevPeriod.deleteData(start, end);
				this.ashDBPrevPeriod.cleanLogs();
				fireDeleteAction();
			} else {
				Options.getInstance().getASHDatabase().deleteData(start, end);
				Options.getInstance().getASHDatabase().cleanLogs();
				fireDeleteAction();
			}
		}
		removeListener();

		// add temp JPanel()
		splitCalendarPreview.add(new JPanel(), JSplitPane.RIGHT);
		splitMainPane.add(new JPanel(), JSplitPane.BOTTOM);
		splitMainPane.setDividerLocation(splitMainPaneDivLocation);
		validate();
		splitMainPane.setDividerLocation(splitMainPaneDivLocation);
		splitCalendarPreview
				.setDividerLocation(splitCalendarPreviewDivLocation);

	}
	
	/**
	 * Delete env. directory when user delete all data
	 * 
	 * @param envDir
	 */
	private void deleteEnvDir(String envDir){
		Utils.deleteDirectory(new File(envDir));
	}
	
	/**
	 * Initialize HistoryStacked
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.main = new JPanel();
		this.main.setLayout(new GridLayout(1, 1, 3, 3));
		this.add(this.main, BorderLayout.CENTER);
	}

	/**
	 * Get Max CPU from local BDB
	 * 
	 * @param databaseHistory
	 * @return
	 */
	private double getMaxCPUValue(ASHDatabaseH databaseHistory){
		// Get Max CPU from local BDB
		Double valueSampleTime = 0.0;
		double maxCpu = 1.0;
		try {
			maxCpu = Double.parseDouble(databaseHistory
					.getParameter("cpu_count"));
		} catch (NumberFormatException e){
			maxCpu = 1.0;
		}
		return maxCpu;
	}
	
	/**
	 * Get Title from BDB
	 * 
	 * @param databaseHistory
	 * @return
	 */
	private String getTitle(ASHDatabaseH databaseHistory){
		
		String tmpValue = "Preview "
			+databaseHistory.getParameter("ASH.version")+" ::: "
			+ databaseHistory.getParameter("ASH.name")+ "|"
			+ databaseHistory.getParameter("ASH.host")+ "|"
			+ databaseHistory.getParameter("ASH.port")+ "|"
			+ databaseHistory.getParameter("ASH.sid")+ "|"
			+ databaseHistory.getParameter("ASH.username");
		
		return tmpValue;
	}
	
	/**
	 * Set date format for x Axis and range data.
	 * 
	 * @param databaseHistory
	 */
	private void setDateFormatXAxis(long start, long end){
		
		DateFormat dateFormatDate = new SimpleDateFormat("d");
		DateFormat dateFormatMonth = new SimpleDateFormat("MM");
		DateFormat dateFormatData = new SimpleDateFormat("dd.MM.yyyy");
				
		if ((dateFormatDate.format(start)
				.equalsIgnoreCase( dateFormatDate.format(end)))
				&&
				(dateFormatMonth.format(start)
						.equalsIgnoreCase( dateFormatMonth.format(end)))){
			setDateFormatXAxis ("HH:mm");
			setRangeData(dateFormatData.format(start));
		} else {
			DateTime startTemp = new DateTime(start);
			DateTime endTemp = new DateTime(end);
			Interval interval = new Interval(startTemp, endTemp);
			Days days = Days.daysIn(interval);
			
			if (days.getDays()<2){
				setDateFormatXAxis ("HH:mm");
				setRangeData(dateFormatData.format(start)+"-"
						+dateFormatData.format(end));
			} else {
				setDateFormatXAxis ("EEE d, HH");
				setRangeData(dateFormatData.format(start)+"-"
						+dateFormatData.format(end));
			}
		}
	}

	/**
	 * Creates the progress bar.
	 * 
	 * @param msg the msg
	 * 
	 * @return the j panel
	 */
	private JPanel createProgressBar(String msg) {
		JProgressBar progress = ProgressBarUtil.createJProgressBar(msg);
		progress.setPreferredSize(new Dimension(250, 30));
		JPanel panel = new JPanel();
		panel.add(progress);
		return panel;
	}

	/**
	 * Add listener for calendar
	 * @param l
	 */
	public void addListener(Object l) {
		listeners.add(l);
	}
	
	/**
	 * Add listener for delete node
	 * @param l
	 */
	public void addListenerNode(Object l) {
		listenersNode.add(l);
	}

	/**
	 * Remove listener calendar
	 */
	public void removeListener() {
		listeners.clear();
	}
	
	/**
	 * Remove listener calendar
	 */
	public void removeListenerNode() {
		listenersNode.clear();
	}
	
	/**
	 * Fire deleteAll action.
	 */
	protected void fireDeleteAllAction() {
		Iterator iStart = listeners.iterator();
		while (iStart.hasNext()) {
			Object currListeners = iStart.next();
			if (currListeners instanceof CalendarH) {
				CalendarH tempObj = (CalendarH) currListeners;
				tempObj.removeNoData();
			}
		}
	}
	
	/**
	 * Fire delete action.
	 */
	protected void fireDeleteAction() {
		Iterator iStart = listeners.iterator();
		while (iStart.hasNext()) {
			Object currListeners = iStart.next();
			if (currListeners instanceof CalendarH) {
				CalendarH tempObj = (CalendarH) currListeners;
				tempObj.updateCalendarAfterDeleteActions();
			}
		}
	}
	
	/**
	 * Fire deleteAll action and node delete from jtree.
	 */
	protected void fireDeleteNodeAction() {
		Iterator iStart = listenersNode.iterator();
		while (iStart.hasNext()) {
			Object currListeners = iStart.next();
			if (currListeners instanceof MainProfileTree) {
				MainProfileTree tempObj = (MainProfileTree) currListeners;
				tempObj.deleteNodeAllData();
			}
		}
	}
	
	/**
	 * @return the dateFormatXAxis
	 */
	public String getDateFormatXAxis() {
		return dateFormatXAxis;
	}

	/**
	 * @return the rangeData
	 */
	public String getRangeData() {
		return rangeData;
	}

	/**
	 * @param dateFormatXAxis the dateFormatXAxis to set
	 */
	public void setDateFormatXAxis(String dateFormatXAxis) {
		this.dateFormatXAxis = dateFormatXAxis;
	}

	/**
	 * @param rangeData the rangeData to set
	 */
	public void setRangeData(String rangeData) {
		this.rangeData = rangeData;
	}

	/**
	 * @return the splitCalendarPreviewDivLocation
	 */
	public int getSplitCalendarPreviewDivLocation() {
		return splitCalendarPreviewDivLocation;
	}

	/**
	 * @return the splitMainPaneDivLocation
	 */
	public int getSplitMainPaneDivLocation() {
		return splitMainPaneDivLocation;
	}

	/**
	 * @param splitCalendarPreviewDivLocation the splitCalendarPreviewDivLocation to set
	 */
	public void setSplitCalendarPreviewDivLocation(
			int splitCalendarPreviewDivLocation) {
		this.splitCalendarPreviewDivLocation = splitCalendarPreviewDivLocation;
	}

	/**
	 * @param splitMainPaneDivLocation the splitMainPaneDivLocation to set
	 */
	public void setSplitMainPaneDivLocation(int splitMainPaneDivLocation) {
		this.splitMainPaneDivLocation = splitMainPaneDivLocation;
	}

}
