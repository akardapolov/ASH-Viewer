/**
 * @(#)AbstractJTableList.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.swing.table.list;

import com.egantt.model.component.ComponentList;
import com.egantt.model.component.ComponentManager;
import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.context.DummyComponentContext;
import com.egantt.swing.table.model.column.ColumnManager;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates JXTable's based on the Component list
 */
public abstract class AbstractTableList implements ComponentList {
	protected final Map<Integer, JXTable> cache = new HashMap<>(1);

	protected ColumnManager columnManager;

	protected ComponentManager componentManager;

	protected ComponentContext headerComponentContext = DummyComponentContext.instance;

	protected ComponentContext rendererComponentContext = DummyComponentContext.instance;

	public ColumnManager getColumnManager() {
		return columnManager;
	}

	// __________________________________________________________________________

	public void setColumnManager(ColumnManager columns) {
		this.columnManager = columns;
	}

	public void setComponentManager(ComponentManager manager) {
		this.componentManager = manager;
	}

	public void setRendererComponentContext(
			ComponentContext rendererComponentContext) {
		this.rendererComponentContext = rendererComponentContext;
	}

	public void setHeaderComponentContext(
			ComponentContext headerComponentContext) {
		this.headerComponentContext = headerComponentContext;
	}

	// __________________________________________________________________________

	public int size() {
		return columnManager.size();
	}

	// __________________________________________________________________________

	public JComponent get(int index) {
		JXTable table = cache.containsKey(new Integer(index)) ? cache
				.get(new Integer(index)) : null;

		if (table == null) {
			table = createTable();
			initializeTable(table);
			cache.put(new Integer(index), table);
		}

		componentManager.registerComponent(table, rendererComponentContext);
		componentManager.registerComponent(table.getTableHeader(),
				headerComponentContext);

		{
			TableColumnModel columnModel = columnManager.get(index);
			if (columnModel != null)
				table.setColumnModel(columnModel);
		}
		return table;
	}

	// __________________________________________________________________________

	protected void initializeTable(JXTable table) {
		table.setAutoscrolls(false);
		table.setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setModel(columnManager.getModel());
		table.setSelectionModel(columnManager.getSelectionModel());
		table.setRowHeight(25);
	}

	protected abstract JXTable createTable();
}
