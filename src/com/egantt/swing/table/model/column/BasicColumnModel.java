/*
 * @(#)BasicColumnModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.column;

import com.egantt.swing.table.model.ColumnModel;

/**
 *  A Basic implementation of the <code>ColumnMode</code> interface
 */
public class BasicColumnModel implements ColumnModel
{
	/** the root node is internal and therefore invisible*/
	protected final Object columns[];
	protected final Class [] columnClasses; /** column classes */

	public BasicColumnModel(Object columns [], Class columnClasses [])
	{
		this.columnClasses = columnClasses;
		this.columns = columns;
	}

	// _________________________________________________________________________

	public Object getColumn(int index)
	{
	   return columns[index];
	}

	public Class getColumnClass(int index)
	{
		return columnClasses[index] != null ? columnClasses[index] : Object.class;
	}

	// _________________________________________________________________________

	public int size()
	{
		return columns.length;
	}
}
