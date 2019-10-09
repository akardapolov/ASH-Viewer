/*
 * @(#)BasicRangePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.GraphicsState;

import com.egantt.drawing.DrawingPainter;

import com.egantt.drawing.painter.RangePainter;

import com.egantt.model.drawing.StateResources;
import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

/**
  * Relies on the availabiltiy of other plotters to perform the paint
  */
public class BasicRangePainter implements RangePainter
{
	protected final GraphicsManager graphics;
	protected final boolean alwaysPaint;

	protected DrawingPainter painter;
	protected GraphicsState state;
	protected RangeModel model;

	public BasicRangePainter(GraphicsManager graphics, boolean alwaysPaint)
	{
		this.alwaysPaint = alwaysPaint;
		this.graphics = graphics;
	}

	// __________________________________________________________________________

	public void setModel(RangeModel model)
	{
		this.model = model;
	}

	public void setPainter(DrawingPainter painter)
	{
		this.painter = painter;
	}

	public void setState(GraphicsState state)
	{
		this.state = state;
	}

	// __________________________________________________________________________

	/**
	  * This paint essentially uses a fences and posts algorithm, we are given the
	  * the posts from the GranularityHelper but we need to calculate the fences
	  */
	public synchronized Shape paint(Object key, Graphics g, Rectangle bounds,
		 PainterState state, GraphicsContext context)
	{
		// start the drawing
		this.state.initialise(g);

		Graphics vg = this.state.create(bounds.x, bounds.y, bounds.width, bounds.height);

		for (RangeIterator iter = model.iterator(key, g, bounds, state, context); iter.hasNext();)
		{
			Rectangle rect = (Rectangle) iter.next();
			if (rect == null)
				continue;

			Object data = iter.getKey();
			if (iter.getKey() == null)
				continue; // :: hack !WHY!

			vg = graphics.create(data, vg, context);

			Object gran = state.get(StateResources.GRANULARITY_KEY);
			GraphicsContext granularityContext = model.getContext(gran, context);
			
			// don't plot plotters, that aren't really plottable
			if (alwaysPaint || painter.width(data, vg, rect, granularityContext) <= rect.width)
			{
				painter.paint(data, vg, rect, state, granularityContext);
			}
		}

		this.state.terminate(g);

		return bounds;
	}

	public long width(Object data, Object granularity, Graphics vg, Rectangle rect, GraphicsContext context)
	{
		vg = graphics.create(data, vg, context);
		return painter.width(data, vg, rect, model.getContext(granularity, context));
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context) {
		return bounds.width;
	}
}
