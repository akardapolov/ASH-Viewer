/*
 * @(#)BasicJTableList.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.swing.table.list;

import com.egantt.model.component.ComponentList;
import org.jdesktop.swingx.JXTable;

/**
 *  Generates JXTable's based on the Component list
 */
public class BasicJTableList extends AbstractTableList implements ComponentList
{
	// __________________________________________________________________________

	protected JXTable createTable()
	{
		return new JXTable();
	}
}
