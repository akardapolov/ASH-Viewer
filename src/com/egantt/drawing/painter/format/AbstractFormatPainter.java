/*
 * @(#)AbstractFormatPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.format;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.LineMetrics;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.drawing.DrawingPainter;
import com.egantt.model.drawing.painter.PainterState;

public abstract class AbstractFormatPainter implements DrawingPainter
{
	// _________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		String value = getValue(key, context);

		//FontMetrics fm = g.getFontMetrics();
		LineMetrics metrics = g.getFontMetrics().getLineMetrics(value, g);

		//Rectangle2D rect = fm.getStringBounds(value, g);

		g.drawString(getValue(key, context), bounds.x, bounds.y + (int) metrics.getAscent());
		return bounds; // to do use the line metrics
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		FontMetrics metrics = g.getFontMetrics();
		return metrics.stringWidth(getValue(key, context));
	}

	// __________________________________________________________________________

	protected abstract String getValue(Object key, GraphicsContext context);
}
