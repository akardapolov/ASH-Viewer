/*
 *-------------------
 * The Gantt.java is part of ASH Viewer
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ash.conn.model.Model;
import org.ash.database.ASHDatabase;
import org.ash.database.Database10g2;
import org.ash.database.Database11g1;
import org.ash.database.Database11g2;
import org.ash.util.Options;
import org.ash.util.ProgressBarUtil;
import org.ash.util.Utils;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.PLSQLTokenMarker;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.ComponentResources;
import com.egantt.swing.component.context.BasicComponentContext;
import com.egantt.swing.component.tooltip.ToolTipState;
import com.egantt.swing.table.list.BasicJTableList;

import ext.egantt.drawing.module.BasicPainterModule;
import ext.egantt.drawing.module.GradientColorModule;
import ext.egantt.drawing.painter.context.BasicPainterContext;
import ext.egantt.swing.GanttDrawingPartHelper;
import ext.egantt.swing.GanttTable;

/**
 * The Class SqlsAndSessionsGantt.
 */
public class Gantt extends JPanel {

	/** The main. */
	private JPanel main;

	/** The root. */
	private JFrame root;

	/** The model. */
	private Model model;

	/** The database. */
	private ASHDatabase database;

	/** The sqls. */
	private JPanel sqls;

	/** The scale. */
	private double scale = 0.8;

	/** The prev percent. */
	private long percentPrev = 0;

	/** The scale toggle: 
	 * < 30  => 2 
	 * 30-70  => 1
	 * > 70 	=> 0 
	 * */
	private int scaleToggle = 0;

	/** The other0. */
	private String other0 = "0";

	/** The application1. */
	private String application1 = "1";

	/** The configuration2. */
	private String configuration2 = "2";

	/** The administrative3. */
	private String administrative3 = "3";

	/** The concurrency4. */
	private String concurrency4 = "4";

	/** The commit5. */
	private String commit5 = "5";

	/** The network7. */
	private String network7 = "7";

	/** The user i o8. */
	private String userIO8 = "8";

	/** The system i o9. */
	private String systemIO9 = "9";

	/** The scheduler10. */
	private String scheduler10 = "10";

	/** The cluster11. */
	private String cluster11 = "11";

	/** The queueing12. */
	private String queueing12 = "12";

	/** The cpu. */
	private String cpu = "13";

	/** The SUM var. */
	private String SUM = "SUM";

	/** The COUNT var. */
	private String COUNT = "COUNT";

	/** The UNKNOWN var. */
	private String UNKNOWN = "UNKNOWN";

	/** The TEXT_PAINTER. */
	final String TEXT_PAINTER = "MyTextPainter";

	/** The detail for top sql, default 10, max - 50, minimum - 0 values*/
	private int topSqlsSqlText = 10;

	/** SQL text */
	private JEditTextArea jtextAreaSqlText = new JEditTextArea();
	
	/** Is select sql plan*/
	private boolean isSelectSqlPlan = false;

	/**
	 * Constructor Gantt JPanel
	 * 
	 * @param rootFrame0 the root frame0
	 * @param model0 the model0
	 * @param database0 the database0
	 */
	public Gantt(JFrame rootFrame0, ASHDatabase database0) {
		super();
		setLayout(new GridLayout(1, 1, 3, 3));

		this.database = database0;
		this.root = rootFrame0;

		this.main = new JPanel();
		this.main.setLayout(new GridLayout(1, 1, 3, 3));

		this.jtextAreaSqlText.setTokenMarker(new PLSQLTokenMarker());
		this.jtextAreaSqlText.setEditable(false);

		this.add(this.main);
	}

