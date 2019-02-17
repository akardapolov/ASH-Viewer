/*
 * @(#)BasicRectanglePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.basic;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public class BasicRectanglePainter implements DrawingPainter
{
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds,
		GraphicsContext context)
	{
		BasicStroke stroke = (BasicStroke) ((Graphics2D)g).getStroke();
		return (long) stroke.getLineWidth() *2;
	}
}
