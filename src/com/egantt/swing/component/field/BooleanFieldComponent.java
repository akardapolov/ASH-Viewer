/*
 * @(#)BooleanFieldComponent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.field;

import java.awt.Component;

import javax.swing.JCheckBox;

import com.egantt.swing.component.FieldComponent;

/**
 *  A field to render the Boolean primitive, uss JCheckbox
 */
public class BooleanFieldComponent implements FieldComponent
{
	protected JCheckBox component = new JCheckBox();

	// _________________________________________________________________________

	/**
	 *  Returns the underlying component
	 */
	public Component getComponent()
	{
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
