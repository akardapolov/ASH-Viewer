/*
 *-------------------
 * The GanttDetailsH.java is part of ASH Viewer
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ash.history.detail.GanttSessions;
import org.ash.history.detail.GanttSqls;
import org.ash.database.ASHDatabase;
import org.ash.gui.GanttSplitPane;
import org.ash.gui.SqlPlan;
import org.ash.history.ASHDatabaseH;
import org.ash.history.SqlPlanH;
import org.ash.util.Options;
import org.ash.util.ProgressBarUtil;
import org.ash.util.Utils;
import org.syntax.jedit.JEditTextArea;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingState;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.ComponentResources;
import com.egantt.swing.component.context.BasicComponentContext;
import com.egantt.swing.component.tooltip.ToolTipState;
import com.egantt.swing.table.list.BasicJTableList;

import ext.egantt.drawing.painter.context.BasicPainterContext;
import ext.egantt.swing.GanttTable;

/**
 * The Class SqlsAndSessionsGantt.
 */
public class GanttDetailsH extends JPanel{

	/** The main. */
	private JPanel main;
	
	/** The database. */
	private ASHDatabaseH database;
		
	/** The TEXT_PAINTER. */
	final String TEXT_PAINTER = "MyTextPainter";
		
	/** Gantt graph data for Sqls */
	private GanttSqls ganttSqls;
	
	/** Gantt graph data for Sessions */
	private GanttSessions ganttSessions;
	
	/** The waitClass or CPU used */
	private String waitClass = "none";
	
	/** SQL text */
	private JEditTextArea jtextAreaSqlText = Options.getInstance().getJtextAreaSqlTextGanttDetailsH();
	
	/**
	 * Constructor Gantt JPanel
	 * 
	 * @param rootFrame0 the root frame0
	 * @param model0 the model0
	 * @param database0 the database0
	 */
	public GanttDetailsH(ASHDatabaseH database0, String waitClass0){
		super();
		setLayout(new GridLayout(1, 1, 3, 3));	
		
		this.database = database0;
		this.waitClass = waitClass0;

		// Initialize sqls and sessions
		this.ganttSqls = new GanttSqls(database0);
		this.ganttSessions = new GanttSessions(database0);
		
		this.main = new JPanel();
		this.main.setLayout(new GridLayout(1, 1, 3, 3));	
		
		this.add(this.main);
	}
	
