/*
 * @(#)DOMNavigatorModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row.navigator;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

/**
 *  Uses the attributes from the Element to populate the rows
 */
public class DOMNavigatorModel extends AbstractNavigatorModel
{
	protected final Object columns [];
	protected final int index;

	protected Map<Object, Object[]>cache = new HashMap<Object, Object[]>(0);

	public DOMNavigatorModel(Object columns [], int index)
	{
		this.columns = columns;
		this.index = index;
	}

	public DOMNavigatorModel(Object columns [])
	{
		this (columns, -1);
	}

	// _________________________________________________________________________

	protected Object getValue(Object o, int col)
	{
		Object [] values = (Object []) cache.get(o);
		if (values != null && values[col] != null)
			return values[col];

		return col == index ? o : ((Element) o).getAttribute(columns[col].toString());
	}

	protected void setValue(Object o, int col, Object value)
	{
		// nothing can be done about this
		if (col == index)
			return;

		if (value instanceof String)
		{
			((Element) o).setAttribute(columns[col].toString(), (String) value);
			return;
		}

		Object values [] = (Object []) cache.get(o);
		if (values == null)
		{
			values = new Object[columns.length];
			cache.put(o, values);
		}
		values[col] =  value;
	}
}
