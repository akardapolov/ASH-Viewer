/*
 *-------------------
 * The DetailsPanelHSession.java is part of ASH Viewer
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import org.ash.gui.StackedChartSessions;
import org.ash.history.detail.GanttDetailsH;
import org.ash.history.detail.StackedChartDetail;
import org.ash.history.ASHDatabaseH;
import org.ash.util.Options;
import org.ash.util.ProgressBarUtil;
import org.jfree.chart.ChartPanel;

import com.sleepycat.je.DatabaseException;

public class DetailsPanelHSession extends JPanel implements ActionListener{

	/** The database. */
	private ASHDatabaseH database;
			
	/** The main panel. */
	private JPanel mainPanel;
	
	/** The button panel. */
	private JToolBar buttonPanel;
	   
	/** The radio button for cpu */
	private JRadioButton cpuRadioButton = new JRadioButton();
	
	/** The radio button for scheduler */
	private JRadioButton schedulerRadioButton = new JRadioButton();
	
	/** The radio button for User I/O */
	private JRadioButton userIORadioButton = new JRadioButton();
	
	/** The radio button for System I/O */
	private JRadioButton systemIORadioButton = new JRadioButton();
	
	/** The radio button for Concurrency */
	private JRadioButton concurrencyRadioButton = new JRadioButton();
	
	/** The radio button for Application */
	private JRadioButton applicationRadioButton = new JRadioButton();
	
	/** The radio button for Commit */
	private JRadioButton commitRadioButton = new JRadioButton();
	
	/** The radio button for Configuration */
	private JRadioButton configurationRadioButton = new JRadioButton();
	
	/** The radio button for Administrative */
	private JRadioButton administrativeRadioButton = new JRadioButton();
	
	/** The radio button for Network */
	private JRadioButton networkRadioButton = new JRadioButton();
	
	/** The radio button for Queuning */
	private JRadioButton queuningRadioButton = new JRadioButton();
	
	/** The radio button for Cluster */
	private JRadioButton clusterRadioButton = new JRadioButton();
	
	/** The radio button for Other */
	private JRadioButton otherRadioButton = new JRadioButton();
	
	/** Button group for details radio buttons */
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	/** The max cpu. */
	private double maxCpu;
	
	/** The begin time. */
	private double beginTime = 0.0;
	 
	/** The end time. */
	private double endTime = 0.0;
	
	/**
	 *  Constructor DetailFrame
	 *  
	 * @param database
	 * @param beginTime
	 * @param endTime
	 * @param maxCpu
	 */
	public DetailsPanelHSession(ASHDatabaseH database, double beginTime, double endTime, double maxCpu){
		this.database = database;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.maxCpu = maxCpu;
		this.initialize();
	}
	
	/**
	 * Initialize DetailFrame
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout());
		JSplitPane splitPaneMainDetail = new JSplitPane();
		
		this.cpuRadioButton.setText(Options.getInstance().getResource("AllSessionLabel.text"));
		this.cpuRadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.cpuRadioButton);
		/*this.schedulerRadioButton.setText(Options.getInstance().getResource("schedulerLabel.text"));
		this.schedulerRadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.schedulerRadioButton);
		this.userIORadioButton.setText(Options.getInstance().getResource("userIOLabel.text"));
		this.userIORadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.userIORadioButton);
		this.systemIORadioButton.setText(Options.getInstance().getResource("systemIOLabel.text"));
		this.systemIORadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.systemIORadioButton);
		this.concurrencyRadioButton.setText(Options.getInstance().getResource("concurrencyLabel.text"));
		this.concurrencyRadioButton.addItemListener(new SelectItemListenerRadioButton());	
		this.setFont(this.concurrencyRadioButton);
		this.applicationRadioButton.setText(Options.getInstance().getResource("applicationsLabel.text"));
		this.applicationRadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.applicationRadioButton);
		this.commitRadioButton.setText(Options.getInstance().getResource("commitLabel.text"));
		this.commitRadioButton.addItemListener(new SelectItemListenerRadioButton());		
		this.setFont(this.commitRadioButton);
		this.configurationRadioButton.setText(Options.getInstance().getResource("configurationLabel.text"));
		this.configurationRadioButton.addItemListener(new SelectItemListenerRadioButton());	
		this.setFont(this.configurationRadioButton);
		this.administrativeRadioButton.setText(Options.getInstance().getResource("administrativeLabel.text"));
		this.administrativeRadioButton.addItemListener(new SelectItemListenerRadioButton());	
		this.setFont(this.administrativeRadioButton);
		this.networkRadioButton.setText(Options.getInstance().getResource("networkLabel.text"));
		this.networkRadioButton.addItemListener(new SelectItemListenerRadioButton());		
		this.setFont(this.networkRadioButton);
		this.queuningRadioButton.setText(Options.getInstance().getResource("queueningLabel.text"));
		this.queuningRadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.queuningRadioButton);
		this.clusterRadioButton.setText(Options.getInstance().getResource("clusterLabel.text"));
		this.clusterRadioButton.addItemListener(new SelectItemListenerRadioButton());		
		this.setFont(this.clusterRadioButton);
		this.otherRadioButton.setText(Options.getInstance().getResource("otherLabel.text"));
		this.otherRadioButton.addItemListener(new SelectItemListenerRadioButton());
		this.setFont(this.otherRadioButton);*/
		
		this.buttonGroup.add(cpuRadioButton);
		/*this.buttonGroup.add(schedulerRadioButton);
		this.buttonGroup.add(userIORadioButton);
		this.buttonGroup.add(systemIORadioButton);
		this.buttonGroup.add(concurrencyRadioButton);
		this.buttonGroup.add(applicationRadioButton);
		this.buttonGroup.add(commitRadioButton);
		this.buttonGroup.add(configurationRadioButton);
		this.buttonGroup.add(administrativeRadioButton);
		this.buttonGroup.add(networkRadioButton);
		this.buttonGroup.add(queuningRadioButton);
		this.buttonGroup.add(clusterRadioButton);
		this.buttonGroup.add(otherRadioButton);*/
		
		/** Button panel fot buttons */
		this.buttonPanel = new JToolBar("PanelButton");
		this.buttonPanel.setFloatable(false);
		this.buttonPanel.setBorder(new EtchedBorder());
		
		this.buttonPanel.add(this.cpuRadioButton);
		/*this.buttonPanel.add(this.schedulerRadioButton);
		this.buttonPanel.add(this.userIORadioButton);
		this.buttonPanel.add(this.systemIORadioButton);
		this.buttonPanel.add(this.concurrencyRadioButton);
		this.buttonPanel.add(this.applicationRadioButton);
		this.buttonPanel.add(this.commitRadioButton);
		this.buttonPanel.add(this.configurationRadioButton);
		this.buttonPanel.add(this.administrativeRadioButton);
		this.buttonPanel.add(this.networkRadioButton);
		this.buttonPanel.add(this.queuningRadioButton);
		this.buttonPanel.add(this.clusterRadioButton);
		this.buttonPanel.add(this.otherRadioButton);*/
						
		splitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneMainDetail.add(new JPanel(), "top");
		splitPaneMainDetail.add(new JPanel(), "bottom");
		splitPaneMainDetail.setDividerLocation(230);
		splitPaneMainDetail.setOneTouchExpandable(true);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setVisible(true);
		this.mainPanel.add(splitPaneMainDetail, BorderLayout.CENTER);
				
		this.add(this.buttonPanel, BorderLayout.NORTH);
		this.add(this.mainPanel, BorderLayout.CENTER);		
	}
	
	/**
	 * Set Font for JRadioButton. 
	 * 
	 * @param radioButton
	 */
	private void setFont(JRadioButton radioButton){
		radioButton.setFont(new Font("SansSerif", Font.PLAIN, 9));
	}
	
	/**
	 * Set the threshold max cpu.
	 * @param maxCpu
	 */
	public void setThresholdMaxCpu(Double maxCpu){
		this.maxCpu = maxCpu;
	}
	
	
	/**
	 * Item listener for cpu used and events radio buttons
	 *
	 */
	class SelectItemListenerRadioButton implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("AllSessionLabel.text"))){
					addChartPanel(Options.getInstance().getResource("AllSessionLabel.text"));
				}
				/*else if (sel.getText().equals(Options.getInstance().getResource("schedulerLabel.text"))){
					addChartPanel(Options.getInstance().getResource("schedulerLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("userIOLabel.text"))){
					addChartPanel(Options.getInstance().getResource("userIOLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("systemIOLabel.text"))){
					addChartPanel(Options.getInstance().getResource("systemIOLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("concurrencyLabel.text"))){
					addChartPanel(Options.getInstance().getResource("concurrencyLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("applicationsLabel.text"))){
					addChartPanel(Options.getInstance().getResource("applicationsLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("commitLabel.text"))){
					addChartPanel(Options.getInstance().getResource("commitLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("configurationLabel.text"))){
					addChartPanel(Options.getInstance().getResource("configurationLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("administrativeLabel.text"))){
					addChartPanel(Options.getInstance().getResource("administrativeLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("networkLabel.text"))){
					addChartPanel(Options.getInstance().getResource("networkLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("queueningLabel.text"))){
					addChartPanel(Options.getInstance().getResource("queueningLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("clusterLabel.text"))){
					addChartPanel(Options.getInstance().getResource("clusterLabel.text"));
				}
				else if (sel.getText().equals(Options.getInstance().getResource("otherLabel.text"))){
					addChartPanel(Options.getInstance().getResource("otherLabel.text"));
				}*/
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Add ChartPanel to mainPanel
	 * @param waitClass
	 */
	private void addChartPanel(final String waitClass){
		
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
					addChartPanelT(waitClass);
				
			}
		};
		t.start();
		
	}
	
	/**
	 * Add ChartPanel to mainPanel
	 * @param waitClass
	 */
	private void addChartPanelT(String waitClass){
		
		JSplitPane splitPaneMainDetail = new JSplitPane();
		
		/*StackedChartDetail stackedChartMainObjectDetail = 
			new StackedChartDetail(database,waitClass);*/
		StackedChartSessionsH stackedChartMainObjectDetail =
				new StackedChartSessionsH(database);
		
		
		ChartPanel chartDetailPanel = null;
		try {
			chartDetailPanel = stackedChartMainObjectDetail.createChartPanel(this.beginTime,this.endTime);
		} catch (DatabaseException e) {
			e.printStackTrace(); 
		}
		//stackedChartMainObjectDetail.setThresholdMaxCpu(this.maxCpu);
		
		//GanttDetailsH sqlsAndSessions = new GanttDetailsH(database, waitClass);
		//chartDetailPanel.addListenerReleaseMouse(sqlsAndSessions);
					
		
		//database.clearStackedXYAreaChartDetail();
		//database.saveStackedXYAreaChartDetail(stackedChartMainObjectDetail, waitClass);
		//database.initialLoadingDataToChartPanelDataSetDetail(waitClass,this.beginTime,this.endTime);
		
		//stackedChartMainObjectDetail.setTitle();
		
		this.mainPanel.removeAll();
		
		splitPaneMainDetail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneMainDetail.add(chartDetailPanel,"top");
		splitPaneMainDetail.add(new JPanel(), "bottom");
		splitPaneMainDetail.setDividerLocation(215);
		splitPaneMainDetail.setOneTouchExpandable(true);
		
		this.mainPanel.add(splitPaneMainDetail, BorderLayout.CENTER);
		this.validate();
		this.repaint();
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
