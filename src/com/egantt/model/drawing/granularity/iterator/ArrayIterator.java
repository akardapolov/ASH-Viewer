/*
 * @(#)ArrayIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.granularity.iterator;

import java.util.Iterator;

public class ArrayIterator implements Iterator
{
	protected Object values [];
	protected int position;
	protected int step;

	public ArrayIterator(Object values [], int step)
	{
		this.position = 0;
		this.step = step;
		this.values = values;
	}

	//___________________________________________________________________________

	public boolean hasNext()
	{
		return position < values.length;
	}

	public Object next()
	{
		Object value = values[position];
		this.position += step;
		return value;
	}

	//_not implemented___________________________________________________________

	public void remove()
	{
		// does not make sence for a GranularityIterator
	}
}
