/*
 * @(#)DrawingPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public interface DrawingPainter
{
	/**
     * Paints to the Graphics device
     */
	Shape paint(Object key, Graphics g, Rectangle bounds, PainterState state, GraphicsContext context);

	/**
	 *  @@deprecated calculates the width: it's to expensive
	 */
	long width(Object key, Graphics g, Rectangle bounds, GraphicsContext context);
}
