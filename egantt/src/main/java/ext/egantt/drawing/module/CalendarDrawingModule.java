/**
 * @(#)CalendarDrawingModule.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.drawing.module;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.manager.BasicGraphicsManager;
import com.egantt.drawing.component.painter.PartPainter;
import com.egantt.drawing.component.painter.part.BasicPartPainter;
import com.egantt.drawing.component.painter.part.PartView;
import com.egantt.drawing.component.painter.part.view.BasicPartView;
import com.egantt.drawing.painter.RangePainter;
import com.egantt.drawing.painter.range.model.GranularityRangeModel;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.axis.view.ViewResources;

import ext.egantt.drawing.DrawingModule;
import ext.egantt.drawing.module.LineCalendarModule;
import ext.egantt.drawing.module.TextCalendarModule;

public class CalendarDrawingModule implements DrawingModule
{
	public static String TIMELINE_BOTTOM = "TimelineBottom";
	public static String TIMELINE_TOP = "TimelineTop";

	public static String LINE_PAINTER = "-line";
	public static String TEXT_PAINTER = "-text";

	protected final int orientation = ViewResources.HORIZONTAL.intValue();

	protected final GraphicsManager graphics;

	public CalendarDrawingModule()
	{
		this.graphics = new BasicGraphicsManager();
	}
	
	// _________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
		loadTextModule(TIMELINE_TOP, +2, false, attributes);
		loadLineModule(TIMELINE_TOP, +2 , true, attributes);

		loadTextModule(TIMELINE_BOTTOM, +1, true, attributes);
		loadLineModule(TIMELINE_BOTTOM, +1 , true, attributes);
		
	}

	public void terminate(DrawingContext attributes) {
	}
	
	// _________________________________________________________________________

	protected PartPainter createPainter(PartView view, GraphicsManager graphics)
	{
		BasicPartPainter painter = new BasicPartPainter();
		return painter;
	}

	// _________________________________________________________________________

	protected void loadTextModule(String key, int offset, boolean value, DrawingContext attributes)
	{
		GranularityRangeModel model = new LocalGranularityRangeModel(orientation, offset, attributes);

		TextCalendarModule module = new TextCalendarModule(key + TEXT_PAINTER, model, value);
		module.setGraphics(graphics);
		module.initialise(attributes);

		PartView view = new BasicPartView();
		attributes.put(key + TEXT_PAINTER, ContextResources.PART_PAINTER,
			createPainter(view, graphics));
	}

	protected void loadLineModule(String key, int offset, boolean value, DrawingContext attributes)
	{
		GranularityRangeModel model = new LocalGranularityRangeModel(orientation, offset, attributes);

		LineCalendarModule module = new LineCalendarModule(key + LINE_PAINTER, model);
		module.setGraphics(graphics);
		module.initialise(attributes);

		PartView view = new BasicPartView();
		attributes.put(key + LINE_PAINTER, ContextResources.PART_PAINTER, createPainter(view, graphics));
	}

	// __________________________________________________________________________

	protected final class LocalGranularityRangeModel extends GranularityRangeModel
	{
		final DrawingContext attributes;
		public LocalGranularityRangeModel(int axisKey, int offset, DrawingContext attributes)
		{
			super (axisKey, offset);
			this.attributes = attributes;
		}

		protected boolean accepts(Object key, Object gran, Graphics g, Rectangle bounds)
		{
			if (bounds.width == 0)
				return false;
			
			RangePainter partPainter = (RangePainter) attributes.get(TIMELINE_TOP + TEXT_PAINTER, ContextResources.DRAWING_PAINTER);
			GraphicsContext context = (GraphicsContext) attributes.get(TIMELINE_TOP + TEXT_PAINTER, ContextResources.GRAPHICS_CONTEXT);
			long width = partPainter.width(key, gran, g, bounds, context);
			return width <= bounds.width;
		}
	}
}