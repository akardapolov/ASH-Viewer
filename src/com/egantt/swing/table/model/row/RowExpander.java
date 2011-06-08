/*
 * @(#)RowExpander.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row;

/**
 *  Expands rows within the tree
 */
public interface RowExpander
{
	/**
	 *  Collapses a tree node
	 */
	void collapse(int row, int col, Object value);

	/**
	 *  Expands a tree node
	 */
	void expand(int row, int col, Object value);

	void toggle(int row, int col, Object value);

	// _________________________________________________________________________

	/**
	 *  Returns true if the node isExpanded
	 */
	boolean isExpanded(int row, int col, Object value);
}
