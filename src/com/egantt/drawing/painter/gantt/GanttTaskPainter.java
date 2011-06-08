/*
 * @(#)GanttTaskPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.gantt;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class GanttTaskPainter implements DrawingPainter
{
	//	________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		int height = Math.max(bounds.height, 1) / 2;
		g.fillRect(bounds.x, bounds.y, bounds.width, height);

		// |--.  <-- xPoints[0], yPoints[0] : xPoints[2], yPoints[2]
		// | /
		// |/    <-- xPoints[1], yPoints[1]
		int offset = (int) Math.round(height * 1.8);

		int top = height;
	  	int xPoints [] = new int[3];  int yPoints [] = new int[3];
		xPoints[0] = bounds.x; yPoints[0] = bounds.y + top;
	  	xPoints[1] = bounds.x + (offset /2);  yPoints[1] = bounds.y + height + top;
	  	xPoints[2] = bounds.x + offset; yPoints[2] = bounds.y + top;
		g.fillPolygon(xPoints,yPoints, xPoints.length);

		// as we can draw outside of the boundary x2 should equal width always
		// .--|  <-- xpoints [0] , yPoints[0] : xPoints[2], yPoints[2]
		// \  |
		//  \ |
		//   \|  <-- xPoints [1], yPoints[1]
		xPoints[0] = bounds.x + bounds.width - offset ; yPoints[0] = bounds.y + top;
		xPoints[1] = bounds.x + bounds.width - (offset /2);  yPoints[1] = bounds.y + height + top;
		xPoints[2] = bounds.x + bounds.width; yPoints[2] = bounds.y + top;
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return bounds.width;
	}
}
