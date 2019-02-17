/*
 * @(#)TextCalendarModule.java
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

import com.egantt.drawing.painter.format.BasicFormatPainter;

import com.egantt.drawing.painter.range.BasicRangePainter;

import com.egantt.drawing.painter.range.model.GranularityRangeModel;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingGranularity;

import com.egantt.model.drawing.painter.state.BasicPainterState;

import ext.egantt.drawing.painter.context.compound.BasicCompoundContext;

import ext.egantt.model.drawing.granularity.CachedCalendarGranularity;
import ext.egantt.model.drawing.granularity.CalendarConstants;

import java.awt.Color;
import java.awt.Font;

import java.text.SimpleDateFormat;

import java.util.Calendar;

public class TextCalendarModule
{
	private static final Font font = new Font("SanSerif", Font.PLAIN, 9);

	protected final DrawingGranularity granularity;

	protected final boolean formatType;
	protected final String key;

	protected GranularityRangeModel model;
	protected GraphicsManager graphics;

	public TextCalendarModule(String key, GranularityRangeModel model, boolean formatType)
	{
		this.formatType = formatType;
		this.model = model;
		this.key = key;

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
		BasicCompoundContext context = new BasicCompoundContext();
		context.setDrawingGranularity(granularity);
		context.setFont(font);
		context.setPaint(Color.black);

		// hard coded, as this demo code does not have access to bundles
		context.setFormat(new Integer(Calendar.SECOND),
			new SimpleDateFormat(" hh:mm:ss"));
	   context.setFormat(new Integer(Calendar.MINUTE),
			new SimpleDateFormat(formatType ? " hh:mm:'xx'" : " hh:mm:'xx' dd MMM yyyy"));
		context.setFormat(new Integer(Calendar.HOUR),
			new SimpleDateFormat(formatType ? " hh:'xx'" : " hh:'xx' dd MMM yyyy"));
		context.setFormat(new Integer(Calendar.DAY_OF_MONTH),
			new SimpleDateFormat(formatType ? " E dd/m/yy" : " dd MMMM yyyy"));
		context.setFormat(new Integer(Calendar.WEEK_OF_MONTH),
			new SimpleDateFormat(formatType ? "MMM" : " ww MM/yy"));
		context.setFormat(new Integer(Calendar.WEEK_OF_YEAR),
			new SimpleDateFormat(formatType ? "M" : "M yyyy"));
		context.setFormat(new Integer(Calendar.MONTH),
      	new SimpleDateFormat(formatType ? " MMM" : " MMMM yyyy"));
		context.setFormat(new Integer(Calendar.YEAR),
			new SimpleDateFormat(" yyyy"));
		return context;
	}

	protected DrawingPainter createPainter()
	{
		RangePainter painter = new BasicRangePainter(graphics, false);
		painter.setModel(model);
		painter.setPainter(new BasicFormatPainter());
		painter.setState(new GraphicsState2D());
		return painter;
	}
}
