/*
 * @(#)AbstractModelAdapter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import javax.swing.table.TableModel;

/**
 *  Provides the bridge from the generic E-Gantt table model implementation
 *  to the swing TableModel used by swing.
 */
public abstract class AbstractModelAdapter implements TableModel
{
	protected final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	// _________________________________________________________________________

	public void addTableModelListener(TableModelListener listener)
	{
		listeners.add(listener);
	}

	public void removeTableModelListener(TableModelListener listener)
	{
		listeners.remove(listener);
	}

	// _________________________________________________________________________

	public void fireTableDataChanged()
	{
		TableModelEvent e = new TableModelEvent(this,0, getRowCount());
		for (Iterator iter = listeners.iterator(); iter.hasNext();)
			((TableModelListener)iter.next()).tableChanged(e);
	}
}
