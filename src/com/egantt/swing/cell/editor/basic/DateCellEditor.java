/*
 * @(#)DateCellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.editor.basic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.egantt.swing.cell.CellState;

import com.egantt.swing.cell.editor.AbstractCellEditor;

import com.egantt.swing.cell.editor.event.CellEvent;

public class DateCellEditor extends AbstractCellEditor
{
	protected transient ActionListener listener = new LocalActionListener(); 
	
	protected Format format = new SimpleDateFormat("hh:mm"); 
	protected JTextField component = new JTextField();	
	
	// _________________________________________________________________________

	/**
	 *  Returns the underlying component
	 */
	public JComponent getComponent(CellState state, JComponent parent)
	{
		component.setText(format.format(state.getValue()));
		return component;
	}	
	/**
	 *  Returns the value from the component
	 */
	public Object getValue()
	{
		/**Date date = null;	
		try
		{
			date = (Date) format.parseObject(component.getText());
		}
		catch (Exception ex)
		{
		}*/
		return component.getText();
	}
	
	//	------------------------------------------------------------------------------
		
	protected class LocalActionListener implements ActionListener
	{
		//	---------------------------------------------------------------------------
		
		public void actionPerformed(ActionEvent event)
		{
			Date value = (Date) getValue();
			if (value != null)
				notifyChanges(new CellEvent(component, value));
		}
	}
}