	/**
	 * Load data to JPanels.
	 * 
	 * @param beginTime the begin time
	 * @param endTime the end time
	 */
	public synchronized void loadDataToJPanels(final double beginTime, final double endTime){
		
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
                     catch(InterruptedException e)
                     {
                         e.printStackTrace();
                     }
            	 loadDataToJPanelsPrivate(beginTime, endTime);
            }
        };
        t.start();
	}
	

	/**
	 * Get topSqlsSqlText
	 * 
	 * @return the topSqlsSqlText
	 */
	public int getTopSqlsSqlText() {
		return ganttSqls.getTopSqlsSqlText();
	}

	/**
	 * Set topSqlsSqlText
	 * 
	 * @param topSqlsSqlText the topSqlsSqlText to set
	 */
	public void setTopSqlsSqlText(int topSqlsSqlText) {
		ganttSqls.setTopSqlsSqlText(topSqlsSqlText);
	}

	/**
	 * Load data to panels.
	 * 
	 * @param beginTime the begin time
	 * @param endTime the end time
	 */
	private void loadDataToJPanelsPrivate(double beginTime, double endTime){
		
		JSplitPane splitPane = new GanttSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		this.database.getSqlsTempDetail().clear();
		this.database.getSessionsTempDetail().clear();
		
		this.database.calculateSqlsSessionsData(beginTime, endTime, this.waitClass);
		
		List eventListSqls = this.database.getSqlsTempDetail().getEventList();
		List eventListSessions = this.database.getSessionsTempDetail().getEventList();
		
		// Load data to JTable model
		try {
			String sqlIdHash = "";
			if (this.database.getParameter("ASH.version").equalsIgnoreCase("9i")
					||this.database.getParameter("ASH.version").equalsIgnoreCase("8i")){
				sqlIdHash = "Hash Value";
			} else {
				sqlIdHash = "SQL ID";
			}
			
			String[][] columnNamesSqls = {
		    		{"Activity %", sqlIdHash, "SQL Type"}};
			String[][] columnNamesSessions = {
		    		{"Activity %", "Session ID", "User Name", "Program"}};
			
			/** Array SqlIdText for SQL Text tab*/
			Map<Integer,String> arraySqlIdText50SQLTextTab = new HashMap<Integer, String>();
			
			/** Create gantt table */
			final GanttTable tableGanttSql = new GanttTable(
					ganttSqls.getDataToSqlsGantt(arraySqlIdText50SQLTextTab), columnNamesSqls, getBasicJTableList(),eventListSqls);
			final GanttTable tableGanttSessions = new GanttTable(
					ganttSessions.getDataToSessionsGantt(), columnNamesSessions, getBasicJTableList(),eventListSessions);   
						
			/** Set tooltip and percent*/
			setTooltipAndPercent(tableGanttSql);
			setTooltipAndPercent(tableGanttSessions);
			
			/** Left tabbed pane (Top SQL + SQL text)*/
			JTabbedPane tabsTopSQLText = new JTabbedPane();
			
			SqlPlanH sqlPlan = new SqlPlanH(database);
			
			/** Top SQL pane*/
			JScrollPane leftPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			leftPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
						
			jtextAreaSqlText.setMinimumSize(new Dimension(this.main.getWidth()/2,this.main.getHeight()-50));
			jtextAreaSqlText.setPreferredSize(new Dimension(this.main.getWidth()/2,this.main.getHeight()-50));
			
			/** Top sessions pane*/
			JScrollPane rightPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			rightPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
						
			leftPane.setViewportView(tableGanttSql.getJTable());
			rightPane.setViewportView(tableGanttSessions.getJTable());
			leftPane.setVerticalScrollBar(leftPane.getVerticalScrollBar());
			rightPane.setVerticalScrollBar(rightPane.getVerticalScrollBar());	
			
			/** Add component to left tabs*/
			tabsTopSQLText.add(leftPane,Options.getInstance().getResource("tabTopSQL.text"));
			tabsTopSQLText.add(jtextAreaSqlText,Options.getInstance().getResource("tabSQLText.text"));
			tabsTopSQLText.add(sqlPlan, Options.getInstance().getResource("tabSQLPlan.text"));
			tabsTopSQLText.setEnabledAt(1, false);
			tabsTopSQLText.setEnabledAt(2, false);
			
			/** Add selection listener for table model*/
			SelectionListener listener = 
				new SelectionListener(
						tableGanttSql.getJTable(), tabsTopSQLText,
						jtextAreaSqlText, arraySqlIdText50SQLTextTab, sqlPlan, database);
			tableGanttSql.getJTable().getSelectionModel().addListSelectionListener(listener);
			
			/** Layout components*/
			splitPane.setLeftComponent(tabsTopSQLText);
			splitPane.setRightComponent(rightPane);
			splitPane.setDividerLocation(this.getWidth()/2);
			splitPane.setOneTouchExpandable(true);
			
			this.main.removeAll();
			this.main.add(splitPane);
	        this.validate();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 *  Selection listener for Top SQL JTable
	 */
	private class SelectionListener implements ListSelectionListener {
		JTable table;
		JTabbedPane tabbedpane;
		JEditTextArea sqlTextArea;
		Map<Integer, String> arraySqlIdText50SQLTextTab;
		SqlPlanH sqlPlan;
		ASHDatabaseH database;
		
		SelectionListener(JTable table, JTabbedPane tabbedpane,
				JEditTextArea sqlTextArea,
				Map<Integer, String> arraySqlIdText50SQLTextTab,
				SqlPlanH sqlPlan, ASHDatabaseH database) {
			this.table = table;
			this.tabbedpane = tabbedpane;
			this.sqlTextArea = sqlTextArea;
			this.arraySqlIdText50SQLTextTab = arraySqlIdText50SQLTextTab;
			this.sqlPlan = sqlPlan;
			this.database = database;
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {

			if (e.getValueIsAdjusting()) {

				JPanel panelSqlText = createProgressBar("Loading, please wait...");
				JPanel panelSqlPlan = createProgressBar("Loading, please wait...");
				tabbedpane.setComponentAt(1, panelSqlText);
				tabbedpane.setComponentAt(2, panelSqlPlan);
				
				tabbedpane.setEnabledAt(1, true);
				tabbedpane.setEnabledAt(2, true);
				
				Thread t = new Thread() {
					@Override
					public void run() {
						// delay
						try {
							Thread.sleep(50L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						loadSqlTextAndPlan();
					}
				};
				t.start();
			}
		}
		
		/**
		 * Load sql text and plan to tabs
		 */
		private void loadSqlTextAndPlan(){
			final String sqlId = arraySqlIdText50SQLTextTab.get(table.getSelectedRow());
			final String sqlText = database.getSqlText(sqlId);
			final String sqlType = database.getSqlType(sqlId);

			// Load formatted sql text
			if (sqlText != null && sqlText != "") {
				tabbedpane.setEnabledAt(1, true);

				try {
					sqlTextArea.setText(Utils.formatSqlAll(sqlText)
							.toString());
					sqlTextArea.setCaretPosition(0);
					sqlTextArea.updateUI();
				} catch (Exception e1) {
					sqlTextArea
							.setText("Error in syntax highlighting of sql!");
				}
			} else {
				tabbedpane.setEnabledAt(1, false);
				sqlTextArea.setText("");
			}			

			// Load sql plan's for sql_id
			if (sqlType.equalsIgnoreCase("SELECT")
					|| sqlType.equalsIgnoreCase("INSERT")
					|| sqlType.equalsIgnoreCase("UPDATE")
					|| sqlType.equalsIgnoreCase("DELETE")
					|| sqlType.equalsIgnoreCase("MERGE")
					|| sqlType.equalsIgnoreCase("UNKNOWN")
					|| (sqlType == "" && sqlType != null)) {
				tabbedpane.setEnabledAt(2, true);
				try {
					sqlPlan.loadSqlPlan(sqlId, true);
				} catch (Exception e1) {
					System.out.println("SQL Exception occured: "
							+ e1.getMessage()+e1.getStackTrace());
					e1.printStackTrace();
				}
			} else {
				tabbedpane.setEnabledAt(2, false);
			}
			
			tabbedpane.setComponentAt(1, sqlTextArea);
			tabbedpane.setComponentAt(2, sqlPlan);			
		}
	}
	
	/**
	 *  Selection listener for Top SQL JTable
	 */
	private class SelectionListener1 implements ListSelectionListener {
	        JTable table;
	        JTabbedPane tabbedpane;
	        JEditTextArea sqlTextArea;
	        Map<Integer,String> arraySqlIdText50SQLTextTab;
	    
	        SelectionListener1(JTable table,JTabbedPane tabbedpane, 
	        		JEditTextArea sqlTextArea,Map<Integer,String> arraySqlIdText50SQLTextTab) {
	            this.table = table;
	            this.tabbedpane = tabbedpane;
	            this.sqlTextArea = sqlTextArea;
	            this.arraySqlIdText50SQLTextTab = arraySqlIdText50SQLTextTab;
	        }

			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			public void valueChanged(ListSelectionEvent e) {
				final String sqlText = arraySqlIdText50SQLTextTab.get(table.getSelectedRow());
				if (sqlText != null && sqlText != ""){
					tabbedpane.setEnabledAt(1, true);
					
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								sqlTextArea.setText(
										Utils.formatSqlAll(sqlText).toString());
								sqlTextArea.setCaretPosition(0);
								sqlTextArea.updateUI();
							} catch (Exception e) {
								sqlTextArea.setText(
										"Error in syntax highlighting of sql!");
							}
						}
					});
					
				} else {
					tabbedpane.setEnabledAt(1, false);
					sqlTextArea.setText("");
				}
			}
	  }
	
	
	/**
	 * Get BasicJTableList
	 * 
	 * @return BasicJTableList
	 */
	private BasicJTableList getBasicJTableList(){
		
		BasicJTableList tableListSqls = new BasicJTableList();
		{
			BasicComponentContext componentContext = new BasicComponentContext();
			
			ToolTipState state = new ToolTipState() {
				public 	String getToolTipText(MouseEvent event, CellState cellState) {
					
				DrawingState drawing = cellState.getDrawing();
				Object key = drawing != null ? drawing.getValueAt(event.getPoint()): null;
				if (key == null)
					return "";
				return key.toString();
				}
			};
			componentContext.put(ComponentResources.TOOLTIP_STATE, state);
			tableListSqls.setRendererComponentContext(componentContext);
	}

		return 	tableListSqls;

	}
	
	/**
	 * Set tooltip and percent for GanttTable
	 * 
	 * @param gantttable
	 */
	private void setTooltipAndPercent(GanttTable gantttable){
		
		///Tooltip
		{
			BasicPainterContext graphics = new BasicPainterContext();
			graphics.setPaint(Color.WHITE);
			graphics.put(TEXT_PAINTER, new Font(null, Font.PLAIN, 8));
			gantttable.getDrawingContext().put(TEXT_PAINTER,
					ContextResources.GRAPHICS_CONTEXT, graphics);
		}
		// Percent
		{
        	BasicPainterContext graphics = new BasicPainterContext();
        	graphics.setPaint(Color.BLACK);
        	graphics.put(TEXT_PAINTER, new Font(null, Font.BOLD, 10));
        	gantttable.getDrawingContext().put(TEXT_PAINTER, 
        			ContextResources.GRAPHICS_CONTEXT, graphics);
        }
		
	}
		
	/**
	 * Creates the progress bar.
	 * 
	 * @param msg the msg
	 * 
	 * @return the jpanel
	 */
	private JPanel createProgressBar(String msg)
	    {
	        JProgressBar progress = ProgressBarUtil.createJProgressBar(msg);
	        progress.setPreferredSize(new Dimension(250, 30));
	        JPanel panel = new JPanel();
	        panel.add(progress);
	        return panel;
	    }
	
}
