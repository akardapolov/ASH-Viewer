/*
 * @(#)BasicColumnManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.column.manager;

import com.egantt.swing.table.model.column.ColumnManager;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *  <code>BasicColumnManager</code> is a basic implementation of the <code>ColumnManager</code>
 *  is intended to provide managing <code>TableColumn</code>'s easily.
 */
public class BasicColumnManager implements ColumnManager
{
	protected final TableModel model;
	protected final ListSelectionModel selectionModel;

	protected int index = 0;
	protected List<TableColumnModel> models = new ArrayList<TableColumnModel>();

	/**
	 * This constructor will automatically generate a column model
	 * @param model
	 */
	public BasicColumnManager(TableModel model)
	{
		this.model = model;
		this.selectionModel = new DefaultListSelectionModel();
		
		final DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
		for (int i=0; i < model.getColumnCount(); i++) {
			TableColumn column = new TableColumn(i);
			columnModel.addColumn(column);
		}
		models.add(columnModel);
	}
	
	public BasicColumnManager(TableModel model, Object columnNames[][])
	{
		this.model = model;
		this.selectionModel = new DefaultListSelectionModel();	
		int columnCount = 0;
		for (int a=0; a < columnNames.length; a++) {
			final DefaultTableColumnModelExt columnModel = new DefaultTableColumnModelExt();
			models.add(columnModel);

			for (int b=0; b < columnNames[a].length; b++) {
				TableColumnExt column = new TableColumnExt(columnCount++);
				column.setHeaderValue(columnNames[a][b]);
				columnModel.addColumn(column);
			}
		}
	}
	
	/**
	 *  This one will not if you don't want a column model yet use this and feed through null
	 * @param model not null
	 * @param columnModel can be null
	 */
	public BasicColumnManager(TableModel model, TableColumnModel columnModel) {
		this.model = model;
		this.selectionModel = new DefaultListSelectionModel();
		
		if (columnModel != null)
			models.add(columnModel);
	}
	
	// __________________________________________________________________________

	public TableModel getModel()
	{
		return model;
	}

	// __________________________________________________________________________

	public TableColumnModel get(int index)
	{
		return models.size() > index ? models.get(index) : null;
	}

	// __________________________________________________________________________

	public int size()
	{
		return models.size();
	}

	// __________________________________________________________________________

	/**
	 *  Adds a <code>TableColumn</code> to the uppermost <code>TableColumnModel</code>
	 *  in the stack.
	 */
	public void add(TableColumn column)
	{
		TableColumnModel model = (TableColumnModel) models.get(models.size() - 1);
		column.setModelIndex(index++);
		model.addColumn(column);
	}

	/**
	 *  Adds a <code>TableColumnModel</code> which becomes the uppermost in the stack.
	 */
	public void add(TableColumnModel columnModel)
	{
		models.add(columnModel);
	}

	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}
}
