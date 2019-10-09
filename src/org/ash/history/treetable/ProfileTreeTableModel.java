/*
 *-------------------
 * The ProfileTreeTableModel.java is part of ASH Viewer
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
package org.ash.history.treetable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class ProfileTreeTableModel extends DefaultTreeTableModel {
    private static final int NAME = 0;

    private static final int SIZE = 1;

    private static final int FOLDER = 2;

    public ProfileTreeTableModel(TreeNode node) {
        super(node);
    }

    @Override
	public int getColumnCount() {
        return 3;
    }

    /**
     * Returns which object is displayed in this column.
     */
    @Override
	public Object getValueAt(Object node, int column) {
        Object res = "n/a";
        if (node instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) node;
            if (defNode.getUserObject() instanceof ProfileData) {
                ProfileData profile = (ProfileData) defNode.getUserObject();
                switch (column) {
                case NAME:
                    res = profile.getName();
                    break;
                case SIZE:
                    res = profile.getSize();
                    break;
                case FOLDER:
                    res = profile.getDirectory();
                    break;
                }
            }
        }
        return res;
    }

    /**
     * What the TableHeader displays when the Table is in a JScrollPane.
     */
    @Override
	public String getColumnName(int column) {
        String res = "";
        switch (column) {
        case NAME:
            res = "Profile";
            break;
        case SIZE:
            res = "Size, Mb";
            break;
        case FOLDER:
            res = "Directory";
            break;
        }
        return res;
    }


    /**
     * Tells if a column can be edited.
     */
    @Override
	public boolean isCellEditable(Object node, int column) {
        return false;
    }

    /**
     * Called when done editing a cell.
     */
    @Override
	public void setValueAt(Object value, Object node, int column) {
    }
}
