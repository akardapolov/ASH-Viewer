/*
 *-------------------
 * The MainProfileTree.java is part of ASH Viewer
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
import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.ash.history.treetable.DefaultTreeTableModel;
import org.ash.history.treetable.ProfileData;
import org.ash.history.treetable.ProfileTreeTableModel;
import org.ash.util.Options;
import org.ash.util.Utils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * The Class ProfileTree (history).
 */
public class MainProfileTree extends JPanel {
	/**
	 * Constructor DatabaseTree
	 * 
	 * @param mainFrame0
	 * @param database0
	 */
	public MainProfileTree(JFrame mainFrame0) {
		this.mainFrame = mainFrame0;
		this.initialize();
	}

	/**
	 * Initialize DatabaseTree
	 */
	private void initialize() {

		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.add(scroll, BorderLayout.CENTER);

		treeTable = new JXTreeTable(generateModel());
		treeTable.setColumnControlVisible(true);
		treeTable.setRolloverEnabled(true);
		treeTable.setHorizontalScrollEnabled(true);
		treeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		treeTable.addTreeExpansionListener(new ProfileTreeExpansionListener());
		treeTable.packAll();

		treeTable.setHighlighters(HighlighterFactory.createSimpleStriping());
		treeTable
				.addHighlighter(new ColorHighlighter(
						HighlightPredicate.ROLLOVER_ROW, Color.LIGHT_GRAY,
						Color.WHITE));

		treeTable.setTreeCellRenderer(new ProfileDefaultTreeCellRenderer());
		treeTable.addTreeSelectionListener(new ProfileTreeSelectionListener());
		
		scroll.setViewportView(treeTable);
	}

