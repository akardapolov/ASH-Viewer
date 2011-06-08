/*
 * @(#)CellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell;

import java.util.EventObject;

import com.egantt.swing.cell.editor.event.CellListener;

public interface CellEditor extends CellRenderer
{
	/**
	 *  Add a <code>CellListener</code>
	 */
	void addCellListener(CellListener listener);
	
	/**
	 *  Remove a <code>CellListener</code>
	 */
	void removeCellListener(CellListener listener);

	Object getValue();

	void cancelEditing();

	void stopEditing();
	
	boolean isCellEditable(EventObject eo);
}
