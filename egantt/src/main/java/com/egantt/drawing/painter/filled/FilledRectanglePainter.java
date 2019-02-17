/*
 * @(#)FilledRectanglePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.filled;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class FilledRectanglePainter implements DrawingPainter
{
	//	________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.draw3DRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, true);
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds,
		GraphicsContext context)
	{
		return bounds.width;
	}
}
