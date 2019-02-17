/*
 * @(#)BasicGraphicsManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.manager;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsResources;

import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

import java.awt.geom.AffineTransform;

/**
 *  This is used for applets instead of the other mechanism as applets can
 *  and must not use reflection on components that potentially have methods
 *  in the sun.* packages unfortunatly this applies to graphics
 */
public class BasicGraphicsManager extends AbstractGraphicsManager
{
	public Graphics create(Object key, Graphics g, GraphicsContext context)
	{
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform transform = (AffineTransform) context.get(key, GraphicsResources.TRANSFORM);
		if (transform != null)
		   g2d.setTransform(transform);

		Composite composite = (Composite) context.get(key, GraphicsResources.COMPOSITE);
		if (composite != null)
			g2d.setComposite(composite);

		Font font = (Font) context.get(key, GraphicsResources.FONT);
		if (font != null)
		   g2d.setFont(font);

		Paint paint = (Paint) context.get(key, GraphicsResources.PAINT);
		if (paint != null)
			g2d.setPaint(paint);

		Stroke stroke = (Stroke) context.get(key, GraphicsResources.STROKE);
		if (stroke != null)
		   g2d.setStroke(stroke);
		return g;
	}
}
