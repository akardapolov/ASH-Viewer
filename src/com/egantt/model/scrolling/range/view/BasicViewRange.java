/*
 * @(#)BasicViewRange.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.scrolling.range.view;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.AxisInterval;

public class BasicViewRange extends AbstractViewRange
{
	protected int range;
	
	public BasicViewRange(int range)
	{
		this.range = range;
	}
	
	public BasicViewRange()
	{
		this (Integer.MAX_VALUE);
	}
	
	// __________________________________________________________________________
	
	public int getRange()
	{
		return range;
	}
	
	// __________________________________________________________________________

	public int getExtent()
	{
		AxisInterval interval = manager.getView().getInterval();
		DrawingTransform transform = manager.getAxis().getTransform();
		
		int x1 = transform.transform(interval.getStart(), range);
		int x2 = transform.transform(interval.getFinish(), range);
		return x2 - x1;
	}
	
	
	public int getValue()
	{
		AxisInterval interval = manager.getView().getInterval();
		DrawingTransform transform = manager.getAxis().getTransform();
		return transform.transform(interval.getStart(), range);
	}

	//___________________________________________________________________________

	public void setValue(int value)
	{
		DrawingTransform transform = manager.getAxis().getTransform();
		manager.setStart(transform.inverseTransform(value, range));
   }

	public int getBlockIncrement() {
		return getExtent() / 100;
	}

	public int getUnitIncrement() {
		AxisInterval interval = manager.getView().getInterval();
		DrawingTransform transform = manager.getAxis().getTransform();
		int start = transform.transform(interval.getStart(), range);
		int finish = transform.transform(interval.getFinish(), range);
		return finish - start;
	}
}
