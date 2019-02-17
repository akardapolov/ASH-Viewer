/*
 * @(#)ColumnUtilities.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.model.table;

import com.egantt.swing.table.model.column.ColumnManager;

/**
 *  Brought into effect to provide a seperation of data, for the different
 *  charting types that E-Gantt utilises.
 */
public interface ColumnUtilities
{
	/**
	 *  Returns a ColumnManager
	 */
	ColumnManager getColumnManager();
}
