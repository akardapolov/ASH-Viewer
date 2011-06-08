/*
 * @(#)BasicModelAdapter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.adapter;

import com.egantt.swing.table.model.ColumnModel;
import com.egantt.swing.table.model.RowModel;

/**
 *  Provides the bridge from the generic E-Gantt table model implementation
 *  to the swing TableModel used by swing.
 */
public class BasicModelAdapter extends AbstractModelAdapter
{
	protected final ColumnModel columns;
	protected final RowModel rows;

	public BasicModelAdapter(ColumnModel columns, RowModel rows)
	{
		this.columns = columns;
		this.rows = rows;
	}

	// _________________________________________________________________________
	
	public int getColumnCount()
	{
	   return columns.size();
   }

	public int getRowCount()
	{
	   return rows.size();
	}

	// _________________________________________________________________________

	public String getColumnName(int index)
	{
		return columns.getColumn(index).toString();
	}

	public Class getColumnClass(int index)
	{
		return columns.getColumnClass(index);
	}

	// _________________________________________________________________________

	public Object getValueAt(int row, int column)
	{
		return rows.getValueAt(row, column);
	}

	public void setValueAt(Object value, int row, int column)
	{
		rows.setValueAt(value, row, column);
	}

	// _________________________________________________________________________

	public boolean isCellEditable(int row, int column)
	{
		return true;
	}
}
