/*
 * @(#)ComponentList.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.component;

import javax.swing.JComponent;

import com.egantt.swing.table.model.column.ColumnManager;

/**
 *  Provides access to components within the list
 */
public interface ComponentList
{
	/**
	 *  E-Gantt currently only supports Swing therefore returns only JComponent's
	 */
	JComponent get(int position);

	/**
	 *  Returns the number of components this manager manages
	 */
	int size();

	void setColumnManager(ColumnManager manager);

	void setComponentManager(ComponentManager manager);
}
