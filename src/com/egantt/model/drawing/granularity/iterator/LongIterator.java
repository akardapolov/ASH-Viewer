/*
 * @(#)LongIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.granularity.iterator;

import java.util.Iterator;

public class LongIterator implements Iterator
{
	protected long finish;
	protected long position;
	protected long step;

	public LongIterator(long start, long finish, long step)
	{
		this.finish = finish;
		this.position = start;
		this.step = step;
	}

	//___________________________________________________________________________

	public boolean hasNext()
	{
		return position + step < finish;
	}

	public Object next()
	{
		Long value = new Long(position);
		this.position +=  step;
		return value;
	}

	//_not implemented___________________________________________________________

	public void remove()
	{
		// does not make sence for a GranularityIterator
	}
}
