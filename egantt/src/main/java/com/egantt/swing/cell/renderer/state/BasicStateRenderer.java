/*
 * @(#)BasicStateRenderer.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.renderer.state;

import java.util.Iterator;

import javax.swing.JComponent;

import com.egantt.drawing.DrawingComponent;
import com.egantt.drawing.component.DrawingComponentPainter;
import com.egantt.model.drawing.DrawingState;
import com.egantt.swing.cell.CellRenderer;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.ComponentResources;

import ext.egantt.util.reflect.ReflectionHelper;

/**
  *  DrawingCellRender is used as a wrapper around the drawing in order to insert
  *  it into a JXTable Cell
  */
public class BasicStateRenderer implements CellRenderer
{
	protected final DrawingComponent component;
	protected DrawingComponentPainter painter;

	public BasicStateRenderer(DrawingComponent component, ComponentContext context)
	{
		this.component = component;

		for (Iterator iter = ReflectionHelper.fields(ComponentResources.class); iter.hasNext();)
		{
			String field = (String) iter.next();
			Object value = context.get(field);
			if (field != null && field.length() > 0)
				ReflectionHelper.set(component, field, value);
		}
	}

	// _________________________________________________________________________

	public void setComponentPainter(DrawingComponentPainter painter)
	{
		this.painter = painter; // need to keep this @link #setDrawingState
		component.setComponentPainter(painter);
	}

	// _________________________________________________________________________

	public JComponent getComponent(CellState cellState, JComponent parent)
	{
		Object value = cellState.getValue();
		if (!(value instanceof DrawingState))
			return null;

		initialize(cellState);
		return (JComponent) component;
	}
	// _________________________________________________________________________

	/**
	 *  Should be called only internally from the @see #getTableCellRenderComponent
	 */
	protected void initialize(CellState cellState)
	{
		component.setCellState(cellState);
	}
}
