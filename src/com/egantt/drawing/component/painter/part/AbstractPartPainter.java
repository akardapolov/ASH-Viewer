/*
 * @(#)AbstractPartPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.component.painter.part;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Iterator;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.component.painter.PartPainter;
import com.egantt.drawing.component.painter.part.view.BasicPartView;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.swing.cell.CellState;

public abstract class AbstractPartPainter implements PartPainter
{
	private DrawingContext context = null;
	
	protected Insets insets;
	protected PartView view = new BasicPartView();

	// _________________________________________________________________________

	/**
	 *  Optional Argument the DrawingContext can be overriden as this point if not
	 *  this will come via the Paint method call.
	 */
	public void setContext(DrawingContext context)
	{
		this.context = context;
	}

	public void setInsets(Insets insets)
	{
		this.insets = insets;
	}

	public void setView(PartView view)
	{
		this.view = view;
	}

	// _________________________________________________________________________

	/**
	 *  Implementation in paintProtected
	 */
	public final void paint(Component c, DrawingPart part, GraphicsState graphics, CellState cellState, DrawingContext context)
	{
		paintProtected(c, part, graphics, cellState, this.context != null ? this.context : context);
	}
	
	//	________________________________________________________________________
	
	protected void paintProtected(Component c, DrawingPart part, GraphicsState graphics, CellState cellState, DrawingContext context)
	{
		final Object [] keys  = part.keys().toArray();
		final AxisInterval intervals [] = new AxisInterval[keys.length];
		final DrawingTransform transforms [] = new DrawingTransform [keys.length];

		DrawingState drawing = cellState.getDrawing();
		for (int i=0; i < keys.length; i++)
		{
			AxisView view = (AxisView) context.get(keys[i], ContextResources.AXIS_VIEW);
			drawing.put(keys[i], view);

			intervals[i] = view.getInterval();
			transforms[i] = view.getTransform();
		}

		for (Iterator iter = part.values(intervals); iter.hasNext();)
		{
			Object key = iter.next();

			Rectangle bounds = view.create(c, part.getInterval(key, intervals), transforms);

			if (bounds == null)
				continue;

			if (!part.isSummaryPart())
				drawing.put(key, bounds);

			Graphics g = insets != null
				? graphics.create(bounds.x + insets.left
				, bounds.y + insets.top, bounds.width + insets.right
				, bounds.height + insets.bottom)
				: graphics.create(bounds.x, bounds.y,
					bounds.width , bounds.height);

			if (g != null)
				paint(key, c, part, cellState, transforms, g, context);
		}
	}

	protected abstract void paint(Object key, Component c, DrawingPart part,
		CellState cellState, DrawingTransform transforms[], Graphics g, DrawingContext context);
}
