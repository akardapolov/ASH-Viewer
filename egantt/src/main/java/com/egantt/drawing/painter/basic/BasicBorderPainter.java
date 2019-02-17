/*
 * @(#)BasicBorderPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.basic;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.StateResources;

import com.egantt.model.drawing.painter.PainterResources;
import com.egantt.model.drawing.painter.PainterState;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.border.Border;

public class BasicBorderPainter implements DrawingPainter
{
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		Border border = (Border) context.get(key, PainterResources.BORDER);
		Component c = (Component) state.get(StateResources.COMPONENT);

		border.paintBorder(c, g, bounds.x, bounds.y, bounds.width, bounds.height);
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return bounds.width;
	}
}
