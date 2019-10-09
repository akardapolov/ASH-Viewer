/*
 *-------------------
 * The GanttSplitPane.java is part of ASH Viewer
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

import javax.swing.JSplitPane;

/**
 * The Class EganttSplitPane.
 */
public class GanttSplitPane extends JSplitPane {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3104977481978858679L;

	/**
	 * Instantiates a new egantt split pane.
	 * 
	 * @param type the type
	 */
	public GanttSplitPane(int type) {
		super(type);
		setDividerSize(10);
	}
}
