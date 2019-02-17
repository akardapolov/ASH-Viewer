/*
 * @(#)DoubleIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.granularity.iterator;

import java.util.Iterator;

public class DoubleIterator implements Iterator
{
	protected double finish;
	protected double position;
	protected double step;

	public DoubleIterator(double start, double finish, double step)
	{
		this.finish = finish;
		this.position = start;
		this.step = step;
	}

	//___________________________________________________________________________

	public boolean hasNext()
	{
		return position < finish;
	}

	public Object next()
	{
		Double value = new Double(position);
		this.position +=  step;
		return value;
	}

	//_not implemented___________________________________________________________

	public void remove()
	{
		// does not make sence for a GranularityIterator
	}
}
