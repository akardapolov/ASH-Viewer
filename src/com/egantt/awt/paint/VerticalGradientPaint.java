package com.egantt.awt.paint;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.awt.image.ColorModel;

public class VerticalGradientPaint extends AbstractGradientPaint
{
	public VerticalGradientPaint(Color color1, Color color2)
	{
		super (color1, color2);
	}
	
	// _________________________________________________________________________
	
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
		Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
	{
		Rectangle bounds = userBounds instanceof Rectangle
			? (Rectangle) userBounds : userBounds.getBounds();

		// create the paint object
		java.awt.GradientPaint gp =  new java.awt.GradientPaint(bounds.x, bounds.y,
		   color1, bounds.x, bounds.y + bounds.height, color2, cyclic);

		return gp.createContext(cm, deviceBounds, userBounds, xform, hints);
   }
}
