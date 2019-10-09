/*
 * @(#)AbstractGradientPaint.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.paint;

import com.egantt.drawing.DrawingPaint;

import java.awt.Color;

public abstract class AbstractGradientPaint implements DrawingPaint
{
	protected final Color color1;
	protected final Color color2;
	
	protected boolean cyclic;
	
	public AbstractGradientPaint(Color color1, Color color2)
	{
		this.color1 = color1;
		this.color2 = color2;
	}

	// _________________________________________________________________________
	
	public void setCyclic(boolean cyclic)
	{
		this.cyclic = cyclic;
	}
	
   // __________________________________________________________________________
   
	/**
     * Returns the transparency mode for this <code>GradientPaint</code>.
     * @return an integer value representing this <code>GradientPaint</code>
     * object's transparency mode.
     * @see Transparency
     */
	public int getTransparency()
	{
		return (((color1.getAlpha() & color2.getAlpha()) == 0xff) ? OPAQUE : TRANSLUCENT);
	}
}
