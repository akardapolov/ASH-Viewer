/*
 * @(#)DoubleInterval.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.interval;

import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.MutableInterval;
import com.egantt.model.drawing.transform.DoubleTransform;

public class DoubleInterval implements MutableInterval
{
	protected double start;
	protected double finish;

	public DoubleInterval(double start, double finish)
	{
		this.start = start;
		this.finish = finish;
	}

	//___________________________________________________________________________
	
	public void setStart(Object start)
	{
		this.start = ((Double)start).doubleValue();
	}

	public void setFinish(Object finish)
	{
		this.finish = ((Double)finish).doubleValue();
	}
	
	//___________________________________________________________________________

	public Object getStart()
	{
		return new Double(start);
	}

	public Object getFinish()
	{
		return new Double(finish);
	}

	//___________________________________________________________________________

	public Object getRange()
	{
		return new Double(finish - start);
	}

	//___________________________________________________________________________

	public double getStartValue()
	{
		return start;
	}

	public double getFinishValue()
	{
		return finish;
	}

	//___________________________________________________________________________

	public double getRangeValue()
	{
		return finish - start;
	}

	// __________________________________________________________________________
	
	public boolean containsValue(Object o)
	{
		if (o == null || !(o instanceof Double))
			return false;
		
		double value = ((Double) o).doubleValue();
		return this.start <= value  && this.finish >= value;
	}

	public boolean contains(AxisInterval i)
	{
		if (i == null || !(i instanceof DoubleInterval))
			return false;
		
		DoubleInterval interval = (DoubleInterval) i;
		return this.start <= interval.getStartValue() &&
		   this.finish >= interval.getFinishValue();
	}

	public boolean intersects(AxisInterval i)
	{
		if (i == null || !(i instanceof DoubleInterval))
			return false;
		
		DoubleInterval interval = (DoubleInterval) i;
		return ! (finish < interval.getStartValue())
		   || ! (start > interval.getFinishValue());
	}
	
	//	________________________________________________________________________
	
	public AxisInterval union(AxisInterval i) {
		if (i == null)
			return new DoubleInterval(new Double(getStartValue()), new Double(getFinishValue()));
	
		if (!(i instanceof DoubleInterval))
			return null;
		
		DoubleInterval interval = (DoubleInterval) i;
		return new DoubleInterval(
				Math.min(getStartValue(), interval.getStartValue()), 
				Math.max(getFinishValue(), interval.getFinishValue()));
	}
	
	//	________________________________________________________________________
	
	public Object clone() {
		return new DoubleInterval(getStartValue(), getFinishValue());
	}

	public DrawingTransform getTransform() {
		return new DoubleTransform(getStartValue(), getFinishValue() - getStartValue());
	}
}
