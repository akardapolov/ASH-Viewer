/*
 *-------------------
 * The MainPanel.java is part of ASH Viewer
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.ash.database.ASHDatabase;
import org.ash.gui.StatusBar;

/**
 * The Class MainPanel (history).
 */
public class MainPanel extends JPanel {

	/** The MainFrame. */
	private JFrame mainFrame;
	
	/** The database. */
	private ASHDatabase database;
	
	/** The status bar. */
    private StatusBar statusBar;
	
    /** The DatabaseTree panel. */
    private MainProfileTree databaseTree;
    
    /** The HistoryStacked panel. */
    private MainPreview historyStacked;
   
	/** The split pane main. */
	private JSplitPane splitPaneMain;
    
	/**
	 * Constructor HistoryPanel
	 * 
	 * @param mainFrame0
	 * @param database0
	 * @param statusBar0
	 */
	public MainPanel(JFrame mainFrame0, StatusBar statusBar0){
		this.mainFrame = mainFrame0;
		this.statusBar = statusBar0;
		this.splitPaneMain = new JSplitPane();
		this.databaseTree = new MainProfileTree(mainFrame0);
		this.historyStacked = new MainPreview(mainFrame0, statusBar0);
		this.initialize();
	}
	
	/**
	 * Initialize HistoryPanel
	 */
	private void initialize() {
	
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		this.splitPaneMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPaneMain.add(this.databaseTree, JSplitPane.LEFT);
		this.splitPaneMain.add(this.historyStacked, JSplitPane.RIGHT);
		this.splitPaneMain.setDividerLocation(230);
		this.splitPaneMain.setOneTouchExpandable(true);
		
		this.add(splitPaneMain, BorderLayout.CENTER);
		
		this.databaseTree.addListener(this.historyStacked);
		this.historyStacked.addListenerNode(this.databaseTree);
		
	}
	
}
