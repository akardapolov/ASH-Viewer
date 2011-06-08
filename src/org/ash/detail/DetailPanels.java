/*
 *-------------------
 * The DetailPanels.java is part of ASH Viewer
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
package org.ash.detail;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ash.database.ASHDatabase;
import org.ash.gui.StatusBar;
import org.ash.util.Options;
import org.jfree.chart.ChartPanel;

public class DetailPanels extends JPanel{

	/** The MainFrame. */
	private JFrame mainFrame;
	
	/** The database. */
	private ASHDatabase database;
	
	/** The tabbed pane */
	private JTabbedPane tabsDetail;
	
	/** The stacked chart main object for cpu. */
	private StackedChartDetail cpuStackedChartMainObjectDetail;
	
	/** The stacked chart main object for scheduler. */
	private StackedChartDetail schedulerStackedChartMainObjectDetail;
	
	/** The stacked chart main object for userIO. */
	private StackedChartDetail userIOStackedChartMainObjectDetail;
	
	/** The stacked chart main object for systemIO. */
	private StackedChartDetail systemIOStackedChartMainObjectDetail;
	
	/** The stacked chart main object for concurrency. */
	private StackedChartDetail concurrencyStackedChartMainObjectDetail;
	
	/** The stacked chart main object for application. */
	private StackedChartDetail applicationStackedChartMainObjectDetail;
	
	/** The stacked chart main object for commit. */
	private StackedChartDetail commitStackedChartMainObjectDetail;
	
	/** The stacked chart main object for configuration. */
	private StackedChartDetail configurationStackedChartMainObjectDetail;
	
	/** The stacked chart main object for administrative. */
	private StackedChartDetail administrativeStackedChartMainObjectDetail;
	
	/** The stacked chart main object for network. */
	private StackedChartDetail networkStackedChartMainObjectDetail;
	
	/** The stacked chart main object for queuning. */
	private StackedChartDetail queuningStackedChartMainObjectDetail;
	
	/** The stacked chart main object for cluster. */
	private StackedChartDetail clusterStackedChartMainObjectDetail;
	
	/** The stacked chart main object for other. */
	private StackedChartDetail otherStackedChartMainObjectDetail;
	
	/** The split pane main for cpu. */
	private JSplitPane cpuSplitPaneMainDetail;
	
	/** The split pane for scheduler. */
	private JSplitPane schedulerSplitPaneMainDetail;
	
	/** The split pane for userIO. */
	private JSplitPane userIOSplitPaneMainDetail;
	
	/** The split pane for systemIO. */
	private JSplitPane systemIOSplitPaneMainDetail;
	
	/** The split pane for concurrency. */
	private JSplitPane concurrencySplitPaneMainDetail;
	
	/** The split pane for application. */
	private JSplitPane applicationSplitPaneMainDetail;
	
	/** The split pane for commit. */
	private JSplitPane commitSplitPaneMainDetail;
	
	/** The split pane for configuration. */
	private JSplitPane configurationSplitPaneMainDetail;
	
	/** The split pane for administrative. */
	private JSplitPane administrativeSplitPaneMainDetail;
	
	/** The split pane for network. */
	private JSplitPane networkSplitPaneMainDetail;
	
	/** The split pane for queuning. */
	private JSplitPane queuningSplitPaneMainDetail;
	
	/** The split pane for cluster. */
	private JSplitPane clusterSplitPaneMainDetail;
	
	/** The split pane for other. */
	private JSplitPane otherSplitPaneMainDetail;
	
	/** The chart chart panel for cpu. */
	private ChartPanel cpuChartPanel;
	
	/** The chart chart panel for scheduler. */
	private ChartPanel schedulerChartPanel;
	
	/** The chart chart panel for userIO. */
	private ChartPanel userIOChartPanel;
	
	/** The chart chart panel for systemIO. */
	private ChartPanel systemIOChartPanel;
	
	/** The chart chart panel for concurrency. */
	private ChartPanel concurrencyChartPanel;
	
	/** The chart chart panel for application. */
	private ChartPanel applicationChartPanel;
	
	/** The chart chart panel for commit. */
	private ChartPanel commitChartPanel;
	
	/** The chart chart panel for configuration. */
	private ChartPanel configurationChartPanel;
	
	/** The chart chart panel for administrative. */
	private ChartPanel administrativeChartPanel;
	
	/** The chart chart panel for network. */
	private ChartPanel networkChartPanel;
	
	/** The chart chart panel for queuning. */
	private ChartPanel queuningChartPanel;
	
	/** The chart chart panel for cluster. */
	private ChartPanel clusterChartPanel;
	
	/** The chart chart panel for other. */
	private ChartPanel otherChartPanel;
	
	/** The top sqls and sessions for cpu. */
	private GanttDetails cpuSqlsAndSessions;
	
	/** The top sqls and sessions for scheduler. */
	private GanttDetails schedulerSqlsAndSessions;
	
	/** The top sqls and sessions for userIO. */
	private GanttDetails userIOSqlsAndSessions;
	
	/** The top sqls and sessions for systemIO. */
	private GanttDetails systemIOSqlsAndSessions;
	
	/** The top sqls and sessions for concurrency. */
	private GanttDetails concurrencySqlsAndSessions;
	
	/** The top sqls and sessions for application. */
	private GanttDetails applicationSqlsAndSessions;
	
	/** The top sqls and sessions for commit. */
	private GanttDetails commitSqlsAndSessions;
	
	/** The top sqls and sessions for configuration. */
	private GanttDetails configurationSqlsAndSessions;
	
	/** The top sqls and sessions for administrative. */
	private GanttDetails administrativeSqlsAndSessions;
	
	/** The top sqls and sessions for network. */
	private GanttDetails networkSqlsAndSessions;
	
	/** The top sqls and sessions for queuning. */
	private GanttDetails queuningSqlsAndSessions;
	
	/** The top sqls and sessions for cluster. */
	private GanttDetails clusterSqlsAndSessions;
	
	/** The top sqls and sessions for other. */
	private GanttDetails otherSqlsAndSessions;
		
	 /** The status bar. */
    private StatusBar statusBar;
	
	/** The max cpu. */
	private double maxCpu;
	
	/** The divider. */
	private int dividerLocation = 290;
	
	/**
	 * Constructor DetailFrame
	 * 
	 * @param mainFrame0
	 * @param database0
	 * @param statusBar0
	 */
	public DetailPanels(JFrame mainFrame0, ASHDatabase database0, StatusBar statusBar0){
		this.mainFrame = mainFrame0;
		this.database = database0;
		this.statusBar = statusBar0;
		this.tabsDetail = new JTabbedPane();
		
		this.cpuStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("cpuLabel.text"));
		this.schedulerStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("schedulerLabel.text"));
		this.userIOStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("userIOLabel.text"));
		this.systemIOStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("systemIOLabel.text"));	
		this.concurrencyStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("concurrencyLabel.text"));	
		this.applicationStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("applicationsLabel.text"));	
		this.commitStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("commitLabel.text"));
		this.configurationStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("configurationLabel.text"));
		this.administrativeStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("administrativeLabel.text"));
		this.networkStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("networkLabel.text"));
		this.queuningStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("queueningLabel.text"));
		this.clusterStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("clusterLabel.text"));
		this.otherStackedChartMainObjectDetail = new StackedChartDetail
			(this.database, Options.getInstance().getResource("otherLabel.text"));
		
		this.initialize();
	}
	
	/**
	 * Initialize DetailFrame
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		this.cpuSplitPaneMainDetail = new JSplitPane();
		this.schedulerSplitPaneMainDetail = new JSplitPane();
		this.userIOSplitPaneMainDetail = new JSplitPane();
		this.systemIOSplitPaneMainDetail = new JSplitPane();
		this.concurrencySplitPaneMainDetail = new JSplitPane();
		this.applicationSplitPaneMainDetail = new JSplitPane();
		this.commitSplitPaneMainDetail = new JSplitPane();
		this.configurationSplitPaneMainDetail = new JSplitPane();
		this.administrativeSplitPaneMainDetail = new JSplitPane();
		this.networkSplitPaneMainDetail = new JSplitPane();
		this.queuningSplitPaneMainDetail = new JSplitPane();
		this.clusterSplitPaneMainDetail = new JSplitPane();
		this.otherSplitPaneMainDetail = new JSplitPane();
		
		this.cpuChartPanel = this.cpuStackedChartMainObjectDetail.createChartPanel();
		this.schedulerChartPanel = this.schedulerStackedChartMainObjectDetail.createChartPanel();
		this.userIOChartPanel = this.userIOStackedChartMainObjectDetail.createChartPanel();
		this.systemIOChartPanel = this.systemIOStackedChartMainObjectDetail.createChartPanel();
		this.concurrencyChartPanel = this.concurrencyStackedChartMainObjectDetail.createChartPanel();
		this.applicationChartPanel = this.applicationStackedChartMainObjectDetail.createChartPanel();
		this.commitChartPanel = this.commitStackedChartMainObjectDetail.createChartPanel();
		this.configurationChartPanel = this.configurationStackedChartMainObjectDetail.createChartPanel();
		this.administrativeChartPanel = this.administrativeStackedChartMainObjectDetail.createChartPanel();
		this.networkChartPanel = this.networkStackedChartMainObjectDetail.createChartPanel();
		this.queuningChartPanel = this.queuningStackedChartMainObjectDetail.createChartPanel();
		this.clusterChartPanel = this.clusterStackedChartMainObjectDetail.createChartPanel();
		this.otherChartPanel = this.otherStackedChartMainObjectDetail.createChartPanel();
		
		/** Gantt graph */
		this.cpuSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("cpuLabel.text"));
		this.schedulerSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("schedulerLabel.text"));
		this.userIOSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("userIOLabel.text"));
		this.systemIOSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("systemIOLabel.text"));
		this.concurrencySqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("concurrencyLabel.text"));
		this.applicationSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("applicationsLabel.text"));
		this.commitSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("commitLabel.text"));
		this.configurationSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("configurationLabel.text"));
		this.administrativeSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("administrativeLabel.text"));
		this.networkSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("networkLabel.text"));
		this.queuningSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("queueningLabel.text"));
		this.clusterSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("clusterLabel.text"));
		this.otherSqlsAndSessions = new GanttDetails(this.mainFrame, this.database,
				Options.getInstance().getResource("otherLabel.text"));
		
		this.cpuSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.cpuSplitPaneMainDetail.add(this.cpuChartPanel, "top");
		this.cpuSplitPaneMainDetail.add(this.cpuSqlsAndSessions, "bottom");
		this.cpuSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.cpuSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.schedulerSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.schedulerSplitPaneMainDetail.add(this.schedulerChartPanel, "top");
		this.schedulerSplitPaneMainDetail.add(this.schedulerSqlsAndSessions, "bottom");
		this.schedulerSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.schedulerSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.userIOSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.userIOSplitPaneMainDetail.add(this.userIOChartPanel, "top");
		this.userIOSplitPaneMainDetail.add(this.userIOSqlsAndSessions, "bottom");
		this.userIOSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.userIOSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.systemIOSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.systemIOSplitPaneMainDetail.add(this.systemIOChartPanel, "top");
		this.systemIOSplitPaneMainDetail.add(this.systemIOSqlsAndSessions, "bottom");
		this.systemIOSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.systemIOSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.concurrencySplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.concurrencySplitPaneMainDetail.add(this.concurrencyChartPanel, "top");
		this.concurrencySplitPaneMainDetail.add(this.concurrencySqlsAndSessions, "bottom");
		this.concurrencySplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.concurrencySplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.applicationSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.applicationSplitPaneMainDetail.add(this.applicationChartPanel, "top");
		this.applicationSplitPaneMainDetail.add(this.applicationSqlsAndSessions, "bottom");
		this.applicationSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.applicationSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.commitSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.commitSplitPaneMainDetail.add(this.commitChartPanel, "top");
		this.commitSplitPaneMainDetail.add(this.commitSqlsAndSessions, "bottom");
		this.commitSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.commitSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.configurationSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.configurationSplitPaneMainDetail.add(this.configurationChartPanel, "top");
		this.configurationSplitPaneMainDetail.add(this.configurationSqlsAndSessions, "bottom");
		this.configurationSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.configurationSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.administrativeSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.administrativeSplitPaneMainDetail.add(this.administrativeChartPanel, "top");
		this.administrativeSplitPaneMainDetail.add(this.administrativeSqlsAndSessions, "bottom");
		this.administrativeSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.administrativeSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.networkSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.networkSplitPaneMainDetail.add(this.networkChartPanel, "top");
		this.networkSplitPaneMainDetail.add(this.networkSqlsAndSessions, "bottom");
		this.networkSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.networkSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.queuningSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.queuningSplitPaneMainDetail.add(this.queuningChartPanel, "top");
		this.queuningSplitPaneMainDetail.add(this.queuningSqlsAndSessions, "bottom");
		this.queuningSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.queuningSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.clusterSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.clusterSplitPaneMainDetail.add(this.clusterChartPanel, "top");
		this.clusterSplitPaneMainDetail.add(this.clusterSqlsAndSessions, "bottom");
		this.clusterSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.clusterSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.otherSplitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.otherSplitPaneMainDetail.add(this.otherChartPanel, "top");
		this.otherSplitPaneMainDetail.add(this.otherSqlsAndSessions, "bottom");
		this.otherSplitPaneMainDetail.setDividerLocation(this.dividerLocation);
		this.otherSplitPaneMainDetail.setOneTouchExpandable(true);
		
		this.tabsDetail.add(this.cpuSplitPaneMainDetail,
				Options.getInstance().getResource("cpuLabel.text"));
		this.tabsDetail.add(this.schedulerSplitPaneMainDetail,
				Options.getInstance().getResource("schedulerLabel.text"));
		this.tabsDetail.add(this.userIOSplitPaneMainDetail,
				Options.getInstance().getResource("userIOLabel.text"));
		this.tabsDetail.add(this.systemIOSplitPaneMainDetail,
				Options.getInstance().getResource("systemIOLabel.text"));
		this.tabsDetail.add(this.concurrencySplitPaneMainDetail,
				Options.getInstance().getResource("concurrencyLabel.text"));
		this.tabsDetail.add(this.applicationSplitPaneMainDetail,
				Options.getInstance().getResource("applicationsLabel.text"));
		this.tabsDetail.add(this.commitSplitPaneMainDetail,
				Options.getInstance().getResource("commitLabel.text"));
		this.tabsDetail.add(this.configurationSplitPaneMainDetail,
				Options.getInstance().getResource("configurationLabel.text"));
		this.tabsDetail.add(this.administrativeSplitPaneMainDetail,
				Options.getInstance().getResource("administrativeLabel.text"));
		this.tabsDetail.add(this.networkSplitPaneMainDetail,
				Options.getInstance().getResource("networkLabel.text"));
		this.tabsDetail.add(this.queuningSplitPaneMainDetail,
				Options.getInstance().getResource("queueningLabel.text"));
		this.tabsDetail.add(this.clusterSplitPaneMainDetail,
				Options.getInstance().getResource("clusterLabel.text"));
		this.tabsDetail.add(this.otherSplitPaneMainDetail,
				Options.getInstance().getResource("otherLabel.text"));
		
		this.cpuChartPanel.addListenerReleaseMouse(this.cpuSqlsAndSessions);
		this.schedulerChartPanel.addListenerReleaseMouse(this.schedulerSqlsAndSessions);
		this.userIOChartPanel.addListenerReleaseMouse(this.userIOSqlsAndSessions);
		this.systemIOChartPanel.addListenerReleaseMouse(this.systemIOSqlsAndSessions);
		this.concurrencyChartPanel.addListenerReleaseMouse(this.concurrencySqlsAndSessions);
		this.applicationChartPanel.addListenerReleaseMouse(this.applicationSqlsAndSessions);
		this.commitChartPanel.addListenerReleaseMouse(this.commitSqlsAndSessions);
		this.configurationChartPanel.addListenerReleaseMouse(this.configurationSqlsAndSessions);
		this.administrativeChartPanel.addListenerReleaseMouse(this.administrativeSqlsAndSessions);
		this.networkChartPanel.addListenerReleaseMouse(this.networkSqlsAndSessions);
		this.queuningChartPanel.addListenerReleaseMouse(this.queuningSqlsAndSessions);
		this.clusterChartPanel.addListenerReleaseMouse(this.clusterSqlsAndSessions);
		this.otherChartPanel.addListenerReleaseMouse(this.otherSqlsAndSessions);
		
	    this.cpuChartPanel.addListenerReleaseMouse(this.statusBar);
		this.schedulerChartPanel.addListenerReleaseMouse(this.statusBar);
		this.userIOChartPanel.addListenerReleaseMouse(this.statusBar);
		this.systemIOChartPanel.addListenerReleaseMouse(this.statusBar);
		this.concurrencyChartPanel.addListenerReleaseMouse(this.statusBar);
		this.applicationChartPanel.addListenerReleaseMouse(this.statusBar);
		this.commitChartPanel.addListenerReleaseMouse(this.statusBar);
		this.configurationChartPanel.addListenerReleaseMouse(this.statusBar);
		this.administrativeChartPanel.addListenerReleaseMouse(this.statusBar);
		this.networkChartPanel.addListenerReleaseMouse((Object)this.statusBar);
		this.queuningChartPanel.addListenerReleaseMouse((Object)this.statusBar);
		this.clusterChartPanel.addListenerReleaseMouse((Object)this.statusBar);
		this.otherChartPanel.addListenerReleaseMouse((Object)this.statusBar);
	
		ChangeListener changeListener = new ChangeListener() {
		      public void stateChanged(ChangeEvent changeEvent) {
		        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		        int index = sourceTabbedPane.getSelectedIndex();
		        statusBar.updateLabelStringDetail(
		        		sourceTabbedPane.getTitleAt(index));
		      }
		    };
		 tabsDetail.addChangeListener(changeListener);
		
		this.add(tabsDetail, BorderLayout.CENTER);
	}
	
	/**
	 * Load data to dataset
	 */
	public void loadDataToDataSet(){
		this.database.saveStackedXYAreaChartDetail(
				this.cpuStackedChartMainObjectDetail, 
				Options.getInstance().getResource("cpuLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.schedulerStackedChartMainObjectDetail, 
				Options.getInstance().getResource("schedulerLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.userIOStackedChartMainObjectDetail, 
				Options.getInstance().getResource("userIOLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.systemIOStackedChartMainObjectDetail, 
				Options.getInstance().getResource("systemIOLabel.text"));	
		this.database.saveStackedXYAreaChartDetail(
				this.concurrencyStackedChartMainObjectDetail, 
				Options.getInstance().getResource("concurrencyLabel.text"));		
		this.database.saveStackedXYAreaChartDetail(
				this.applicationStackedChartMainObjectDetail, 
				Options.getInstance().getResource("applicationsLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.commitStackedChartMainObjectDetail, 
				Options.getInstance().getResource("commitLabel.text"));		
		this.database.saveStackedXYAreaChartDetail(
				this.configurationStackedChartMainObjectDetail, 
				Options.getInstance().getResource("configurationLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.administrativeStackedChartMainObjectDetail, 
				Options.getInstance().getResource("administrativeLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.networkStackedChartMainObjectDetail, 
				Options.getInstance().getResource("networkLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.queuningStackedChartMainObjectDetail, 
				Options.getInstance().getResource("queueningLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.clusterStackedChartMainObjectDetail, 
				Options.getInstance().getResource("clusterLabel.text"));
		this.database.saveStackedXYAreaChartDetail(
				this.otherStackedChartMainObjectDetail, 
				Options.getInstance().getResource("otherLabel.text"));
		
		this.database.initialLoadingDataToChartPanelDataSetDetail();
		
		this.cpuStackedChartMainObjectDetail.setTitle();
		this.schedulerStackedChartMainObjectDetail.setTitle();
		this.userIOStackedChartMainObjectDetail.setTitle();
		this.systemIOStackedChartMainObjectDetail.setTitle();
		this.concurrencyStackedChartMainObjectDetail.setTitle();
		this.applicationStackedChartMainObjectDetail.setTitle();
		this.commitStackedChartMainObjectDetail.setTitle();
		this.configurationStackedChartMainObjectDetail.setTitle();
		this.administrativeStackedChartMainObjectDetail.setTitle();
		this.networkStackedChartMainObjectDetail.setTitle();
		this.queuningStackedChartMainObjectDetail.setTitle();
		this.clusterStackedChartMainObjectDetail.setTitle();
		this.otherStackedChartMainObjectDetail.setTitle();
		
		this.setThresholdMaxCpu();
	}
	
	/**
	 * Set the threshold max cpu (detail).
	 * @param maxCpu
	 */
	public void setThresholdMaxCpu(Double maxCpu){
		this.maxCpu = maxCpu;
	}
	
	/**
     * Set upper bound of range axis (detail)
     * @param bound
     */
    public void setUpperBoundOfRangeAxis(double bound){
    	this.cpuStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.schedulerStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.userIOStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.systemIOStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.concurrencyStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.applicationStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.commitStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.configurationStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.administrativeStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.networkStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.queuningStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.clusterStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
		this.otherStackedChartMainObjectDetail.setUpperBoundOfRangeAxis(bound);
    }
	
    /**
     * Get thumbnail details charts
     * 
     * @return
     */
    public JPanel getThumbnailDetailPanel(){
    	JPanel mainPanel = new JPanel(new GridLayout(3, 5));
    	
    	BufferedImage thumbCpu = 
    		this.cpuStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageCpu = new ImageIcon(thumbCpu);
        mainPanel.add(new JButton(imageCpu));
        
    	BufferedImage thumbScheduler = 
    		this.schedulerStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageScheduler = new ImageIcon(thumbScheduler);
        mainPanel.add(new JButton(imageScheduler));
        
    	BufferedImage thumbUserIO = 
    		this.userIOStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageUserIO = new ImageIcon(thumbUserIO);
        mainPanel.add(new JButton(imageUserIO));
        
    	BufferedImage thumbSystemIO = 
    		this.systemIOStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageSystemIO = new ImageIcon(thumbSystemIO);
        mainPanel.add(new JButton(imageSystemIO));
        
    	BufferedImage thumbConcurrency = 
    		this.concurrencyStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageConcurrency = new ImageIcon(thumbConcurrency);
        mainPanel.add(new JButton(imageConcurrency));
        
    	BufferedImage thumbApplication = 
    		this.applicationStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageApplication = new ImageIcon(thumbApplication);
        mainPanel.add(new JButton(imageApplication));
        
    	BufferedImage thumbCommit = 
    		this.commitStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageCommit = new ImageIcon(thumbCommit);
        mainPanel.add(new JButton(imageCommit));
        
    	BufferedImage thumbConfiguration = 
    		this.configurationStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageConfiguration = new ImageIcon(thumbConfiguration);
        mainPanel.add(new JButton(imageConfiguration));
        
    	BufferedImage thumbAdministrative = 
    		this.administrativeStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageAdministrative = new ImageIcon(thumbAdministrative);
        mainPanel.add(new JButton(imageAdministrative));
        
    	BufferedImage thumbNetwork = 
    		this.networkStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageNetwork = new ImageIcon(thumbNetwork);
        mainPanel.add(new JButton(imageNetwork));
        
        mainPanel.add(new JPanel());
        
    	BufferedImage thumbQueuning = 
    		this.queuningStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageQueuning = new ImageIcon(thumbQueuning);
        mainPanel.add(new JButton(imageQueuning));
        
    	BufferedImage thumbCluster = 
    		this.clusterStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageCluster = new ImageIcon(thumbCluster);
        mainPanel.add(new JButton(imageCluster));
        
    	BufferedImage thumbOther = 
    		this.otherStackedChartMainObjectDetail.
    			createBufferedImage(120, 80, 360, 240,null);
    	ImageIcon imageOther = new ImageIcon(thumbOther);
        mainPanel.add(new JButton(imageOther));
    	
        mainPanel.add(new JPanel());
        
    	return mainPanel;
    }
    
	 /**
     * Sets the threshold max cpu for all charts.
     */
    private void setThresholdMaxCpu(){  	
    	this.cpuStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.schedulerStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.userIOStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.systemIOStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.concurrencyStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.applicationStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.commitStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.configurationStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.administrativeStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.networkStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.queuningStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.clusterStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		this.otherStackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
      }
    
   /**
	* Update xAxis label.
	* @param time the time
	*/
   public void updatexAxisLabel(double time){  	  
	   this.cpuStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.schedulerStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.userIOStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.systemIOStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.concurrencyStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.applicationStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.commitStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.configurationStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.administrativeStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.networkStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.queuningStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.clusterStackedChartMainObjectDetail.updatexAxisLabel(time);
		this.otherStackedChartMainObjectDetail.updatexAxisLabel(time);
   }
    
   /**
    * Set top sql value
    * @param value
    */
   public void setTopSqlsSqlText(int value){
	   this.cpuSqlsAndSessions.setTopSqlsSqlText(value);
	   this.schedulerSqlsAndSessions.setTopSqlsSqlText(value);
	   this.userIOSqlsAndSessions.setTopSqlsSqlText(value);
	   this.systemIOSqlsAndSessions.setTopSqlsSqlText(value);
	   this.concurrencySqlsAndSessions.setTopSqlsSqlText(value);
	   this.applicationSqlsAndSessions.setTopSqlsSqlText(value);
	   this.commitSqlsAndSessions.setTopSqlsSqlText(value);
	   this.configurationSqlsAndSessions.setTopSqlsSqlText(value);
	   this.administrativeSqlsAndSessions.setTopSqlsSqlText(value);
	   this.networkSqlsAndSessions.setTopSqlsSqlText(value);
	   this.queuningSqlsAndSessions.setTopSqlsSqlText(value);
	   this.clusterSqlsAndSessions.setTopSqlsSqlText(value);
	   this.otherSqlsAndSessions.setTopSqlsSqlText(value);
   }
   
   /**
    * Select top sql plan
    * 
    * @param value
    */
   public void setSelectSqlPlan(boolean isSelect){
	   this.cpuSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.schedulerSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.userIOSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.systemIOSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.concurrencySqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.applicationSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.commitSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.configurationSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.administrativeSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.networkSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.queuningSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.clusterSqlsAndSessions.setSelectSqlPlan(isSelect);
	   this.otherSqlsAndSessions.setSelectSqlPlan(isSelect);
   }
   
   /**
    * Get name of selected tab
    * @return name
    */
   public String getCurrentTabName(){
	   return this.tabsDetail.getTitleAt(this.tabsDetail.getSelectedIndex());
   }
   
}
