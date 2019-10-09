/*
 * @(#)BasicColumnManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.column.manager;

import com.egantt.swing.table.model.column.ColumnManager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *  <code>BasicColumnManager</code> is a basic implementation of the <code>ColumnManager</code>
 *  is intended to provide managing <code>TableColumn</code>'s easily.
 */
public class BasicColumnManager implements ColumnManager
{
	private TableModel model;
	private ListSelectionModel selectionModel;

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
			final DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
			models.add(columnModel);

			for (int b=0; b < columnNames[a].length; b++) {
				TableColumn column = new TableColumn(columnCount++);
				column.setHeaderValue(columnNames[a][b]);
				if (b==0){//Set size of gantt chart
					column.setPreferredWidth(200);
				}
				else if (b==1){//Set size of gantt chart
					column.setPreferredWidth(30);
				}
				else if (b==2){//Set size of gantt chart
					column.setPreferredWidth(30);
				}
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
		return models.size() > index ? (TableColumnModel) models.get(index) : null;
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
