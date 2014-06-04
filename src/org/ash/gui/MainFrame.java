/*
 *-------------------
 * The MainFrame.java is part of ASH Viewer
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Point;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ash.conn.gui.ConnectionFrame;
import org.ash.conn.model.Model;
import org.ash.conn.settings.DbConnectionUtil;
import org.ash.database.Database10g1;
import org.ash.database.Database10g11gSE;
import org.ash.database.Database10g2;
import org.ash.database.Database11g1;
import org.ash.database.Database8i;
import org.ash.database.Database9i;
import org.ash.database.ASHDatabase;
import org.ash.detail.DetailPanels;
import org.ash.history.MainPanel;
import org.ash.invoker.Collector;
import org.ash.invoker.Collector10g11gUI;
import org.ash.invoker.Collector9iAndSEDB;
import org.ash.invoker.Collector9iAndSEUI;
import org.ash.util.Options;
import org.jfree.chart.ChartPanel;

import com.sleepycat.je.DatabaseException;

/**
 * The Class MainFrame.
 */
public class MainFrame extends JFrame implements ActionListener{

	/** The db conn util. */
	private static DbConnectionUtil dbConnUtil = null;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3257009847668192306L;
	
	/** The model. */
	private Model model;
	
	/** The button panel. */
	private JToolBar buttonPanel;
	
	/** The split pane main. */
	private JSplitPane splitPaneMain;
	
	/** The database. */
	private ASHDatabase database;
	
	/** The main panel. */
	private JPanel mainPanel;
	
	/** The chart chart panel. */
	private ChartPanel chartChartPanel;
	
	/** The stacked chart main object. */
	private StackedChart stackedChartMainObject;
	
	/** The collector. Update UI and load data from DB for 10g, 
	 * for 9i - only update UI */
	private Collector collectorUI;
	
	/** The collerctor. Collect data from DB for 9i database*/
	private Collector collectorDB;
	
	/** The sqls and sessions. */
	private Gantt sqlsAndSessions;
	
	/** The latency is 15 sec.*/
	private int latency = 15000;
	
	/** The JButton - settings button*/
	private JButton jButtonSettings = new JButton();
	
	/** The JButton - detail button*/
	private JButton jButtonThumbnail = new JButton();
		
    /** The status bar. */
    private StatusBar statusBar;
    
    /** The menu file exit. */
    private JMenuItem menuFileExit = new JMenuItem();
    
    /** The menu help about. */
    private JMenuItem menuHelpAbout = new JMenuItem();
    
	/** The settings dialog. */
	private Settings settingsDialog;
	
	/** The detail panel. */
	private DetailPanels detailJPanel;
	
	/** The tabbed panel. */
	private JTabbedPane tabsMain;
	
	/** The history panel. */
	private MainPanel historyJPanel;
	
