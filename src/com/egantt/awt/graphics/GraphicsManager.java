/*
 * @(#)GraphicsManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics;

import com.egantt.awt.graphics.GraphicsContext;

import java.awt.Graphics;

/**
  * <code>DrawingGraphics</code> is used to convert the <code>Proxy</code>,
  * <code>Transform</code> and Intervals into a <code>Graphics</code> object
  */
public interface GraphicsManager
{
	Graphics create(Object key, Graphics g, GraphicsContext context);
}

