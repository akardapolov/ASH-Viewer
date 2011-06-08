/**
 * @(#)FilledRectanglePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.painter.bounded;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

public class BoundedDiamondPainter implements DrawingPainter
{
	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context)
	{
		bounds = new Rectangle(bounds.x, bounds.y, Math.min(bounds.height, bounds.width), Math.min(bounds.height, bounds.width));
    
		Graphics2D g2d = (Graphics2D) g;
		Paint paint = g2d.getPaint();
		
		int backdropGap = 2;
        if (backdropGap > 0)
        {
        	g.setColor(g.getColor().darker().darker());
        	drawPolygon(g, bounds.x + backdropGap, bounds.y + backdropGap, 
        			bounds.width - ((int)(backdropGap * 1.5)), bounds.height - ((int)(backdropGap * 1.5)));
        }
        
        g2d.setPaint(paint);
        drawPolygon(g, bounds.x, bounds.y, 
    			bounds.width - backdropGap, bounds.height - backdropGap);
        return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds,
		GraphicsContext context)
	{
		return bounds.width;
	}
	
	//	________________________________________________________________________
	
	protected void drawPolygon(Graphics g, int x, int y, int width, int height) {
		
		int xx[]  = new int[4];
		int yy[]  = new int[4];
    
		xx[0] = ((x + width) / 2);
    	xx[1] = x + width;
    	xx[2] = xx[0];
    	xx[3] = x;
    
    	yy[0] = y; 
    	yy[1] = (y + height) / 2;
    	yy[2] = y + height;
    	yy[3] = (y + height) / 2;
    	g.fillPolygon(xx, yy, 4);	
	}
}
