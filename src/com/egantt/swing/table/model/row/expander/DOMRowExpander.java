/*
 * @(#)DOMRowExpander.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row.expander;

import com.egantt.swing.table.model.row.RowExpander;

import org.w3c.dom.Element;

public class DOMRowExpander implements RowExpander
{
	protected final String columnName;

	public DOMRowExpander(String columnName)
	{
		this.columnName = columnName;
	}

	// _________________________________________________________________________

	public void collapse(int row, int col, Object value)
	{
		Element element = (Element) value;
		element.setAttribute(columnName, "false");
	}

	public void expand(int row, int col, Object value)
	{
		Element element = (Element) value;
		element.setAttribute(columnName, "true");
	}

	// _________________________________________________________________________

	public void toggle(int row, int col, Object value)
	{
		Element element = (Element) value;

		if (element.getAttribute(columnName).equals("true"))
			element.setAttribute(columnName, "false");
		else
			element.setAttribute(columnName, "true");
	}

	// _________________________________________________________________________

	public boolean isExpanded(int row, int col, Object value)
	{
		Element element = (Element) value;
		return element.getAttribute(columnName).equals("true");
	}
}
