/**
 * @(#)BoundedOvalPainter.java
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

public class BoundedOvalPainter implements DrawingPainter
{
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		bounds = new Rectangle(bounds.x, bounds.y, Math.min(bounds.height, bounds.width), Math.min(bounds.height, bounds.width));

		int backgroundDropGap = 1;
    
        
        Graphics2D g2d = (Graphics2D) g;
        Paint paint = g2d.getPaint();
        
        if (backgroundDropGap > 0)
        {
        	g.setColor(g2d.getColor().darker().darker());
               
        	g.fillOval(bounds.x + backgroundDropGap, bounds.y + backgroundDropGap, 
        			bounds.width - backgroundDropGap, bounds.height - backgroundDropGap);
        }
        
        {
        	g2d.setPaint(paint);
        	g.fillOval(bounds.x, bounds.y, 
        			bounds.width - backgroundDropGap, bounds.height - backgroundDropGap);
        }
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds,
		GraphicsContext context)
	{
		return Math.min(bounds.width, bounds.height);
	}
}
