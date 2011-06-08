/*
 * @(#)BasicCellRenderer.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.renderer.basic;

import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.ComponentResources;

import com.egantt.swing.cell.CellRenderer;
import com.egantt.swing.cell.CellState;

import ext.egantt.util.reflect.ReflectionHelper;

import java.util.Iterator;

import javax.swing.JComponent;

public class BasicCellRenderer extends AbstractCellRenderer implements CellRenderer
{
	private static final long serialVersionUID = 3983528468928681094L;

	protected final ComponentContext context;

	public BasicCellRenderer(ComponentContext context)
	{
		this.context = context;
	}

	// _________________________________________________________________________

	public JComponent getComponent(CellState state, JComponent parent)
	{
		JComponent component = super.getComponent(state, parent);

		for (Iterator iter = ReflectionHelper.fields(ComponentResources.class); iter.hasNext();)
		{
			String field = (String) iter.next();
			Object fieldValue = context.get(field);
			if (fieldValue != null)
				ReflectionHelper.set(component, field, fieldValue);
		}
		return component;
	}
}
