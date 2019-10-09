/**
 * @(#)AxisPercentagePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.painter.axis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.drawing.DrawingPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.StateResources;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.painter.PainterState;
import com.egantt.util.Trace;

public class AxisPercentagePainter implements DrawingPainter
{
	private static final boolean trace = Trace.getEnable(AxisPercentagePainter.class.getName());
	public static final String PROGRESS_PAINT = "progressPaint";
	
	protected final String axis;
	
	public AxisPercentagePainter(String axis) {
		this.axis = axis;
	}
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{	
		AxisView view = (AxisView) context.get(axis, ContextResources.AXIS_VIEW);
		if (view == null)
		{
			if (trace) Trace.out.println("AxisPercentagePainter - paint: Axis not defined in context [" + axis + "]");
			// Axis not defined
			return bounds; 
		}
		
		DrawingPart part = (DrawingPart) state.get(StateResources.DRAWING_PART);
		if (part == null  || part.keys().indexOf(axis) < 0)
		{
			if (trace) Trace.out.println("AxisPercentagePainter - paint: Axis not defined for DrawingPart axis [" + axis + "]");
			return bounds; 
		}
		
		final int axisIndex = part.keys().indexOf(axis);
		
		AxisInterval interval = part.getInterval()[axisIndex];
		if (interval == null)  {
			if (trace) Trace.out.println("AxisPercentagePainter - paint: interval is null for DrawingPart axis [" + axis + "]");
			return bounds; 
		}
		
		// draw the outer rectangle
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.draw3DRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, true);
		
		Color fillColor = (Color) context.get(key, AxisPercentagePainter.PROGRESS_PAINT);
		fillColor = fillColor != null ? fillColor : Color.BLACK;
		
		int xOffset = Math.min(2, bounds.width - (2 * 2));
		int yOffset = new Double(bounds.height * 0.30).intValue();
		
		{
			g.setColor(fillColor);
			DrawingTransform transform = view.getAxis().getTransform();
			int width = transform.transform(interval.getFinish(), bounds.width) -  transform.transform(interval.getStart(), bounds.width); 
			//	draw the outer rectangle
			g.fillRect(bounds.x + xOffset, bounds.y + yOffset, 
					width - (xOffset * 2) , bounds.height - (yOffset *2));
		}
	
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return bounds.width;
	}
}