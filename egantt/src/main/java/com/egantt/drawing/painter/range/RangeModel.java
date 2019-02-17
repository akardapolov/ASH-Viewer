/*
 * @(#)RangeModel.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.model.drawing.painter.PainterState;

public interface RangeModel
{
	/**
	 *  returns an apropriate proxy based on the existing proxy
	 *  passing in a proxy to do this is less than ideal
	 */	
	GraphicsContext getContext(Object gran, GraphicsContext context);

	// _________________________________________________________________________

	/**
	  * This converts a one dimensional transform into a useable granularity for this class
	  */
	RangeIterator iterator(Object key, Graphics g, Rectangle bounds,
		PainterState state, GraphicsContext context);
}
