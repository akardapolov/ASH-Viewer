/*
 *-------------------
 * The TopActivityDetail.java is part of ASH Viewer
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.ash.history.detail.DetailsPanelH;
import org.ash.history.detail.DetailsPanelHSession;
import org.ash.util.Options;
import org.ash.util.ProgressBarUtil;
import org.jfree.chart.ChartPanel;

import com.sleepycat.je.DatabaseException;

/**
 * The Class MainPanelH (history).
 */
public class TopActivityDetail extends JPanel{

	/** The MainFrame. */
	private JFrame mainFrame;
	
	/** The tabbed panel. */
	private JTabbedPane tabsMain;
	
	/** The split pane main. */
	private JSplitPane splitPaneMain = new JSplitPane();
	
	/** The main panel. */
	private JPanel mainPanel = new JPanel();
	
	/** The top activity panel (history). */
	private JPanel mainPanelHistory;
	
	/** The BDB database. */
	private ASHDatabaseH databaseHistory;
	
	/** The chart chart panel. */
	private ChartPanel chartChartPanel;
	
	/** The stacked chart main object. */
	private TopActivityPreview stackedChartMainObject;

	/** The gantt graph for sqls and sessions. */
	private GanttH sqlsAndSessions;
		
	/** The date format for chart title. */
	DateFormat dateFormatTitle = new SimpleDateFormat("HH:mm");

	
	/**
	 * Instantiates a new main frame.
	 * 
	 */
	public TopActivityDetail(JFrame mainFrame, ASHDatabaseH databaseHistory) {
		
		this.mainFrame = mainFrame;
		this.setLayout(new GridLayout(1, 1, 1, 1));
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setVisible(true);
		
		this.databaseHistory = databaseHistory;
		
		this.add(this.mainPanel);
	}
	
	
	public void loadPreviewStackedChart(final double begin, final double end){

		this.mainPanel.removeAll();		
        JPanel panel = createProgressBar("Loading, please wait...");
        this.mainPanel.add(panel);
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
					try {
						loadPreviewStackedChartP(begin, end);
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		};
		t.start();
		
	}
	
	/**
	 * Load Top Activity and Detail tabs with data.
	 * 
	 * @param begin 
	 * @param end
	 * @throws DatabaseException
	 */
	private void loadPreviewStackedChartP(double begin, double end) throws DatabaseException {
		
		try {
						
		TopActivityPreview stackedChart = new TopActivityPreview(
				this.databaseHistory);
		
		// Set Max CPU
		stackedChart.setThresholdMaxCpu(getMaxCPUValue(databaseHistory));
		
		// Set Title
		stackedChart.setTitle(getTitle(databaseHistory,begin,end));
		
		// Set chart panel
		this.chartChartPanel = stackedChart.createDemoPanelTopActivity(begin, end);
		
		// Set legend to stacked chart
		stackedChart.addLegend(10);
		
		// Set format for x axis
		stackedChart.setFormat("HH:mm");
				
		/** Initialize Sqls & Sessions JPanel*/
		this.sqlsAndSessions = new GanttH(this.mainFrame, this.databaseHistory);
		
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.add(this.chartChartPanel, "top");
		this.splitPaneMain.add(this.sqlsAndSessions, "bottom");
		this.splitPaneMain.setDividerLocation(240);
		this.splitPaneMain.setOneTouchExpandable(true);
		
		this.mainPanelHistory = new JPanel();
		this.mainPanelHistory.setLayout(new GridLayout(1, 1, 1, 1));
		this.mainPanelHistory.add(splitPaneMain);
		
		this.chartChartPanel.addListenerReleaseMouse(this.sqlsAndSessions);
		this.sqlsAndSessions.repaint();
		
		this.tabsMain = new JTabbedPane();
		
		DetailsPanelH detailJPanelH = 
			new DetailsPanelH(this.databaseHistory,begin,end,getMaxCPUValue(databaseHistory));
		
		DetailsPanelHSession otherJPanelH = 
			new DetailsPanelHSession(this.databaseHistory,begin,end,getMaxCPUValue(databaseHistory));
		
		this.tabsMain.add(this.mainPanelHistory,
				Options.getInstance().getResource("tabMain.text"));
		this.tabsMain.add(detailJPanelH,
				Options.getInstance().getResource("tabDetail.text"));
		this.tabsMain.add(otherJPanelH,
				Options.getInstance().getResource("otherLabel.text"));
		
		this.mainPanel.removeAll();
		this.mainPanel.add(this.tabsMain);
		this.validate();

		} catch (DatabaseException e) {
			e.printStackTrace();
		}
				
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
	private String getTitle(ASHDatabaseH databaseHistory, double begin, double end){		
		String tmpValue = "Top activity";
		return tmpValue;
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

	
}
