/*
 * @(#)GanttTaskPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.gantt;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.interval.DrawingStateInterval;
import com.egantt.model.drawing.painter.PainterState;

public class LinkTaskPainter extends AbstractLinkTaskPainter
{
	@Override
	protected void paintStart(DrawingStateInterval interval, AxisInterval localInterval, AxisView view, Object key, Graphics g, Rectangle bounds, PainterState state, GraphicsContext context) {
		int x = view.getTransform().transform(localInterval.getFinish(), bounds.width);
		int x2 = view.getTransform().transform(interval.getFinish(), bounds.width);
		
		int xOffset = bounds.height / 6;
		g.fillRect(Math.min(x, x2), bounds.height / 3, bounds.height /3, bounds.height / 3);
		g.drawLine(Math.min(x, x2), bounds.height / 2, Math.max(x, x2) + xOffset, bounds.height / 2);
		g.drawLine(Math.max(x2, x) + xOffset, bounds.height /2, Math.max(x2, x) + xOffset, bounds.height);
	}

	@Override
	protected void paintOther(DrawingStateInterval interval, AxisView view, Object key, Graphics g, Rectangle bounds, PainterState state, GraphicsContext context) {
		
		int xOffset = bounds.height / 6;
		int x2 = view.getTransform().transform(interval.getFinish(), bounds.width);
		g.drawLine(x2 + xOffset, bounds.y, x2 + xOffset , bounds.height);
	}

	@Override
	protected void paintFinish(DrawingStateInterval interval, AxisInterval localInterval, AxisView view, Object key, Graphics g, Rectangle bounds, PainterState state, GraphicsContext context) {
		int x = view.getTransform().transform(localInterval.getFinish(), bounds.width);
		int x2 = view.getTransform().transform(interval.getFinish(), bounds.width);
		int xOffset = bounds.height / 6;
		g.drawLine(Math.min(x, x2), bounds.height / 2, Math.max(x, x2) + xOffset, bounds.height / 2);
		g.drawLine(Math.max(x2, x) + xOffset, bounds.y, Math.max(x2, x) + xOffset, bounds.height / 2);
	}
}
