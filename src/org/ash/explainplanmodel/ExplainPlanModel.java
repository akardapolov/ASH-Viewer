/*
 *-------------------
 * The ExplainPlanModel.java is part of ASH Viewer
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
package org.ash.explainplanmodel;

public interface ExplainPlanModel {

	/**
	 * Returns the number of children of <code>node</code>.
	 */
	public int getChildCount(Object node);

	/**
	 * Returns the child of <code>node</code> at index <code>i</code>.
	 */
	public Object getChild(Object node, int i);

	/**
	 * Returns true if the passed in object represents a leaf, false
	 * otherwise.
	 */
	public boolean isLeaf(Object node);

	/**
	 * Returns the number of columns.
	 */
	public int getColumnCount();

	/**
	 * Returns the name for a particular column.
	 */
	public String getColumnName(int column);

	/**
	 * Returns the class for the particular column.
	 */
	public Class<?> getColumnClass(int column);

	/**
	 * Returns the value of the particular column.
	 */
	public Object getValueAt(Object node, int column);

	public int getIndexOfChild(Object parent, Object child);

}