/*
 * @(#)BooleanCellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.editor.basic;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.egantt.swing.cell.CellState;

import com.egantt.swing.cell.editor.AbstractCellEditor;

/**
 *  A field to render the Boolean primitive, uss JCheckbox
 */
public class BooleanCellEditor extends AbstractCellEditor
{
	protected JCheckBox component = new JCheckBox();

	// _________________________________________________________________________

	/**
	 *  Returns the underlying component
	 */
	public JComponent getComponent(CellState state, JComponent parent)
	{
		boolean value =  ((Boolean) state.getValue()).booleanValue();
		component.setSelected(value);
		return component;
	}

	// _________________________________________________________________________

	/**
	 *  Returns the value from the component
	 */
	public Object getValue()
	{
		return new Boolean(component.isSelected());
	}

	/**
	 *  Expects a java.lang.Boolean and updates the selected state
	 */
	public void setValue(Object value)
	{
		component.setSelected( ((Boolean) value).booleanValue());
	}
}
