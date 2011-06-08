/*
 * @(#)GranularityRangeModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range.model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.context.CompoundContext;
import com.egantt.awt.graphics.context.compound.SubCompoundContext;
import com.egantt.drawing.painter.range.RangeIterator;
import com.egantt.drawing.painter.range.RangeModel;
import com.egantt.drawing.painter.range.RangeTransform;
import com.egantt.drawing.painter.range.iterator.BasicRangeIterator;
import com.egantt.drawing.painter.range.transform.CompoundRangeTransform;
import com.egantt.model.drawing.DrawingGranularity;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.StateResources;
import com.egantt.model.drawing.painter.PainterResources;
import com.egantt.model.drawing.painter.PainterState;

/**
 * This is an OffScreen plotter, unlike other plotters it attempts to calculate
 * the required granularity for a plotter by painting in a virtual buffer and then
 * guessing the range
 */
public abstract class GranularityRangeModel implements RangeModel
{
	protected final int axisKey;
	protected final int offset;

	public GranularityRangeModel(int axisKey, int offset)
	{
		this.axisKey = axisKey;
		this.offset = Math.abs(offset);
	}

	// _________________________________________________________________________

	public GraphicsContext getContext(Object gran, GraphicsContext context) {
		return gran != null && context instanceof CompoundContext
			? new SubCompoundContext(gran, (CompoundContext) context) : context;
	}

	// _________________________________________________________________________

	public RangeIterator iterator(Object key, Graphics g, Rectangle bounds,
			PainterState state, GraphicsContext context)
	{
		DrawingGranularity granularity = (DrawingGranularity)
			context.get(key, PainterResources.GRANULARITY);

		RangeTransform transform = new CompoundRangeTransform(axisKey,
																 (DrawingTransform []) state.get(StateResources.TRANSFORMS));

		Object gran = state.get(StateResources.GRANULARITY_KEY);
		gran = gran != null ? gran :
			calculateGranularity(granularity, g, state, transform, bounds);

		return iterator(gran, granularity, transform, bounds);
	}

	// __________________________________________________________________________

	protected Object calculateGranularity(DrawingGranularity granularity,
			Graphics g, PainterState state, RangeTransform transform, Rectangle bounds)
	{
		ArrayList <Object> granularities = new ArrayList<Object>();
		Rectangle rect = new Rectangle(); // try to cache the rect's they are to expensive

		for (Iterator iter = granularity.keys(); iter.hasNext();)
		{
			Object gran = iter.next();
	
			granularities.add(gran);

			rect.setBounds(0,0, (int)
					granularity.width(gran, transform, bounds.width),bounds.height);

			if (!interateGranularities(granularity.values(gran, transform, bounds.width), gran, g, rect))
			{
				Object lastGran = granularities.get(Math.max(granularities.size() - offset -1, 0));
				state.put(StateResources.GRANULARITY_KEY, lastGran);
				return lastGran;
			}
		}
		return null;
	}
	
	protected boolean  interateGranularities(Iterator iter, Object gran, Graphics g, Rectangle rect) {
		while (iter.hasNext())
		{
			if (accepts(iter.next(), gran,  g, rect))
				return true;
		}
		return false;
	}

	// __________________________________________________________________________

	protected abstract boolean accepts(Object key, Object gran, Graphics g, Rectangle bounds);


	// __________________________________________________________________________

	protected RangeIterator iterator(Object value, DrawingGranularity granularity,
			RangeTransform transform, Rectangle bounds)
	{
		Iterator inner = value != null
			? granularity.values(value, transform, bounds.width)
			: Collections.EMPTY_SET.iterator();
		return new BasicRangeIterator(inner, transform, bounds.width, bounds.height);
	}
}
