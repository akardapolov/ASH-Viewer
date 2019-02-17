/**
 * @(#)BoundedBoxPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.painter.bounded;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.drawing.DrawingPainter;
import com.egantt.model.drawing.painter.PainterState;

public class BoundedBoxPainter implements DrawingPainter
{
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		bounds = new Rectangle(bounds.x, bounds.y, Math.min(bounds.height, bounds.width), Math.min(bounds.height, bounds.width));
        
		int shadowOffset = 1;

		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height - shadowOffset;
		int width = bounds.height - shadowOffset;
		Graphics2D g2d = (Graphics2D) g;
		Paint paint = g2d.getPaint();
        
        if (shadowOffset > 0 ){
            g.setColor(g.getColor().darker().darker());
            g.fillRect(x + shadowOffset, y + shadowOffset, width, height);
        }
        
        {
            g2d.setPaint(paint);
            g.fillRect(x, y, width, height);
        }
		return bounds;
	}
	
	//	________________________________________________________________________

	public long width(Object key, Graphics g, Rectangle bounds,
		GraphicsContext context)
	{
		return Math.min(bounds.width, bounds.height);
	}
}
