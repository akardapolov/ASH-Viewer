/*
 * @(#)RowModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model;

/**
 *  An implementation of the rows in a table
 */
public interface RowModel
{
    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     * note: @see #size must be called first
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
	Object getValueAt(int row, int column);

	/**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
	 * note: @see #size must be called first
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
	void setValueAt(Object value, int row, int column);
	
	/**
	 *  Returns the number of rows in the model, the table implementation
     *  uses this method to determine how many rows it
     *  should display.
	 */
	int size();
}
