/*
 * @(#)AbstractRangeIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range.iterator;

import com.egantt.drawing.painter.range.RangeIterator;

import java.util.Iterator;

abstract class AbstractRangeIterator implements RangeIterator
{
	protected final long width;
	
	protected transient long lastX = 0;
	protected transient Object lastData;
	protected transient Iterator iter;
	
	public AbstractRangeIterator(Iterator iter, long width)
	{
		this.iter = iter;
		this.width = width;
	}

	// __________________________________________________________________________

	public Object getKey()
	{
	   return lastData;
	}

	// __________________________________________________________________________

	public boolean hasNext()
	{
	   return width >= lastX && iter.hasNext();
	}
}