	/**
	 * Instantiates a new main frame.
	 */
	public MainFrame() {
		super();
		Options.getInstance().setDateFormat("dd.MM.yyyy hh:mm:ss");
		Options.getInstance().setLanguage(Locale.getDefault().getLanguage());
		Options.getInstance().setJtextAreaSqlTextGanttHAndDetailsH();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) screenSize.getWidth(),
				(int) screenSize.getHeight() - 24);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);

		// retrieve the HashMap of Default properties.  
		//UIDefaults uiDefs = UIManager.getDefaults();
		
		// Set indent for SQL plan JTreeTable.  
		//uiDefs.put("Tree.leftChildIndent" , new Integer( 1 ) ); 
		//uiDefs.put("Tree.rightChildIndent" , new Integer( 8 ) );  
		
		try {
			jbInit();
			menuFileNewConn_actionPerformed(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Jb init.
	 * 
	 * @throws Exception the exception
	 */
	private void jbInit() throws Exception {
		/** set layout */
		this.setLayout(new BorderLayout());
		this.tabsMain = new JTabbedPane();		
		
		this.model = new Model();
		this.splitPaneMain = new JSplitPane();
		this.statusBar = new StatusBar();
		
		this.jButtonSettings.setMnemonic(Options.getInstance().getResource("settingsMain.mnemonic").charAt(0));
		this.jButtonSettings.setText(Options.getInstance().getResource("settingsMain.text"));
		this.jButtonSettings.setPreferredSize(new Dimension(100,30));
		this.jButtonSettings.addActionListener(this);

		this.jButtonThumbnail.setMnemonic(Options.getInstance().getResource("ThumbnailMain.mnemonic").charAt(0));
		this.jButtonThumbnail.setText(Options.getInstance().getResource("ThumbnailMain.text"));
		this.jButtonThumbnail.setPreferredSize(new Dimension(100,30));
		this.jButtonThumbnail.addActionListener(this);
		
		/** Button panel fot buttons */
		this.buttonPanel = new JToolBar("PanelButton");
		this.buttonPanel.setFloatable(false);
		this.buttonPanel.setBorder(new EtchedBorder());
		
		/** Layout of buttons */
		this.buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		this.buttonPanel.add(this.jButtonSettings);
		this.buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		//this.buttonPanel.add(this.jButtonThumbnail);
		//this.buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		//this.buttonPanel.add(this.jButtonDetail);
		//this.buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		//this.buttonPanel.add(this.spinerRangeWindowLabel);
		//this.buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		//this.buttonPanel.add(this.spinerRangeWindow);
		//this.buttonPanel.add(Box.createRigidArea(new Dimension(this.getWidth()-250, 0)));
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setVisible(true);
				
		this.settingsDialog = new Settings(this);
		this.settingsDialog.setLocation(buttonPanel.getX()+100,buttonPanel.getY()+50);
		this.settingsDialog.setVisible(false);
		
	}

	/**
	 * Initialize.
	 * 
	 * @throws DatabaseException the database exception
	 */
	public void initialize() throws DatabaseException {
		
		/** Initialize progressBar*/
		ProgressOnStart progressBarOnStart = new ProgressOnStart();
		
		/** Initialize connection pool to Oracle database */
		this.initializeConnectionPool();
			progressBarOnStart.setProgressValueAndTaskOutput(5,"Initialize connection pool");

		/** Initialize Berkeley DB, load (append) data to local database*/
		this.initializeBerkeleyDatabaseAndLoading();
			progressBarOnStart.setProgressValueAndTaskOutput(20, "Initialize local DB, load data");
		
		/** Initialize Detail panel */
		this.detailJPanel = new DetailPanels(this,this.database,this.statusBar);
		this.detailJPanel.setVisible(false);
			progressBarOnStart.setProgressValueAndTaskOutput(35,"Initialize detail tab");
		
		/** Initialize History panel */
		this.historyJPanel = new MainPanel(this,this.statusBar);
		this.historyJPanel.setVisible(false);
			progressBarOnStart.setProgressValueAndTaskOutput(40,"Initialize history tab");

		/** Initialize main Top Activity chart panel */
		this.stackedChartMainObject = new StackedChart(this.database);
		this.setThresholdMaxCpu();
		this.chartChartPanel = this.stackedChartMainObject.createChartPanel();
			progressBarOnStart.setProgressValueAndTaskOutput(60,"Initialize Top Activity tab");
			
		/** Initialize Sqls & Sessions JPanel*/
		this.sqlsAndSessions = new Gantt(this, this.database);
				
		/** Add stacked area chart and gantt area to collector */
		this.collectorUI.addListenerStart(this.stackedChartMainObject);
			
		/** Set JSplitPane parameters */
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.add(this.chartChartPanel, "top");
		this.splitPaneMain.add(this.sqlsAndSessions, "bottom");
		this.splitPaneMain.setDividerLocation(290);
		this.splitPaneMain.setOneTouchExpandable(true);
			progressBarOnStart.setProgressValueAndTaskOutput(80,"Initialize Top SQL and Top Sessions");

		/** Add sqlsAndSessions and statusBar to Top Activity chart panel listener */
		this.chartChartPanel.addListenerReleaseMouse(this.sqlsAndSessions);
		this.chartChartPanel.addListenerReleaseMouse(this.statusBar);
		
		/** Add JSplitPane to mainPanel */
		this.mainPanel.add(splitPaneMain, BorderLayout.CENTER);
		this.sqlsAndSessions.repaint();		
		
		/** Load data to detail data sets */
		this.detailJPanel.loadDataToDataSet();	
		this.collectorUI.addListenerStart(this.detailJPanel);
			progressBarOnStart.setProgressValueAndTaskOutput(90, "Load data to detail data set");
	
			
		/** Add Main, Detail, History tabs */
		this.tabsMain.add(this.mainPanel,
				Options.getInstance().getResource("tabMain.text"));
		this.tabsMain.add(this.detailJPanel,
				Options.getInstance().getResource("tabDetail.text"));
		this.tabsMain.add(this.historyJPanel,
				Options.getInstance().getResource("tabHistory.text"));
			
		/** Create menu bar */
		this.setJMenuBar(createMenuBar());
		
		/** Add buttonPanel, tabsMain and statusBar to MainFrame */
		this.getContentPane().add(this.buttonPanel, BorderLayout.NORTH);
		this.getContentPane().add(this.tabsMain, BorderLayout.CENTER);
		this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
			
		/** Add changeTabsListener to tabsMain */
		this.tabsMain.addChangeListener(new ChangeTabsListener());
			progressBarOnStart.setProgressValueAndTaskOutput(100,"Done");
			progressBarOnStart.done();
			progressBarOnStart.dispose();
		/** Show main panel */			
		this.setTitle();
		this.validate();
		this.setVisible(true);

		/** Start collectors */
		this.collectorStart();
	}

	/**
	 * Initialize off line mode.
	 * 
	 * @throws DatabaseException the database exception
	 */
	public void initializeOffline(){
		
		/** Initialize History panel */
		this.historyJPanel = new MainPanel(this,this.statusBar);
		this.historyJPanel.setVisible(false);
		
		this.tabsMain.add(this.historyJPanel,
				Options.getInstance().getResource("tabHistory.text"));
		
		this.setJMenuBar(createMenuBar());
		this.getContentPane().add(this.buttonPanel, BorderLayout.NORTH);
		this.getContentPane().add(this.tabsMain, BorderLayout.CENTER);
		this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
		
		this.tabsMain.addChangeListener(new ChangeTabsListener());
		this.statusBar.updateLabelStringHistory();
		
		this.jButtonSettings.setEnabled(false);
		this.setTitle("ASH Viewer ::: Off-line mode :::");
		
		this.validate();
		this.setVisible(true);
		
	}
	
	/**
	 * Initialize berkeley database.
	 */
	private void initializeBerkeleyDatabaseAndLoading() {

		String versionOracleDB = model.getVersionDB();
				
		if (Options.getInstance().getEditionDb().equalsIgnoreCase("EE")){

			if (versionOracleDB.equalsIgnoreCase("10g2")) {
				this.database = new Database10g2(this.model);
				this.collectorUI = new Collector10g11gUI(this.database, this.latency);
				this.database.loadToLocalBDB();
				this.database.loadToLocalBDBCollector();
			}
			
			if (versionOracleDB.equalsIgnoreCase("10g1")) {
				this.database = new Database10g1(this.model);
				this.collectorUI = new Collector10g11gUI(this.database, this.latency);
				this.database.loadToLocalBDB();
				this.database.loadToLocalBDBCollector();
			}
			
			if (versionOracleDB.equalsIgnoreCase("11g")) {
				this.database = new Database11g1(this.model);
				this.collectorUI = new Collector10g11gUI(this.database, this.latency);
				this.database.loadToLocalBDB();
				this.database.loadToLocalBDBCollector();
			}
			/*if (versionOracleDB.equalsIgnoreCase("11g2")) {
				this.database = new Database11g2(this.model);
				this.collectorUI = new Collector10g11gUI(this.database, this.latency);
				this.database.loadToLocalBDB();
				this.database.loadToLocalBDBCollector();
			}*/
		} else { // SE
				this.database = new Database10g11gSE(this.model);
				this.collectorUI = new Collector9iAndSEUI(this.database, this.latency);
				this.collectorDB = new Collector9iAndSEDB(this.database, this.latency);
				this.database.loadToLocalBDB();
				this.collectorDB.start();
		}
		
		if (versionOracleDB.equalsIgnoreCase("9i")) {
			this.database = new Database9i(this.model);
			this.collectorUI = new Collector9iAndSEUI(this.database, this.latency);
			this.collectorDB = new Collector9iAndSEDB(this.database, this.latency);
			this.database.loadToLocalBDB();
			this.collectorDB.start();
		}
		
		if (versionOracleDB.equalsIgnoreCase("8i")) {
			this.database = new Database8i(this.model);
			this.collectorUI = new Collector9iAndSEUI(this.database, this.latency);
			this.collectorDB = new Collector9iAndSEDB(this.database, this.latency);
			this.database.loadToLocalBDB();
			this.collectorDB.start();
		}
		
		// Save ref. to DatabaseMain
		Options.getInstance().setDatabaseMain(this.database);

	}
	
	/**
	 * Start UI collector
	 */
	private void collectorStart(){
		this.collectorUI.start();
	}
	
	/**
	 * Gets the db conn util.
	 * 
	 * @return the db conn util
	 */
	public static DbConnectionUtil getDbConnUtil() {
		return dbConnUtil;
	}

	/**
	 * Sets the db conn util.
	 * 
	 * @param dbConnUtil the new db conn util
	 */
	public static void setDbConnUtil(DbConnectionUtil dbConnUtil) {
		MainFrame.dbConnUtil = dbConnUtil;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JFrame#processWindowEvent(java.awt.event.WindowEvent)
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			menuFileExit_actionPerformed(null);
		}
	}
	
	/**
	 * Sets the threshold max cpu.
	 */
	private void setThresholdMaxCpu(){
		Double valueSampleTime = 0.0;
		double maxCpu = Double.parseDouble(this.database.getParameter("cpu_count"));
		this.stackedChartMainObject.setThresholdMaxCpu(maxCpu);
		this.detailJPanel.setThresholdMaxCpu(maxCpu);
	}
	
	/**
	 * Sets the title.
	 */
	private void setTitle() {

		/** set title of current frame*/
		this.setTitle("ASH Viewer ::: "
				+ dbConnUtil.getDbConnection().getName() + "|"
				+ dbConnUtil.getDbConnection().getHost() + "|"
				+ dbConnUtil.getDbConnection().getPort() + "|"
				+ dbConnUtil.getDbConnection().getSID() + "|"
				+ dbConnUtil.getDbConnection().getUsername());
		
		/** load parameters to local BDB */
		this.database.saveParameterToLocalBDB("ASH.name", dbConnUtil.getDbConnection().getName());
		this.database.saveParameterToLocalBDB("ASH.host", dbConnUtil.getDbConnection().getHost());
		this.database.saveParameterToLocalBDB("ASH.port", dbConnUtil.getDbConnection().getPort());
		this.database.saveParameterToLocalBDB("ASH.sid", dbConnUtil.getDbConnection().getSID());
		this.database.saveParameterToLocalBDB("ASH.username", dbConnUtil.getDbConnection().getUsername());
		this.database.saveParameterToLocalBDB("ASH.version", Options.getInstance().getVersionDb());
	}	

	/**
	 * Initialize connection pool.
	 */
	private void initializeConnectionPool() {

		/** init connection parameter */
		String connParam = "jdbc:oracle:thin:@"
				+ dbConnUtil.getDbConnection().getUrl();

		/** init connection pool */
		model.connectionPoolInit("oracle.jdbc.pool.OracleDataSource",
				connParam, dbConnUtil.getDbConnection().getUsername(),
				dbConnUtil.getDbConnection().getPassword());
		
		/** check errors */
		if (model.getErrorMessage() != null) {
			JOptionPane.showMessageDialog(this, model.getErrorMessage(),
					"Sql error", JOptionPane.ERROR_MESSAGE);
			model.setErrorMessageNull();
			model = null;
			System.exit(0);
		}

		Options.getInstance().setNameOfConnection(
				dbConnUtil.getDbConnection().getName());
		Options.getInstance().setVersionDb(
				model.getVersionDB());
		Options.getInstance().setEditionDb(
				dbConnUtil.getDbConnection().getEdition());

	}

	/**
	 * Dispose alll.
	 */
	private void disposeAlll() {
		this.model.closeConnections();
		this.model.closeConnectionPool();
		this.model = null;
		this.buttonPanel = null;
		this.splitPaneMain = null;
	}


   /* (non-Javadoc)
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    */
   public void actionPerformed(ActionEvent e) {
			/** Get action command */
			String str = e.getActionCommand();

			/** Show settings window */
			if (str.equals(Options.getInstance().getResource("settingsMain.text"))) {
				settingsDialog.setModal(true);
				settingsDialog.setVisible(true);
			}
			/** Show thumbnail detail panel*/
			if (str.equals(Options.getInstance().getResource("ThumbnailMain.text"))) {
				Thumbnail thumbnail = new Thumbnail(this,detailJPanel.getThumbnailDetailPanel());
				thumbnail.setModal(true);
				thumbnail.setVisible(true);
			}
			
		}
	
   /**
    * Set upper bound of range axis
    * 
    * @param value
    */
   public void setUpperBoundOfRangeAxisTopA(double value){
	   stackedChartMainObject.setUpperBoundOfRangeAxis(value);
   }
   
   /**
    * Set upper bound of range axis
    * 
    * @param value
    */
   public void setUpperBoundOfRangeAxisDetail(double value){
		detailJPanel.setUpperBoundOfRangeAxis(value);
   }
   
   /**
    * Set count for top sql
    * 
    * @param value
    */
   public void setTopSqlsSqlTextTopA(int value){
	   sqlsAndSessions.setTopSqlsSqlText(value);
   }
   
   /**
    * Select sql plan data from TA
    * 
    * @param isSelectSqlPlan
    */
	public void setSelectSqlPlanTA(boolean isSelectSqlPlan) {
		sqlsAndSessions.setSelectSqlPlan(isSelectSqlPlan);
	}

   /**
    * Set count for top sql
    * 
    * @param value
    */
   public void setTopSqlsSqlTextDetail(int value){
	   this.detailJPanel.setTopSqlsSqlText(value);
   }
   
   /**
    * Select sql plan data from Detail
    * 
    * @param isSelectSqlPlan
    */
   public void setSelectSqlPlanDetail(boolean isSelectSqlPlan){
	   this.detailJPanel.setSelectSqlPlan(isSelectSqlPlan);
   }
   
   /**
    * Set range window for collerctorUI
    * 
    * @param value
    */
   public void setcollectorUIRangeWindow(int value){
	   collectorUI.setRangeWindow(value);
	   new switchToRangeWinModeAuto().start();
   }
   
   /**
    * Switch to auto mode (top activity)
    * 
    */
   public void switchToAuto(){
	   new switchToAuto().start();
   }
   
   /**
    * Switch to manual mode (top activity)
    * 
    */
   public void switchToManual(){
	   new switchToManual().start();
   }
 
    /**
     * Change tab listener
     * 
     */
    private final class ChangeTabsListener implements ChangeListener {
	public void stateChanged(ChangeEvent changeEvent) {
	    JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
	    int index = sourceTabbedPane.getSelectedIndex();
	    if (sourceTabbedPane.getTitleAt(index).
	    		equalsIgnoreCase(Options.getInstance().getResource("tabMain.text"))){
	    	statusBar.updateLabelStringTopActivity();
	    } else if(sourceTabbedPane.getTitleAt(index).
	    		equalsIgnoreCase(Options.getInstance().getResource("tabDetail.text"))) {
	    	statusBar.updateLabelStringDetail(detailJPanel.getCurrentTabName());
	    }
	      else if(sourceTabbedPane.getTitleAt(index).
	    		equalsIgnoreCase(Options.getInstance().getResource("tabHistory.text"))) {
	    	statusBar.updateLabelStringHistory();
	    }
	  }
}

	/**
	 * Switch UI to Auto mode selection
	 * 
	 */
	class switchToAuto extends Thread {
		
		@Override
		public void run(){
			
			double endTime = database.getSysdate();
			double beginTime = endTime - collectorUI.getRangeWindow();
			
			stackedChartMainObject.setSelectionChart(false);
			stackedChartMainObject.setFlagThresholdBeginTimeAutoSelection(true);
			
			stackedChartMainObject.setThresholdBeginTimeAutoSelection(
					beginTime,collectorUI.getRangeWindow()/collectorUI.getK());
			sqlsAndSessions.loadDataToJPanels(beginTime, endTime);
			statusBar.setRange(beginTime, endTime);
			
			if (!collectorUI.isListenerExist(sqlsAndSessions)){
				collectorUI.addListenerStart(sqlsAndSessions);
			}
			if (!collectorUI.isListenerExist(statusBar)){
				collectorUI.addListenerStart(statusBar);
			}
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					statusBar.setSelection("Auto");
					statusBar.setRangeWindow("("+settingsDialog.getSpinerRangeWindowValue()+")");
					settingsDialog.setSpinerRangeWindowEnabled(true);
				}
			});
		}
	}
	
	/**
	 * Switch UI to Manual mode selection
	 *
	 */
	class switchToManual extends Thread {
		
		@Override
		public void run(){
			
			stackedChartMainObject.setSelectionChart(true);
			stackedChartMainObject.removeThresholdBeginTimeAutoSelection();
			stackedChartMainObject.setFlagThresholdBeginTimeAutoSelection(false);
			
			if (collectorUI.isListenerExist(sqlsAndSessions)){
				collectorUI.removeListenerStart(sqlsAndSessions);
			}
			if (collectorUI.isListenerExist(statusBar)){
				collectorUI.removeListenerStart(statusBar);
			}
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					statusBar.setSelection("Manual");
					statusBar.setRangeWindow("");
					settingsDialog.setSpinerRangeWindowEnabled(false);
				}
			});
		}		
	}
	
	/**
	 * 	Load data to egantt charts when change range window
	 *
	 */
	class switchToRangeWinModeAuto extends Thread {
		
		@Override
		public void run(){
			
			double endTime = database.getSysdate();
			double beginTime = endTime - collectorUI.getRangeWindow();
			
			stackedChartMainObject.setSelectionChart(false);
			stackedChartMainObject.setFlagThresholdBeginTimeAutoSelection(true);
			
			stackedChartMainObject.setThresholdBeginTimeAutoSelection(
					beginTime,collectorUI.getRangeWindow()/collectorUI.getK());
			sqlsAndSessions.loadDataToJPanels(beginTime, endTime);
			statusBar.setRange(beginTime, endTime);
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					statusBar.setRangeWindow("("+settingsDialog.getSpinerRangeWindowValue()+")");
					settingsDialog.setSpinerRangeWindowEnabled(true);
				}
			});
		}
	}
 	
    /**
     * Creates the menu bar.
     * 
     * @return the j menu bar
     */
    private JMenuBar createMenuBar() {
    	JMenu menuFile = new JMenu();
    	
    	 	menuFile.setMnemonic(Options.getInstance().getResource("file.mnemonic").charAt(0));
    	    menuFile.setText(Options.getInstance().getResource("file.text"));
    	    menuFileExit.setMnemonic(Options.getInstance().getResource("exit.mnemonic").charAt(0));
    	    menuFileExit.setText(Options.getInstance().getResource("exit.text"));
    	    menuFileExit.addActionListener(new MainFrame_menuFileExit_ActionAdapter(this));
    	
    	 menuFile.add(menuFileExit);

        JMenu menuHelp = new JMenu();
	    	menuHelp.setMnemonic(Options.getInstance().getResource("help.mnemonic").charAt(0));
	    	menuHelp.setText(Options.getInstance().getResource("help.text"));
	    	menuHelpAbout.setMnemonic(Options.getInstance().getResource("about.mnemonic").charAt(0));
	    	menuHelpAbout.setText(Options.getInstance().getResource("about.text"));
	    	menuHelpAbout.addActionListener(new MainFrame_menuHelpAbout_ActionAdapter(this));
	    
	    menuHelp.add(menuHelpAbout);
	    	
        JMenuBar bar = new JMenuBar();
        
        bar.add(menuFile);
        bar.add(menuHelp);

        return bar;
    }
    

	/**
	 * Menu file new conn_action performed.
	 * 
	 * @param e the e
	 */
	private void menuFileNewConn_actionPerformed(ActionEvent e) {
		final ConnectionFrame f = new ConnectionFrame(this);
		Dimension mdiSize = this.getSize();
		Dimension frameSize = f.getSize();
		if (frameSize.height > mdiSize.height) {
			frameSize.height = mdiSize.height;
		}
		if (frameSize.width > mdiSize.width) {
			frameSize.width = mdiSize.width;
		}
		f.setLocation((mdiSize.width - frameSize.width) / 2,
				(mdiSize.height - frameSize.height) / 2);
		f.setVisible(true);

		new Thread() {
			@Override
			public void run() {
				try {
					sleep(100);
				} catch (InterruptedException ex1) {
				}
				f.toFront();
				f.setVisible(true);
				f.requestFocus();
			}
		}.start();
	}
	
	/**
	 * Menu file exit_action performed.
	 * 
	 * @param e the e
	 */
	public void menuFileExit_actionPerformed(ActionEvent e) {
		if (JOptionPane.showConfirmDialog(this, Options.getInstance()
				.getResource("quit application?"), Options.getInstance()
				.getResource("attention"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == 0) {
			if (this.collectorUI != null) this.collectorUI.stop();
			if (this.database != null)  this.database.close();
			System.exit(0);
		}
	}

   /**
  	 * Menu help about_action performed.
  	 * 
  	 * @param e the e
  	 */
  	public void menuHelpAbout_actionPerformed(ActionEvent e) {
	    AboutBox dlg = new AboutBox(this);
	    Dimension dlgSize = dlg.getPreferredSize();
	    Dimension frmSize = getSize();
	    Point loc = getLocation();
	    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
	    dlg.setModal(true);
	    dlg.show();
	  }
    
    /**
     * The Class MainFrame_menuFileExit_ActionAdapter.
     */
    class MainFrame_menuFileExit_ActionAdapter implements ActionListener {
      
      /** The adaptee. */
      MainFrame adaptee;

      /**
       * Instantiates a new main frame_menu file exit_ action adapter.
       * 
       * @param adaptee the adaptee
       */
      MainFrame_menuFileExit_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
      }
      
      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e) {
        adaptee.menuFileExit_actionPerformed(e);
      }
    }


    /**
     * The Class MainFrame_menuHelpAbout_ActionAdapter.
     */
    class MainFrame_menuHelpAbout_ActionAdapter implements ActionListener {
      
      /** The adaptee. */
      MainFrame adaptee;

      /**
       * Instantiates a new main frame_menu help about_ action adapter.
       * 
       * @param adaptee the adaptee
       */
      MainFrame_menuHelpAbout_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
      }
      
      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e) {
        adaptee.menuHelpAbout_actionPerformed(e);
      }
    }
}
