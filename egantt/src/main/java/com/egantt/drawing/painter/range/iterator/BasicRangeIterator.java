/*
 * @(#)BasicRangeIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range.iterator;

import java.awt.Rectangle;
import java.util.Iterator;

import com.egantt.drawing.painter.range.RangeTransform;

public class BasicRangeIterator extends AbstractRangeIterator
{
	protected final RangeTransform transform;
	protected final long height;

	protected transient Object data;

	public BasicRangeIterator(Iterator iter, RangeTransform transform, long width, long height)
	{
		super (iter, width);

	   this.transform = transform;
	   this.height = height;
	}

	// _________________________________________________________________________

	public Object next() {
		return next(0);
	}
	public Object next(int recursionCount)
	{
	   this.lastData = data;
	   this.data = iter.next();

		long x2 = transform.transform(data, (long) width);

	   int width = (int) x2 - (int) lastX;
	   if (width <= 0)
	   {
			return iter.hasNext() && recursionCount < 10 ? next(++recursionCount) : null; // not much we can do with first object
	   }

	   Rectangle rect = new Rectangle((int)lastX, 0, width, (int)height);

	   this.lastX = x2;
	   return rect;
	}

	// _________________________________________________________________________
	
	public void remove()
	{
	}
}
