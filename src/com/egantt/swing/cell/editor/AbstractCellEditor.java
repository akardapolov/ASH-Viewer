/*
 * @(#)AbstractCellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ChangeListener;

import com.egantt.swing.cell.CellEditor;

import com.egantt.swing.cell.editor.event.CellEvent;
import com.egantt.swing.cell.editor.event.CellListener;
 
public abstract class AbstractCellEditor implements CellEditor
{
	protected final List<CellListener>listeners = new ArrayList<CellListener>();

	//	---------------------------------------------------------
		
	public void addCellListener(CellListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeCellListener(CellListener listener)
	{
		listeners.remove(listener);
	}
	
	public void cancelEditing() {
	}

	public void stopEditing() {
	}
	

	public boolean isCellEditable(EventObject eo) {
		return true;
	}
	
	protected void notifyChanges(CellEvent event)
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext();)
		{
			ChangeListener listener = (ChangeListener) iter.next();
			listener.stateChanged(event);
		}
	}
}