/*
 * @(#)ListNavigatorModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row.navigator;

import java.util.List;

public class ListNavigatorModel extends AbstractNavigatorModel
{
	// _________________________________________________________________________
	
	protected Object getValue(Object o, int col)
	{
		return ((List) o).get(col);
	}
	
	protected void setValue(Object o, int col, Object value)
	{
		((List <Object>) o).set(col, value);
	}
}
