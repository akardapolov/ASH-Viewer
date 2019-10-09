/*
 * @(#)ReflectionGraphicsManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.manager;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsResources;

import ext.egantt.util.reflect.ReflectionHelper;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Iterator;

/**
 *  Should avoid using this one, as the disadvantages are it's quite slow
 *  and it will break under applets because reflection is not allowed
 */
public class ReflectionGraphicsManager extends AbstractGraphicsManager
{
	// __________________________________________________________________________

	public Graphics create(Object key, Graphics g, GraphicsContext context)
	{
		Graphics2D g2d = (Graphics2D) g;

		for (Iterator iter = ReflectionHelper.fields(GraphicsResources.class); iter.hasNext();)
		{
			String field = (String) iter.next();
			Object value = context.get(key, field);
			if (value != null)
				ReflectionHelper.set(g2d, field, value);
		}
		return g;
	}
}
