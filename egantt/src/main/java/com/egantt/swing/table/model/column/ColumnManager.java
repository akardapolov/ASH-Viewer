/*
 * @(#)ColumnManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.column;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import javax.swing.table.TableModel;

public interface ColumnManager
{
	// _________________________________________________________________________

	TableColumnModel get(int index);

	TableModel getModel();

	// _________________________________________________________________________

	int size();

	ListSelectionModel getSelectionModel();
}