	/**
	 * Load data to jpanels.
	 * 
	 * @param beginTime the begin time
	 * @param endTime the end time
	 */
	public synchronized void loadDataToJPanels(final double beginTime,
			final double endTime) {

		this.main.removeAll();
		JPanel panel = createProgressBar("Loading, please wait...");
		this.main.add(panel);

		Thread t = new Thread() {
			@Override
			public void run() {
				// delay
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadDataToJPanelsPrivate(beginTime, endTime);
			}
		};
		t.start();
	}

	/**
	 * Load data to jpanels.
	 * 
	 * @param beginTime the begin time
	 * @param endTime the end time
	 */
	private void loadDataToJPanelsPrivate(double beginTime, double endTime) {

		JSplitPane splitPane = new GanttSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		this.database.getSqlsTemp().clear();
		this.database.getSessionsTemp().clear();
		this.database.calculateSqlsSessionsData(beginTime, endTime, "All");

		// Load data to JTable model
		try {
			String sqlIdHash = "";
			if (Options.getInstance().getVersionDb().equalsIgnoreCase("9i")) {
				sqlIdHash = "Hash Value";
			} else {
				sqlIdHash = "SQL ID";
			}

			String[][] columnNamesSqls = { { "Activity %", sqlIdHash,
					"SQL Type" } };
			String[][] columnNamesSessions = { { "Activity %", "Session ID",
					"User Name", "Program" } };

			/** Array NofRow-sqlId for SQL Text tab*/
			Map<Integer, String> arraySqlIdTSQLTextTab = new HashMap<Integer, String>();

			/** Create gantt table */
			final GanttTable tableGanttSql = new GanttTable(
					loadDataToSqlsGantt(arraySqlIdTSQLTextTab), columnNamesSqls,
					getBasicJTableList());
			final GanttTable tableGanttSessions = new GanttTable(
					loadDataToSessionsGantt(), columnNamesSessions,
					getBasicJTableList());

			/** Set tooltip and percent*/
			setTooltipAndPercent(tableGanttSql);
			setTooltipAndPercent(tableGanttSessions);

			/** Left tabbed pane (Top SQL + SQL text)*/
			JTabbedPane tabsTopSQLText = new JTabbedPane();

			/** Top SQL pane*/
			JScrollPane leftPane = new JScrollPane(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			leftPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

			SqlPlan sqlPlan = new SqlPlan(root, database);

			/** Top sessions pane*/
			JScrollPane rightPane = new JScrollPane(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			rightPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

			leftPane.setViewportView(tableGanttSql.getJTable());
			rightPane.setViewportView(tableGanttSessions.getJTable());
			leftPane.setVerticalScrollBar(leftPane.getVerticalScrollBar());
			rightPane.setVerticalScrollBar(rightPane.getVerticalScrollBar());

			/** Add component to left tabs*/
			tabsTopSQLText.add(leftPane, Options.getInstance().getResource(
					"tabTopSQL.text"));
			tabsTopSQLText.add(jtextAreaSqlText, Options.getInstance()
					.getResource("tabSQLText.text"));
			tabsTopSQLText.add(sqlPlan, Options.getInstance().getResource(
					"tabSQLPlan.text"));
			tabsTopSQLText.setEnabledAt(1, false);
			tabsTopSQLText.setEnabledAt(2, false);

			/** Add selection listener for table model*/
			SelectionListener listener = new SelectionListener(tableGanttSql
					.getJTable(), tabsTopSQLText, jtextAreaSqlText,
					arraySqlIdTSQLTextTab, sqlPlan, database);
			tableGanttSql.getJTable().getSelectionModel()
					.addListSelectionListener(listener);

			/** Layout components*/
			splitPane.setLeftComponent(tabsTopSQLText);
			splitPane.setRightComponent(rightPane);
			splitPane.setDividerLocation(this.getWidth() / 2);
			splitPane.setOneTouchExpandable(true);

			this.main.removeAll();
			
			if (database instanceof Database10g2 ||
					database instanceof Database11g1 ||
						database instanceof Database11g2) {
				
				JTabbedPane tabPane = new JTabbedPane();
				tabPane.add("Top sql & sessions",splitPane);
				tabPane.add("ASH Report",new ASHReport(root,database,beginTime,endTime));
				
				this.main.add(tabPane);
				this.validate();
				
			} else {
				this.main.add(splitPane);
				this.validate();
			}

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
		SqlPlan sqlPlan;
		ASHDatabase database;
		
		SelectionListener(JTable table, JTabbedPane tabbedpane,
				JEditTextArea sqlTextArea,
				Map<Integer, String> arraySqlIdText50SQLTextTab,
				SqlPlan sqlPlan, ASHDatabase database) {
			this.table = table;
			this.tabbedpane = tabbedpane;
			this.sqlTextArea = sqlTextArea;
			this.arraySqlIdText50SQLTextTab = arraySqlIdText50SQLTextTab;
			this.sqlPlan = sqlPlan;
			this.database = database;
		}

		/*
		 * (non-Javadoc)
		 * 
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
			final String sqlId = arraySqlIdText50SQLTextTab.get(table
					.getSelectedRow());
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
					sqlPlan.loadSqlPlan(sqlId, false);
				} catch (Exception e1) {
					System.out.println("SQL Exception occured: "
							+ e1.getMessage());
				}
			} else {
				tabbedpane.setEnabledAt(2, false);
			}
			
			tabbedpane.setComponentAt(1, sqlTextArea);
			tabbedpane.setComponentAt(2, sqlPlan);			
		}
	}

	/**
	 * Get BasicJTableList
	 * 
	 * @return BasicJTableList
	 */
	private BasicJTableList getBasicJTableList() {

		BasicJTableList tableListSqls = new BasicJTableList();
		{
			BasicComponentContext componentContext = new BasicComponentContext();

			ToolTipState state = new ToolTipState() {
				public String getToolTipText(MouseEvent event,
						CellState cellState) {

					DrawingState drawing = cellState.getDrawing();
					Object key = drawing != null ? drawing.getValueAt(event
							.getPoint()) : null;
					if (key == null)
						return "";
					return key.toString();
				}
			};
			componentContext.put(ComponentResources.TOOLTIP_STATE, state);
			tableListSqls.setRendererComponentContext(componentContext);
		}
		return tableListSqls;
	}

	/**
	 * Set tooltip and percent for GanttTable
	 * 
	 * @param gantttable
	 */
	private void setTooltipAndPercent(GanttTable gantttable) {

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
	 * Load data to sqls gantt.
	 * 
	 * @return the object[][]
	 */
	private Object[][] loadDataToSqlsGantt(Map<Integer, String> arraySqlIdTSQLTextTab) {

		int i = 0;
		int ii = 0;
		int sizeGanttTable = 100;
		int sizeMainSqls = database.getSqlsTemp().getMainSqls().size();
		
		StringBuilder clipBoardContent = new StringBuilder();
		Object[][] data = new Object[Math.min(sizeGanttTable, sizeMainSqls)][3];

		final GanttDrawingPartHelper partHelper = new GanttDrawingPartHelper();

		double countOfSqls = database.getSqlsTemp().getCountSql();// get_sum();
		double sumOfRange = database.getSqlsTemp().get_sum();

		// Desc sorting
		HashMap<String, HashMap<String, Object>> sortedSessionMap = sortHashMapByValuesCOUNT(database
				.getSqlsTemp().getMainSqls());

		List<String> arraySqlId = new ArrayList<String>();

		for (Entry<String, HashMap<String, Object>> me : sortedSessionMap
				.entrySet()) {
			
			data[i][0] = createDrawingState(partHelper, me, countOfSqls,
					sumOfRange);

			data[i][1] = me.getKey();
			data[i][2] = UNKNOWN;

			/** Load sqlid, sqltype, sqltext from local storage */
			if (ii < this.topSqlsSqlText) {
				arraySqlId.add(me.getKey());
			}
			/** Exit when rows > 500 */
			if (i + 1 == Math.min(sizeGanttTable, sizeMainSqls)) {
				break;
			}
			i++;
			ii++;
		}
				
		/** Load commandType, sqlText from Oracle database*/
		database.loadSqlTextCommandTypeFromDB(arraySqlId);

		/** Load sql plan to local BDB*/
		if (isSelectSqlPlan){
			database.loadSqlPlanFromDB(arraySqlId, false);	
		}
		
		/** Load CommandType, SqlText to gantt*/
		ii = 0;
		for (Entry<String, HashMap<String, Object>> me : sortedSessionMap
				.entrySet()) {
			String sqlId = me.getKey();
			String sqlText = database.getSqlText(sqlId);
			String sqlType = database.getSqlType(sqlId);

			data[ii][1] = createDrawingStateSqlId(partHelper, sqlId, sqlText);
			if (!sqlType.equals(""))
				data[ii][2] = sqlType;

			// Save clipboard content 
			clipBoardContent.append(sqlId + ":::" + sqlText + "\n");

			/** Save arraySqlIdText50 for SQL Text tab*/
			if (!sqlType.equalsIgnoreCase("UNKNOWN")) {
				arraySqlIdTSQLTextTab.put(ii, sqlId);
			}
			
			/** Exit when rows > 500 */
			if (ii + 1 == Math.min(sizeGanttTable, sizeMainSqls)) {
				break;
			}
			
			ii++;
		}

		/** Set clipboard content */
		Utils.setClipBoardContent(clipBoardContent.toString());

		percentPrev = 0;

		return data;
	}

	/**
	 * Load data to sessions gantt.
	 * 
	 * @return the object[][]
	 */
	private Object[][] loadDataToSessionsGantt() {
		String USERNAME = "USERNAME";
		String PROGRAM = "PROGRAM";
		String SESSIONID = "SESSIONID";

		int i = 0;
		int sizeGanttTable = 100;
		int sizeMainSqls = database.getSessionsTemp().getMainSessions().size();
		Object[][] data = new Object[Math.min(sizeGanttTable, sizeMainSqls)][4];

		final GanttDrawingPartHelper partHelper = new GanttDrawingPartHelper();

		double countOfSqls = database.getSessionsTemp().getCountSql();// get_sum();
		double sumOfRange = database.getSessionsTemp().get_sum();

		// Desc sorting
		HashMap<String, HashMap<String, Object>> sortedSessionMap = sortHashMapByValuesCOUNT(database
				.getSessionsTemp().getMainSessions());

		for (Entry<String, HashMap<String, Object>> me : sortedSessionMap
				.entrySet()) {

			data[i][0] = createDrawingState(partHelper, me, countOfSqls,
					sumOfRange);
			data[i][1] = me.getValue().get(SESSIONID);
			data[i][2] = me.getValue().get(USERNAME);
			data[i][3] = me.getValue().get(PROGRAM);

			/** Exit when rows > 500 */
			if (i + 1 == Math.min(sizeGanttTable, sizeMainSqls)) {
				break;
			}
			i++;
		}

		percentPrev = 0;

		return data;
	}

	/**
	 * Creates the drawing state for Sqls and Sessions.
	 * 
	 * @param obj the obj
	 * @param helper the helper
	 * @param me the me
	 * @param countOfSqls the sum of range
	 * 
	 * @return the drawing state
	 */
	private DrawingState createDrawingState(GanttDrawingPartHelper helper,
			Entry<String, HashMap<String, Object>> me, double countOfSqls,
			double sumOfRange) {

		BasicDrawingState state = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		ListDrawingPart textLayer = helper.createDrawingPart(true);

		double countPerSqlID = (Double) me.getValue().get(COUNT);
		double percent = round(countPerSqlID / countOfSqls * 100, 2);

		/* < 30  => 2 
		 * 30-70  => 1
		 * > 70    => 0 */
		if (percentPrev == 0) {
			if (percent > 70) {
				scaleToggle = 0;
			} else if (percent < 70 && percent > 30) {
				scaleToggle = 1;
			} else if (percent < 30) {
				scaleToggle = 2;
			}
		}

		if (percent < 0.6) {
			// Show only percent
			String localizedText = "" + percent + "%";
			helper.createActivityEntry(new StringBuffer(localizedText),
					new Date(0), new Date(100),
					BasicPainterModule.BASIC_STRING_PAINTER, TEXT_PAINTER,
					textLayer);

			state.addDrawingPart(part);
			state.addDrawingPart(textLayer);
			return state;
		}

		// Save to local map, sort by values 
		HashMap tempKeyMap = new HashMap<String, Double>();
		tempKeyMap.put(other0, me.getValue().get(other0));
		tempKeyMap.put(application1, me.getValue().get(application1));
		tempKeyMap.put(configuration2, me.getValue().get(
				configuration2));
		tempKeyMap.put(administrative3, me.getValue().get(
				administrative3));
		tempKeyMap.put(concurrency4, me.getValue().get(concurrency4));
		tempKeyMap.put(commit5, me.getValue().get(commit5));
		tempKeyMap.put(network7, me.getValue().get(network7));
		tempKeyMap.put(userIO8, me.getValue().get(userIO8));
		tempKeyMap.put(systemIO9, me.getValue().get(systemIO9));
		tempKeyMap.put(scheduler10, me.getValue().get(scheduler10));
		tempKeyMap.put(cluster11, me.getValue().get(cluster11));
		tempKeyMap.put(queueing12, me.getValue().get(queueing12));
		tempKeyMap.put(cpu, me.getValue().get(cpu));

		HashMap sortedByKeyMap = new HashMap<String, Double>();

		//Sort values on desc order
		sortedByKeyMap = sortHashMapByValuesD(tempKeyMap);

		// Calculate sum of event by SqlId
		Set keySetsorted = sortedByKeyMap.keySet();

		long start = 0;
		scale = Utils.getScale(scaleToggle, percent);

		// Create activites for row on gantt
		Set keySetsorted1 = sortedByKeyMap.keySet();
		Iterator jj = keySetsorted.iterator();
		while (jj.hasNext()) {

			String key = (String) jj.next();
			double value = (Double) sortedByKeyMap.get(key);

			// Show only not zero activites.
			if (value != 0) {

				double currentGroupPercentNotScale = (value / countPerSqlID)
						* percent;
				double currentGroupPercent = currentGroupPercentNotScale
						* scale;

				if (currentGroupPercent < 1.0 && currentGroupPercent >= 0.6) {
					currentGroupPercent = round(currentGroupPercent, 0);
				}

				long currGroupPercentL = (long) round(currentGroupPercent, 0);

				// Set tooltip
				final StringBuffer o = new StringBuffer();
				{
					o.append("<HTML>");
					o.append("<b>" + getToolTipClassEvent(key) + " "
							+ round(currentGroupPercentNotScale, 2) + "%"
							+ "</b>");
					o.append("</HTML>");
				}

				// Exit when prev. egantt < than current egantt graph
				if (percentPrev != 0
						&& (start + currGroupPercentL) > percentPrev) {
					currGroupPercentL = percentPrev - start;
					helper.createActivityEntry(o, new Date(start), new Date(
							start + currGroupPercentL),
							getGradientContext(key), part);
					start = start + currGroupPercentL;
					break;
				}

				// If row only one
				if (currentGroupPercent == 100) {
					helper.createActivityEntry(o, new Date(start), new Date(
							currGroupPercentL), getGradientContext(key), part);
				} else {
					helper.createActivityEntry(o, new Date(start), new Date(
							start + currGroupPercentL),
							getGradientContext(key), part);
					start = start + currGroupPercentL;
				}
			}
		}

		// Show percent
		String localizedText = "" + percent + "%";
		helper.createActivityEntry(new StringBuffer(localizedText), new Date(
				start), new Date(100), BasicPainterModule.BASIC_STRING_PAINTER,
				TEXT_PAINTER, textLayer);

		state.addDrawingPart(part);
		state.addDrawingPart(textLayer);

		percentPrev = start;

		return state;
	}

	/**
	 * Creates the drawing state for SqlId.
	 * 
	 * @param helper  the helper
	 * @param me  the me
	 * @param arraySqlIdText50
	 * @return the drawing state
	 */
	private DrawingState createDrawingStateSqlId(GanttDrawingPartHelper helper,
			String key,
			String value) {

		BasicDrawingState state = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		ListDrawingPart textLayer = helper.createDrawingPart(true);

		//String key = "";
		//String value = "";

		// Set tooltip (sql_text)
		final StringBuffer o = new StringBuffer();

		if (value.equals("")) {
			value = "No data";
		}
		o.append(Utils.formatSqlQueryShort(value));

		// Show sqlid
		helper.createActivityEntry(new StringBuffer(key), new Date(5),
				new Date(95), BasicPainterModule.BASIC_STRING_PAINTER,
				TEXT_PAINTER, textLayer);

		helper.createActivityEntry(o, new Date(0), new Date(100),
				BasicPainterModule.BASIC_STRING_PAINTER, part);

		state.addDrawingPart(part);
		state.addDrawingPart(textLayer);

		return state;

	}

	/**
	 * Sort hash map by values d.
	 * 
	 * @param passedMap the passed map
	 * 
	 * @return the linked hash map
	 */
	public LinkedHashMap sortHashMapByValuesD(HashMap<String, Double> passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put(key, val);
					break;
				}

			}

		}
		return sortedMap;
	}

	/**
	 * Sort hash map by values sumd.
	 * 
	 * @param passedMap the passed map
	 * 
	 * @return the linked hash map< string, hash map< string, object>>
	 */
	public LinkedHashMap<String, HashMap<String, Object>> sortHashMapByValuesSUMD(
			HashMap<String, HashMap<String, Object>> passedMap) {

		//bdb.getSqlsTemp().getMainSqls()

		List mapKeys = new ArrayList();
		List mapValues = new ArrayList();

		for (Entry<String, HashMap<String, Object>> me : passedMap.entrySet()) {
			mapKeys.add(me.getKey());
			mapValues.add(me.getValue().get(SUM));
		}

		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);

		LinkedHashMap<String, HashMap<String, Object>> sortedMap = new LinkedHashMap<String, HashMap<String, Object>>();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).get(SUM).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					sortedMap.put((String) key, passedMap.get(key));
					passedMap.remove(key);
					mapKeys.remove(key);
					break;
				}

			}

		}
		return sortedMap;
	}

	/**
	 * Sort hash map by values sumd.
	 * 
	 * @param passedMap the passed map
	 * 
	 * @return the linked hash map< string, hash map< string, object>>
	 */
	public LinkedHashMap<String, HashMap<String, Object>> sortHashMapByValuesCOUNT(
			HashMap<String, HashMap<String, Object>> passedMap) {

		//bdb.getSqlsTemp().getMainSqls()

		List mapKeys = new ArrayList();
		List mapValues = new ArrayList();

		for (Entry<String, HashMap<String, Object>> me : passedMap.entrySet()) {
			mapKeys.add(me.getKey());
			mapValues.add(me.getValue().get(COUNT));
		}

		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);

		LinkedHashMap<String, HashMap<String, Object>> sortedMap = new LinkedHashMap<String, HashMap<String, Object>>();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).get(COUNT).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					sortedMap.put((String) key, passedMap.get(key));
					passedMap.remove(key);
					mapKeys.remove(key);
					break;
				}

			}

		}
		return sortedMap;
	}

	/**
	 * Round.
	 * 
	 * @param d the d
	 * @param decimalPlace the decimal place
	 * 
	 * @return the double
	 */
	public double round(double d, int decimalPlace) {
		BigDecimal bd;
		try {
			bd = new BigDecimal(Double.toString(d));
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.doubleValue();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return 0.0;
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
	 * Gets the gradient context.
	 * 
	 * @param value the value
	 * 
	 * @return the gradient context
	 */
	private String getGradientContext(String value) {
		String context = "";
		if (value == other0) {
			context = GradientColorModule.OTHER0_GRADIENT_CONTEXT;
		} else if (value == application1) {
			context = GradientColorModule.APPLICATION1_GRADIENT_CONTEXT;
		} else if (value == configuration2) {
			context = GradientColorModule.CONFIGURATION2_GRADIENT_CONTEXT;
		} else if (value == administrative3) {
			context = GradientColorModule.ADMINISTRATIVE3_GRADIENT_CONTEXT;
		} else if (value == concurrency4) {
			context = GradientColorModule.CONCURRENCY4_GRADIENT_CONTEXT;
		} else if (value == commit5) {
			context = GradientColorModule.COMMIT5_GRADIENT_CONTEXT;
		} else if (value == network7) {
			context = GradientColorModule.NETWORK7_GRADIENT_CONTEXT;
		} else if (value == userIO8) {
			context = GradientColorModule.USERIO8_GRADIENT_CONTEXT;
		} else if (value == systemIO9) {
			context = GradientColorModule.SYSTEMIO9_GRADIENT_CONTEXT;
		} else if (value == scheduler10) {
			context = GradientColorModule.SCHEDULER10_GRADIENT_CONTEXT;
		} else if (value == cluster11) {
			context = GradientColorModule.CLUSTER11_GRADIENT_CONTEXT;
		} else if (value == queueing12) {
			context = GradientColorModule.QUEUEING12_GRADIENT_CONTEXT;
		} else if (value == cpu) {
			context = GradientColorModule.CPU_GRADIENT_CONTEXT;
		}

		return context;
	}

	/**
	 * Gets the tooltip of class event.
	 * 
	 * @param value the value
	 * 
	 * @return the tooltip
	 */
	private String getToolTipClassEvent(String value) {
		String context = "";
		if (value == other0) {
			context = "Other";
		} else if (value == application1) {
			context = "Application";
		} else if (value == configuration2) {
			context = "Configuration";
		} else if (value == administrative3) {
			context = "Administrative";
		} else if (value == concurrency4) {
			context = "Concurrency";
		} else if (value == commit5) {
			context = "Commit";
		} else if (value == network7) {
			context = "Network";
		} else if (value == userIO8) {
			context = "User IO";
		} else if (value == systemIO9) {
			context = "System IO";
		} else if (value == scheduler10) {
			context = "Scheduler";
		} else if (value == cluster11) {
			context = "Cluster";
		} else if (value == queueing12) {
			context = "Queueing";
		} else if (value == cpu) {
			context = "CPU used";
		}

		return context;
	}

	/**
	 * @return the topSqlsSqlText
	 */
	public int getTopSqlsSqlText() {
		return topSqlsSqlText;
	}

	/**
	 * @param topSqlsSqlText the topSqlsSqlText to set
	 */
	public void setTopSqlsSqlText(int topSqlsSqlText) {
		this.topSqlsSqlText = topSqlsSqlText;
	}

	/**
	 * @return the isSelectSqlPlan
	 */
	public boolean isSelectSqlPlan() {
		return isSelectSqlPlan;
	}

	/**
	 * @param isSelectSqlPlan the isSelectSqlPlan to set
	 */
	public void setSelectSqlPlan(boolean isSelectSqlPlan) {
		this.isSelectSqlPlan = isSelectSqlPlan;
	}

}
