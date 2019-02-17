/*
 * @(#)LineCalendarModule.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.drawing.module;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;

import com.egantt.awt.graphics.state.GraphicsState2D;

import com.egantt.drawing.DrawingPainter;

import com.egantt.drawing.painter.RangePainter;

import com.egantt.drawing.painter.basic.BasicRectanglePainter;
import com.egantt.drawing.painter.range.BasicRangePainter;

import com.egantt.drawing.painter.range.model.GranularityRangeModel;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import com.egantt.model.drawing.painter.state.BasicPainterState;

import ext.egantt.drawing.DrawingModule;
import ext.egantt.drawing.painter.context.BasicPainterContext;

import ext.egantt.model.drawing.granularity.CachedCalendarGranularity;
import ext.egantt.model.drawing.granularity.CalendarConstants;

import java.awt.BasicStroke;
import java.awt.Color;

public class LineCalendarModule implements DrawingModule
{
	protected final CachedCalendarGranularity granularity;

	protected final String key;

	protected GranularityRangeModel model;
	protected GraphicsManager graphics;

	public LineCalendarModule(String key, GranularityRangeModel model)
	{
		this.key = key;
		this.model = model;
		this.granularity = new CachedCalendarGranularity(1, CalendarConstants.FORMAT_KEYS);
	}

	// __________________________________________________________________________

	public void setGraphics(GraphicsManager graphics)
	{
		this.graphics = graphics;
	}

	public void setRangeModel(GranularityRangeModel model)
	{
		this.model = model;
	}

	// __________________________________________________________________________

	public void initialise(DrawingContext context)
	{
		context.put(key, ContextResources.GRAPHICS_CONTEXT, createContext());
		context.put(key, ContextResources.DRAWING_PAINTER, createPainter());
		context.put(key, ContextResources.PAINTER_STATE, new BasicPainterState());
	}

	public void terminate(DrawingContext context)
	{
		context.put(key, ContextResources.GRAPHICS_CONTEXT, null);
		context.put(key, ContextResources.DRAWING_PAINTER, null);
		context.put(key, ContextResources.PAINTER_STATE, null);
	}

	// __________________________________________________________________________

	protected GraphicsContext createContext()
	{
		BasicPainterContext context = new BasicPainterContext();
		context.setDrawingGranularity(granularity);
		context.setPaint(Color.black);
		context.setStroke(new BasicStroke(0));
		return context;
	}

	protected DrawingPainter createPainter()
	{
		RangePainter painter = new BasicRangePainter(graphics, true);
		painter.setModel(model);
		painter.setPainter(new BasicRectanglePainter());
		painter.setState(new GraphicsState2D());
		return painter;
	}
}