	/**
	 * Load data to Model for JXTreeJTable
	 * 
	 * @return ProfileTreeTableModel
	 */
	private ProfileTreeTableModel generateModel() {

		// shouldn't be visible
		DefaultMutableTreeNode aRoot = new DefaultMutableTreeNode(
				new ProfileData());

		// retrieve .ini file list...
		File dir = new File("profile");
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".ini");
			}
		});

		for (int i = 0; i < files.length; i++) {
			String tempBDB = files[i].toString().substring(8);
			tempBDB = tempBDB.substring(0, tempBDB.length() - 4);

			File dirProfile = new File(tempBDB);

			File[] filesBDB = dirProfile.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			// Root profile
			DefaultMutableTreeNode subRoot = new DefaultMutableTreeNode(
					new ProfileData(tempBDB, "", "", 0.0, 0.0));

			boolean isInsSubroot = false;

			for (int ii = 0; ii < filesBDB.length; ii++) {

				// Add subRoot
				if (!isInsSubroot) {
					aRoot.add(subRoot);
					isInsSubroot = true;
				}

				// 1. Get folder name
				String directory = filesBDB[ii].toString().substring(
						filesBDB[ii].toString().indexOf(FILESEPARATOR) + 1,
						filesBDB[ii].toString().length());
				Date tempDataDt = null;
				try {
					tempDataDt = dateFormatDB.parse(directory);
				} catch (ParseException e) {
					tempDataDt = null;
				}

				if (tempDataDt != null) {

					// 2. Get profile name
					String name = "";
					name = dateFormatFull.format(tempDataDt).toString();

					// 3. Get BDB size (Mb)
					long lsize = Utils.getFolderSize(
							new File(tempBDB +FILESEPARATOR+ directory))/(1024*1024);
					String ssize = (lsize == 0 ? "< 1" : lsize+"");
					
					// 4. Set tempFirstKey
					Double tempFirstKey =  new Long(tempDataDt.getTime()).doubleValue();
					
					// Insert node
					DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(
							new ProfileData(name, ssize, tempBDB +FILESEPARATOR+ directory, tempFirstKey, 0.0));
					subRoot.add(leaf);

				}

			}
		}

		return new ProfileTreeTableModel(aRoot);
	}

	/**
	 *  PackAll when collapse/expande node. 
	 */
	class ProfileTreeExpansionListener implements TreeExpansionListener {
		public void treeCollapsed(TreeExpansionEvent event) {
			treeTable.packAll();
		}

		public void treeExpanded(TreeExpansionEvent event) {
			treeTable.packAll();
		}
	}

	/**
	 * TreeSelectionListener for JXTreeJTable
	 */
	class ProfileTreeSelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			
			Object node = null;
			
			try {
				node = 
					treeTable.getPathForRow(
						treeTable.getSelectedRow()).getLastPathComponent();
			} catch (Exception ex){
				System.out.println("Error on ProfileTreeSelectionListener.valueChanged");
				return;
			}
			
			if (node instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode dmtt = (DefaultMutableTreeNode) node;
                if (dmtt.getUserObject() instanceof ProfileData) {
                	ProfileData nodeInfo = (ProfileData)dmtt.getUserObject();
                	/* if nothing is selected */
    				if (nodeInfo.getDirectory().equalsIgnoreCase(""))
    					return;
                	fireRunAction(nodeInfo.getDirectory());
                }
            }
		}
	}
	
	/**
	 * DefaultTreeCellRenderer for JXTreeJTable
	 */
	class ProfileDefaultTreeCellRenderer extends DefaultTreeCellRenderer{
		private static final long serialVersionUID = 1L;

		@Override
		public java.awt.Component getTreeCellRendererComponent(
				javax.swing.JTree tree, Object value, boolean sel,
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				ProfileData profile = (ProfileData) node.getUserObject();
				setText(profile.getName());
				if (node.isLeaf()
						&& node.getParent() == tree.getModel().getRoot()) {
					setIcon(getDefaultClosedIcon());
				}
				
				if (Options.getInstance().getEnvDir().trim().equalsIgnoreCase(
						profile.getDirectory().trim()) 
						&& !profile.getDirectory().trim().equalsIgnoreCase("")) {
					setForeground(Color.RED);
				} 
			}
			return this;
		};
	}
	
	/**
	 * Delete node from jtreetable when user delete all data from calendar
	 * 
	 */
	public void deleteNodeAllData(){
				
		TreePath selp = treeTable.getTreeSelectionModel().getSelectionPath();
		
		if (selp != null) {
			
			 DefaultMutableTreeNode toBeDeletedNode = 
				 (DefaultMutableTreeNode) selp.getLastPathComponent();
			
			 DefaultMutableTreeNode nextToBeSelected = toBeDeletedNode
             				.getNextSibling();
			 
			    if (nextToBeSelected == null) {
                    nextToBeSelected = toBeDeletedNode.getPreviousSibling();
                    if (nextToBeSelected == null) {
                        nextToBeSelected = (DefaultMutableTreeNode) toBeDeletedNode
                                .getParent();
                    }
                }
			    
			    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) toBeDeletedNode
                .getParent();
			    int idx = parentNode.getIndex(toBeDeletedNode);
			    parentNode.remove(toBeDeletedNode);
		
			    int[] childIndices = new int[1];
                childIndices[0] = idx;
                Object[] delNodes = new Object[1];
                delNodes[0] = toBeDeletedNode;
                ((DefaultTreeTableModel) treeTable.getTreeTableModel())
                        .nodesWereRemoved(parentNode, childIndices, delNodes);
                treeTable.getTreeSelectionModel().setSelectionPath(
                        new TreePath(nextToBeSelected.getPath()));
			    
		}
	}
	
	/**
	 * Add listener for database
	 * @param l
	 */
	public void addListener(Object l) {
		listeners.add(l);
	}

	/**
	 * Remove listener database
	 * @param l
	 * @return
	 */
	public boolean removeListener(Object l) {
		return listeners.remove(l);
	}

	/**
	 * Fire run action.
	 */
	protected void fireRunAction(String evnDir) {
		Iterator iStart = listeners.iterator();
		while (iStart.hasNext()) {
			Object currListeners = iStart.next();
			if (currListeners instanceof MainPreview) {
				MainPreview tempObj = (MainPreview) currListeners;
				tempObj.loadCalendarRun(evnDir);
			}
		}
	}

	private JScrollPane scroll = new JScrollPane();
	private DateFormat dateFormatDB = new SimpleDateFormat("ddMMyyyyHHmms");
	private DateFormat dateFormatFull = new SimpleDateFormat("EEE, d MMM HH:mm");
	private List listeners = new ArrayList();
	private final String FILESEPARATOR = System.getProperty("file.separator");
	private JXTreeTable treeTable;

	private JFrame mainFrame;
}
