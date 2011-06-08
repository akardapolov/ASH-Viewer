/*
 * @(#)BasicIconPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.basic;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;

public class BasicIconPainter implements DrawingPainter
{
	protected Image image;

	public BasicIconPainter(Image image)
	{
		this.image = image;
	}

	//___________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		g.drawImage(image, 0, 0, null); // point of origin x=0, y=0
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return image.getWidth(null);
	}
}
