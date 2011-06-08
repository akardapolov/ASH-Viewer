/*
 * @(#)ColumnModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model;

/**
 *  Contains a set of columns and there basic Class types
 */
public interface ColumnModel
{
	/**
	 *  number of columns in the model
	 */
	int size();
	
	// _________________________________________________________________________
	
	/**
	 *  returns the column at the position
	 */
	Object getColumn(int index);
	
	/**
	 *  return the column class
	 */
	Class getColumnClass(int index);
}
