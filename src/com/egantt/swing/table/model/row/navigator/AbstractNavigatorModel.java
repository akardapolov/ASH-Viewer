/*
 * @(#)AbstractNavigatorModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row.navigator;

import com.egantt.data.Navigator;
import com.egantt.swing.table.model.RowModel;

/**
 * <code>NavigatorRowModel</code> is a Navigator expressed as a table
 * rather than a linked-list. It attempts to provide an interface to
 * a parse tree that has very little overhead.
 */
public abstract class AbstractNavigatorModel implements RowModel
{
	protected Navigator navigator;
	protected transient Object rows [] = new Object[0];
	
	// _________________________________________________________________________
	
	public void setNavigator(Navigator navigator)
	{
		this.navigator = navigator;
	}
	
	// _________________________________________________________________________
		
	public int size()
	{
		rebuild();
		return rows.length;
	}
	
	// _________________________________________________________________________
	
	public final Object getValueAt(int row, int column)
	{
		return getValue(rows[row], column);
	}
	
	public final void setValueAt(Object value, int row, int column)
	{
		setValue(rows[row], column, value);
	}
	
	// _________________________________________________________________________
	
	protected final void rebuild()
	{
		ensureCapacity();
		
		int size = 0;
		for (Object o = navigator.first(); o!= null; o = navigator.next())
			rows[size++] = o;
	}
	
	// _________________________________________________________________________
	
	protected abstract Object getValue(Object o, int col);
	
	protected abstract void setValue(Object o, int col, Object value);
	
	// _________________________________________________________________________
	
	/**
	 *  note: this has the unfortunate effect of clearing the array under some
	 *  circumstances therefore this method is private.
	 */
	private void ensureCapacity()
	{
		int size = 0;
		for (Object o = navigator.first(); o != null; o = navigator.next())
			size++;
		
		rows = size != rows.length ? new Object[size] : rows;
	}
}
