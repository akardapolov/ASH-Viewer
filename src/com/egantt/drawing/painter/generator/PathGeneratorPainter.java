/*
 * @(#)PathGeneratorPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.generator;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.StateResources;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import java.awt.geom.GeneralPath;

public class PathGeneratorPainter implements DrawingPainter
{
	protected final int rule;

	protected transient int capacity;

	public PathGeneratorPainter(int rule, int capacity)
	{
		this.capacity = capacity;
		this.rule = rule;
	}

	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		DrawingTransform [] transforms  = (DrawingTransform [])
			state.get(StateResources.TRANSFORMS);

		GeneralPath path = (GeneralPath) state.get(StateResources.SHAPE);
		if (path == null)
		{
			path = new GeneralPath(rule, capacity);
			state.put(StateResources.SHAPE, path);
		}

		Object [] keys = (Object []) key;
		int [] points = new int[keys.length];

		for (int i= 0; i < keys.length; i++)
			points[i] = transforms[i % 2].transform(keys[i], i % 2 == 0 ? bounds.width : bounds.height);

		path.moveTo(points[0], points[1]);
		for (int i=0; i < keys.length -1; i = i + 2)
			path.lineTo(points[i], points[i+1]);

		return path.getBounds();
	}

	public long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return g.getFontMetrics().stringWidth((String) key);
	}
}
