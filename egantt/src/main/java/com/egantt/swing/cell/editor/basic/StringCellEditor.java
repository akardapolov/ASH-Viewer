/*
 * @(#)StringCellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.editor.basic;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.egantt.swing.cell.CellState;

import com.egantt.swing.cell.editor.AbstractCellEditor;

/**
 *  A field to render strings, uses JTextField
 */
public class StringCellEditor extends AbstractCellEditor
{
	protected JTextField component = new JTextField();

	// _________________________________________________________________________

	/**
	 *  Returns the underlying component
	 */
	public JComponent getComponent(CellState state, JComponent parent)
	{
		component.setText((String) state.getValue());
		return component;
	}

	public Object getValue() {
		return component.getText();
	}
}
