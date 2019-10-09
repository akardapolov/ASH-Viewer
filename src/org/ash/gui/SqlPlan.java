/*
 *-------------------
 * The SqlPlan.java is part of ASH Viewer
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.ash.database.ASHDatabase;
import org.ash.database.Database10g11gSE;
import org.ash.database.Database8i;
import org.ash.database.Database9i;
import org.ash.explainplanmodel.ExplainPlanModel10g2;
import org.ash.explainplanmodel.ExplainPlanModel9i;
import org.ash.util.ProgressBarUtil;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.TreeTableModel;

public class SqlPlan extends JPanel {

	/** The main. */
	private JPanel main;

	/** The root. */
	private JFrame root;

	/** The database. */
	private ASHDatabase database;

	/**
	 * Constructor.
	 * 
	 * @param rootFrame0
	 * @param database0
	 */
	public SqlPlan(JFrame rootFrame0, ASHDatabase database0) {

		super();
		setLayout(new GridLayout(1, 1, 3, 3));

		this.database = database0;
		this.root = rootFrame0;

		this.main = new JPanel();
		this.main.setLayout(new BorderLayout());

		this.add(this.main);

	}

	/**
	 * Load sql plan from BDB to GUI
	 */
	public void loadSqlPlan(String sqlId, boolean isDetail) {
		// Remove old objects
		this.main.removeAll();

		// Create tabbed pane
		JTabbedPane tabsSQLPlan = new JTabbedPane();
		tabsSQLPlan.setFocusable(false);
		
		// Save tabId - plan hash value
		HashMap<Integer, Double> tabIdPlanHashValue = new HashMap<Integer, Double>();

		// The button panel
		JToolBar buttonPanel;
		buttonPanel = new JToolBar("PanelButton");
		buttonPanel.setFloatable(false);
		buttonPanel.setBorder(new EtchedBorder());
		// The jButtonUpdate button
		JButton jButtonUpdate = new JButton();
		jButtonUpdate.setText("Update");
		jButtonUpdate.setPreferredSize(new Dimension(100, 30));
		jButtonUpdate.setActionCommand("Update");
		// The jButtonPlanCursorCache button
		JButton jButtonPlanCursorCache = new JButton();
		jButtonPlanCursorCache.setText("Cursor Cache");
		jButtonPlanCursorCache.setPreferredSize(new Dimension(100, 30));
		jButtonPlanCursorCache.setActionCommand("getSqlPlanCursorCache");
		// The jButtonPlanAWR button
		JButton jButtonPlanAWR = new JButton();
		jButtonPlanAWR.setText("AWR");
		jButtonPlanAWR.setPreferredSize(new Dimension(100, 30));
		jButtonPlanAWR.setActionCommand("getSqlPlanAWR");
		// SQL ID label
		JLabel sqlIdLabel = new JLabel("SQL ID: " + sqlId, SwingConstants.CENTER);
		sqlIdLabel.setPreferredSize(new Dimension(120, 30));

		// Create action listener
		ButtonPlanActionListener buttonListener = new ButtonPlanActionListener(
				jButtonPlanCursorCache, tabsSQLPlan, database, sqlId,isDetail);
		jButtonPlanCursorCache.addActionListener(buttonListener);
		jButtonPlanAWR.addActionListener(buttonListener);
		jButtonUpdate.addActionListener(buttonListener);

		// Layout of buttons
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(sqlIdLabel);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(jButtonUpdate);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(jButtonPlanCursorCache);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(jButtonPlanAWR);

		// Get list of plan hash value's for sqlId
		if(database instanceof Database10g11gSE){
			jButtonPlanAWR.setEnabled(false);
			addTabs10g11gSE(tabsSQLPlan, tabIdPlanHashValue, sqlId, isDetail);			
		} else if (database instanceof Database9i) {
			jButtonPlanCursorCache.setEnabled(false);
			jButtonPlanAWR.setEnabled(false);
			sqlIdLabel.setText("Hash Value: " + sqlId);
			addTabs9i(tabsSQLPlan, tabIdPlanHashValue, sqlId, isDetail);
		} else if (database instanceof Database8i){
			jButtonPlanCursorCache.setEnabled(false);
			jButtonPlanAWR.setEnabled(false);
			jButtonUpdate.setEnabled(false);
			sqlIdLabel.setText("Hash value: " + sqlId);
		} else {
			addTabs10g11gEE(tabsSQLPlan, tabIdPlanHashValue, sqlId, isDetail);
		}

		// Add buttonPanel, main
		this.main.add(buttonPanel, BorderLayout.NORTH);
		this.main.add(tabsSQLPlan, BorderLayout.CENTER);

	}

	private class ButtonPlanActionListener implements ActionListener {
		JButton jButtonPlan;
		JTabbedPane tabsSQLPlan;
		ASHDatabase database;
		String sqlId;
		boolean isDetail;
		
		String CURSORCACHE = "Cursor Cache";
		String AWR = "AWR";

		public ButtonPlanActionListener(final JButton jButtonPlan,
				final JTabbedPane tabsSQLPlan, final ASHDatabase database,
				final String sqlId,
				final boolean isDetail) {
			super();
			this.jButtonPlan = jButtonPlan;
			this.tabsSQLPlan = tabsSQLPlan;
			this.database = database;
			this.sqlId = sqlId;
			this.isDetail = isDetail;
		}

		public void actionPerformed(final ActionEvent e) {
			/** Get action command */
			final String str = e.getActionCommand();

			if (str.equalsIgnoreCase("Update")) {
				updatePlanTab(str);
			} else if (str.equalsIgnoreCase("getSqlPlanCursorCache")) {
				updatePlanTab(str);
			} else if (str.equalsIgnoreCase("getSqlPlanAWR")) {
				updatePlanTab(str);
			}
		}

		private void updatePlanTab(String param) {
			
			if (param.equalsIgnoreCase("Update")) {

				// Clear tabbedpane
				JPanel panelLoading = createProgressBar("Loading, please wait...");
				tabsSQLPlan.removeAll();
				tabsSQLPlan.add("Loading ...",panelLoading);
				
				Thread t = new Thread() {
					@Override
					public void run() {
						// delay
						try {
							Thread.sleep(50L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// update tabbed pane
						if(database instanceof Database10g11gSE){
							update10g11gSE();
						} else if (database instanceof Database9i){
							update9i();
						} else {
							update10g11gEE();
						}
					}
				};
				t.start();

			} else if (param.equalsIgnoreCase("getSqlPlanCursorCache")) {
								
				boolean isTabCursorCacheExistTmp = false;
				int iTabTmp = 0;
				for (int i = 0; i < tabsSQLPlan.getTabCount(); i++) {
					if (tabsSQLPlan.getTitleAt(i).equalsIgnoreCase(
							CURSORCACHE)) {
						isTabCursorCacheExistTmp = true;
						iTabTmp = i;
					}
				}

				JPanel panelLoading = createProgressBar("Loading, please wait...");
				if (isTabCursorCacheExistTmp) {
					tabsSQLPlan.setComponentAt(iTabTmp, panelLoading);
					tabsSQLPlan.setSelectedIndex(iTabTmp);
				} else {
					tabsSQLPlan.add(CURSORCACHE, panelLoading);
				}

				Thread t = new Thread() {
					@Override
					public void run() {
						// delay
						try {
							Thread.sleep(50L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						boolean isTabCursorCacheExist = false;
						int iTab = 0;
						for (int i = 0; i < tabsSQLPlan.getTabCount(); i++) {
							if (tabsSQLPlan.getTitleAt(i).equalsIgnoreCase(
									CURSORCACHE)) {
								isTabCursorCacheExist = true;
								iTab = i;
							}
						}

						getSqlPlanCursorCache(isTabCursorCacheExist, iTab);
					}
				};
				t.start();
				
			} else if (param.equalsIgnoreCase("getSqlPlanAWR")) {
				
				boolean isTabAWRExistTmp = false;
				int iTabTmp = 0;
				for (int i = 0; i < tabsSQLPlan.getTabCount(); i++) {
					if (tabsSQLPlan.getTitleAt(i).equalsIgnoreCase(
							AWR)) {
						isTabAWRExistTmp = true;
						iTabTmp = i;
					}
				}

				JPanel panelLoading = createProgressBar("Loading, please wait...");
				if (isTabAWRExistTmp) {
					tabsSQLPlan.setComponentAt(iTabTmp, panelLoading);
					tabsSQLPlan.setSelectedIndex(iTabTmp);
				} else {
					tabsSQLPlan.add(AWR, panelLoading);
				}

				Thread t = new Thread() {
					@Override
					public void run() {
						// delay
						try {
							Thread.sleep(50L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						boolean isTabAWRExist = false;
						int iTab = 0;
						for (int i = 0; i < tabsSQLPlan.getTabCount(); i++) {
							if (tabsSQLPlan.getTitleAt(i).equalsIgnoreCase(
									AWR)) {
								isTabAWRExist = true;
								iTab = i;
							}
						}
						
						getSqlPlanAWR(isTabAWRExist, iTab);
					}
				};
				t.start();
				
			}
		}
		
		/**
		 * Update JTabbedPane
		 */
		private void update10g11gEE(){		
			// Reload data from DB
			List<String> arraySqlId = new ArrayList<String>();
			arraySqlId.add(sqlId);				
			database.loadSqlPlanFromDB(arraySqlId, isDetail);				
			database.loadSqlTextSqlIdFromDB(arraySqlId);
			
			// Get list of plan hash value's for sqlId
			List<Double> list = null;
			try {
				if (!isDetail) {
					list = database.getSqlsTemp()
							.getSqlPlanHashValue(sqlId);
				} else {
					list = database.getSqlsTempDetail()
							.getSqlPlanHashValue(sqlId);
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			
			int i = 0;
			Iterator<Double> listIter = list.iterator();
			while (listIter.hasNext()) {
				Double planHashValue = listIter.next();
				String phvTabTitle = "PHV: " + planHashValue.longValue();
				ExplainPlanModel10g2 sqlPlanModel = (ExplainPlanModel10g2) database
						.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
				if (sqlPlanModel != null) {
					JScrollPane scrollPane = new JScrollPane(
							createTreeTable(sqlPlanModel));

					tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				} else {
					JTextArea emptyText = new JTextArea();
					emptyText.setEditable(false);
					emptyText.setBackground(new Color(255, 250, 237));
					emptyText.setText("No data for SQL_ID: " + sqlId + " and "
							+ phvTabTitle);

					tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				}
				
				tabsSQLPlan.requestFocus();
				
				i++;
			}
			
			tabsSQLPlan.removeTabAt(0);
		}
		
		/**
		 * Update JTabbedPane
		 */
		private void update10g11gSE(){		
			// Reload data from DB
			List<String> arraySqlId = new ArrayList<String>();
			arraySqlId.add(sqlId);				
			database.loadSqlPlanFromDB(arraySqlId, isDetail);				
			database.loadSqlTextSqlIdFromDB(arraySqlId);
			
			// Get list of plan hash value's for sqlId
			List<Double> list = database.getSqlPlanHashValueBySqlId(sqlId);
			
			int i = 0;
			Iterator<Double> listIter = list.iterator();
			while (listIter.hasNext()) {
				Double planHashValue = listIter.next();
				String phvTabTitle = "PHV: " + planHashValue.longValue();
				ExplainPlanModel10g2 sqlPlanModel = (ExplainPlanModel10g2) database
						.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
				if (sqlPlanModel != null) {
					JScrollPane scrollPane = new JScrollPane(
							createTreeTable(sqlPlanModel));

					tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				} else {
					JTextArea emptyText = new JTextArea();
					emptyText.setEditable(false);
					emptyText.setBackground(new Color(255, 250, 237));
					emptyText.setText("No data for SQL_ID: " + sqlId + " and "
							+ phvTabTitle);

					tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				}
				i++;
			}
			
			tabsSQLPlan.removeTabAt(0);
		}
		
		/**
		 * Update JTabbedPane
		 */
		private void update9i(){		
			// Reload data from DB
			List<String> arraySqlId = new ArrayList<String>();
			arraySqlId.add(sqlId);				
			database.loadSqlPlanFromDB(arraySqlId, isDetail);				
			database.loadSqlTextSqlIdFromDB(arraySqlId);
			
			// Get list of plan hash value's for sqlId
			List<Double> list = database.getSqlPlanHashValueBySqlId(sqlId);
			
			int i = 0;
			Iterator<Double> listIter = list.iterator();
			while (listIter.hasNext()) {
				Double planHashValue = listIter.next();
				String phvTabTitle = "PHV: " + planHashValue.longValue();
				ExplainPlanModel9i sqlPlanModel = (ExplainPlanModel9i) database
						.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
				if (sqlPlanModel != null) {
					JScrollPane scrollPane = new JScrollPane(
							createTreeTable(sqlPlanModel));

					tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				} else {
					JTextArea emptyText = new JTextArea();
					emptyText.setEditable(false);
					emptyText.setBackground(new Color(255, 250, 237));
					emptyText.setText("No data for Hash Value: " + sqlId + " and "
							+ phvTabTitle);

					tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				}
				i++;
			}
			
			tabsSQLPlan.removeTabAt(0);
		}
		
		
		
		/**
		 * Update Sql Plan hash value from Cursor Cache
		 */
		private void getSqlPlanCursorCache(boolean isTabCursorCacheExist, int iTab){
			
			StringBuffer out = database.getSqlPlanDBMS_XPLAN(sqlId, 0);
			JEditorPane jtextAreaSqlText = new JEditorPane();
			jtextAreaSqlText.setContentType("text/html");
			jtextAreaSqlText.setEditable(false);
			jtextAreaSqlText.setBackground(new Color(255, 250, 237));
			jtextAreaSqlText.setText(out.toString());
			JScrollPane scrollPane = new JScrollPane(jtextAreaSqlText);

			if (!isTabCursorCacheExist) {
				tabsSQLPlan.add(CURSORCACHE, scrollPane);
				tabsSQLPlan.setSelectedIndex(tabsSQLPlan.getTabCount() - 1);
			} else {
				tabsSQLPlan.setComponentAt(iTab, scrollPane);
				tabsSQLPlan.setSelectedIndex(iTab);
			}
		}
		
		/**
		 * Update Sql Plan hash value from AWR
		 */
		private void getSqlPlanAWR(boolean isTabAWRExist, int iTab){
		
			StringBuffer out = database.getSqlPlanDBMS_XPLAN(sqlId, 1);
			JEditorPane jtextAreaSqlText = new JEditorPane();
			jtextAreaSqlText.setContentType("text/html");
			jtextAreaSqlText.setEditable(false);
			jtextAreaSqlText.setBackground(new Color(255, 250, 237));
			jtextAreaSqlText.setText(out.toString());
			JScrollPane scrollPane = new JScrollPane(jtextAreaSqlText);

			if (!isTabAWRExist) {
				tabsSQLPlan.add(AWR, scrollPane);
				tabsSQLPlan.setSelectedIndex(tabsSQLPlan.getTabCount() - 1);
			} else {
				tabsSQLPlan.setComponentAt(iTab, scrollPane);
				tabsSQLPlan.setSelectedIndex(iTab);
			}
		}
	}

	/**
	 * Create JXTreeTable
	 * @param model
	 * @return
	 */
	protected JXTreeTable createTreeTable(TreeTableModel model) {
		if (model == null) {
			return null;
		}
		JXTreeTable treeTable = new JXTreeTable(model);
		treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		treeTable.setColumnControlVisible(true);
		treeTable.setRolloverEnabled(true);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setLeafIcon(null);
		treeTable.setTreeCellRenderer(renderer);
		treeTable.setShowGrid(true, true);
		treeTable.setBackground(new Color(255, 250, 237));
		treeTable.expandAll();

		  TableColumnModel columnModel = treeTable.getColumnModel();

			if (model instanceof ExplainPlanModel10g2) {
				columnModel.getColumn(0).setPreferredWidth(300);
				columnModel.getColumn(11).setPreferredWidth(70);
				columnModel.getColumn(13).setPreferredWidth(50);
				columnModel.getColumn(20).setPreferredWidth(40);
				columnModel.getColumn(21).setPreferredWidth(40);
				columnModel.getColumn(22).setPreferredWidth(40);
				

				List tableColumnExtList = new ArrayList<TableColumnExt>();
				for (int i = 0; i < 38; i++) {
					tableColumnExtList.add(treeTable.getColumnExt(i));
				}
				int i = 0;
				Iterator tableColumnExtListIter = tableColumnExtList.iterator();
				while (tableColumnExtListIter.hasNext()) {
					TableColumnExt tmpColumn = (TableColumnExt) tableColumnExtListIter
							.next();
					if (i == 0 || i == 11 || i == 13 || i == 20 || i == 21
							|| i == 22) {
						tmpColumn.setVisible(true);
					} else {
						tmpColumn.setVisible(false);
					}
					i++;
				}
				
			} else { // ExplainPlanModel9i
				columnModel.getColumn(0).setPreferredWidth(300);
				columnModel.getColumn(11).setPreferredWidth(70);
				columnModel.getColumn(18).setPreferredWidth(50);
				columnModel.getColumn(19).setPreferredWidth(40);
				columnModel.getColumn(20).setPreferredWidth(40);
				//columnModel.getColumn(22).setPreferredWidth(40);
				
				List tableColumnExtList = new ArrayList<TableColumnExt>();
				for (int i = 0; i < 32; i++) {
					tableColumnExtList.add(treeTable.getColumnExt(i));
				}
				int i = 0;
				Iterator tableColumnExtListIter = tableColumnExtList.iterator();
				while (tableColumnExtListIter.hasNext()) {
					TableColumnExt tmpColumn = (TableColumnExt) tableColumnExtListIter
							.next();
					if (i == 0 || i == 11 || i == 18 || i == 19 || i == 20) {
						tmpColumn.setVisible(true);
					} else {
						tmpColumn.setVisible(false);
					}
					i++;
				}
			}
			
		

		return treeTable;
	}
	
	/**
	 * Load tabs for 10g and higher version (EE edition)
	 * @param tabsSQLPlan
	 * @param tabIdPlanHashValue
	 * @param sqlId
	 * @param isDetail
	 */
	protected void addTabs10g11gEE(JTabbedPane tabsSQLPlan, HashMap<Integer, 
									Double> tabIdPlanHashValue, String sqlId, boolean isDetail){
		
		// Get list of plan hash value's for sqlId
		List<Double> list = null;
		try {
			if (!isDetail) {
				list = database.getSqlsTemp()
						.getSqlPlanHashValue(sqlId);
			} else {
				list = database.getSqlsTempDetail()
						.getSqlPlanHashValue(sqlId);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		int i = 0;
		Iterator<Double> listIter = list.iterator();
		while (listIter.hasNext()) {
			Double planHashValue = listIter.next();
			String phvTabTitle = "PHV: " + planHashValue.longValue();

			ExplainPlanModel10g2 sqlPlanModel = (ExplainPlanModel10g2) database
					.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
			if (sqlPlanModel != null) {
				JScrollPane scrollPane = new JScrollPane(
						createTreeTable(sqlPlanModel));

				tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				tabIdPlanHashValue.put(i, planHashValue);
			} else {
				JTextArea emptyText = new JTextArea();
				emptyText.setEditable(false);
				emptyText.setBackground(new Color(255, 250, 237));
				emptyText.setText("No data for SQL_ID: " + sqlId + " and "
						+ phvTabTitle);

				tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				tabIdPlanHashValue.put(i, planHashValue);
			}
			i++;
		}
		
	}
	
	/**
	 * Load tabs for 10g and higher version (SE edition)
	 * @param tabsSQLPlan
	 * @param tabIdPlanHashValue
	 * @param sqlId
	 * @param isDetail
	 */
	protected void addTabs10g11gSE(JTabbedPane tabsSQLPlan, HashMap<Integer, Double> tabIdPlanHashValue, 
								String sqlId, boolean isDetail){
		
		// Get list of plan hash value's for sqlId
		List<Double> list = database.getSqlPlanHashValueBySqlId(sqlId);
		
		int i = 0;
		Iterator<Double> listIter = list.iterator();
		while (listIter.hasNext()) {
			Double planHashValue = listIter.next();
			String phvTabTitle = "PHV: " + planHashValue.longValue();

			ExplainPlanModel10g2 sqlPlanModel = (ExplainPlanModel10g2) database
					.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
			if (sqlPlanModel != null) {
				JScrollPane scrollPane = new JScrollPane(
						createTreeTable(sqlPlanModel));

				tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				tabIdPlanHashValue.put(i, planHashValue);
			} else {
				JTextArea emptyText = new JTextArea();
				emptyText.setEditable(false);
				emptyText.setBackground(new Color(255, 250, 237));
				emptyText.setText("No data for SQL_ID: " + sqlId + " and "
						+ phvTabTitle);

				tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				tabIdPlanHashValue.put(i, planHashValue);
			}
			i++;
		}
		
	}
	
	/**
	 * Load tabs for Oracle 9i 
	 * @param tabsSQLPlan
	 * @param tabIdPlanHashValue
	 * @param sqlId
	 * @param isDetail
	 */
	protected void addTabs9i(JTabbedPane tabsSQLPlan, HashMap<Integer, Double> tabIdPlanHashValue, 
								String sqlId, boolean isDetail){
		
		// Get list of plan hash value's for sqlId
		List<Double> list = database.getSqlPlanHashValueBySqlId(sqlId);
		
		int i = 0;
		Iterator<Double> listIter = list.iterator();
		while (listIter.hasNext()) {
			Double planHashValue = listIter.next();
			String phvTabTitle = "PHV: " + planHashValue.longValue();

			ExplainPlanModel9i sqlPlanModel = (ExplainPlanModel9i) database
					.getSqlPlanModelByPlanHashValue(planHashValue, sqlId);
			if (sqlPlanModel != null) {
				JScrollPane scrollPane = new JScrollPane(
						createTreeTable(sqlPlanModel));

				tabsSQLPlan.addTab(phvTabTitle, scrollPane);
				tabIdPlanHashValue.put(i, planHashValue);
			} else {
				JTextArea emptyText = new JTextArea();
				emptyText.setEditable(false);
				emptyText.setBackground(new Color(255, 250, 237));
				emptyText.setText("No data for Hash value: " + sqlId + " and "
						+ phvTabTitle);

				tabsSQLPlan.addTab(phvTabTitle+" n/a", emptyText);
				tabIdPlanHashValue.put(i, planHashValue);
			}
			i++;
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

}
