/*
 * @(#)AbstractGraphicsManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.manager;

import com.egantt.awt.graphics.GraphicsManager;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.AxisInterval;

import java.awt.Component;
import java.awt.Rectangle;

/**
  * Useful for generating Graphics2D's
  */
public abstract class AbstractGraphicsManager implements GraphicsManager
{
	// _________________________________________________________________________

	public Rectangle create(Component c, AxisInterval intervals [],
		DrawingTransform transforms[])
	{
		int points[] = new int [2];
		points[0]= c.getWidth();
		points[1]= c.getHeight();

		int r[] = new int [points.length * 2];
		for (int i=0; i < points.length; i++)
		{
			r[i *2] = transforms[i].transform(intervals[i].getStart(), points[i]);
			r[i *2+1] = transforms[i].transform(intervals[i].getFinish(), points[i]);
		}

		int width = r[1] - r[0];
		int height = r[3] - r[2];
		return width > 0 && height > 0 ? new Rectangle(r[0], r[2], width, height) : null;
	}
}
