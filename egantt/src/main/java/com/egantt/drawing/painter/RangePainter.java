/*
 * @(#)RangePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.egantt.drawing.DrawingPainter;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsState;

import com.egantt.drawing.painter.range.RangeModel;

public interface RangePainter extends DrawingPainter
{
	void setModel(RangeModel model);

	void setPainter(DrawingPainter plotter);

	void setState(GraphicsState state);
	
	//	________________________________________________________________________
	
	public long width(Object data, Object granularity, Graphics vg, Rectangle rect, GraphicsContext context);
}
