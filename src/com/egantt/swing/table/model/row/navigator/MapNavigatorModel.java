/**
 * @(#)MapNavigatorModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.swing.table.model.row.navigator;

import java.util.Map;

public class MapNavigatorModel extends AbstractNavigatorModel
{
	protected final Object columns[];
	
	public MapNavigatorModel(Object columns [])
	{
		this.columns = columns;
	}
	
	// _________________________________________________________________________
	
	protected Object getValue(Object o, int col)
	{
		return ((Map) o).get(columns[col]);
	}
	
	protected void setValue(Object o, int col, Object value)
	{
		((Map <Object, Object>) o).put(columns[col], value);
	}
}
