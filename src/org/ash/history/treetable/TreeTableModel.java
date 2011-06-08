/*
 *-------------------
 * The TreeTableModel.java is part of ASH Viewer
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

package org.ash.history.treetable;	// temporary package

import javax.swing.tree.TreeModel;

public interface TreeTableModel extends TreeModel {
    public Class getColumnClass(int column);
    public int getColumnCount();
    public String getColumnName(int column);
    public Object getValueAt(Object node, int column);
    public boolean isCellEditable(Object node, int column);
    public void setValueAt(Object value, Object node, int column);
}