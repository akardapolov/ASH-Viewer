/*
 * @(#)LongInterval.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.interval;

import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.MutableInterval;
import com.egantt.model.drawing.transform.LongTransform;

public class LongInterval implements MutableInterval
{
	protected long start;
	protected long finish;

	public LongInterval(long start, long finish)
	{
		this.start = start;
		this.finish = finish;
	}

	//___________________________________________________________________________
	
	public void setStart(Object start)
	{
		this.start = ((Long)start).longValue();
	}
	
	public void setFinish(Object finish)
	{
		this.finish = ((Long)finish).longValue();
	}
	
	//___________________________________________________________________________

	public Object getStart()
	{
		return new Long(start);
	}

	public Object getFinish()
	{
		return new Long(finish);
	}

	//___________________________________________________________________________

	public Object getRange()
	{
		return new Long(finish - start);
	}

	//___________________________________________________________________________

	public long getStartValue()
	{
		return start;
	}

	public long getFinishValue()
	{
		return finish;
	}

	//___________________________________________________________________________

	public long getRangeValue()
	{
		return finish - start;
	}

	//___________________________________________________________________________
	
	public boolean containsValue(Object o)
	{
		if (o == null || !(o instanceof Long))
			return false;
		
		long value = ((Long) o).longValue();
		return this.start <= value  && this.finish >= value;
	}

	public boolean contains(AxisInterval i)
	{
		if (i == null || !(i instanceof LongInterval))
			return false;
		
		LongInterval interval = (LongInterval) i;
		return this.start <= interval.getStartValue() &&
		   this.finish >= interval.getFinishValue();
	}

	public boolean intersects(AxisInterval i)
	{
		if (i == null || !(i instanceof LongInterval))
			return false;
		
		LongInterval interval = (LongInterval) i;
		return ! (finish < interval.getStartValue())
		   || ! (start > interval.getFinishValue());
	}
	
	//	________________________________________________________________________
	
	public AxisInterval union(AxisInterval i) {
		if (i == null)
			return new LongInterval(new Long(getStartValue()), new Long(getFinishValue()));
	
		if (!(i instanceof LongInterval))
			return null;
		
		LongInterval interval = (LongInterval) i;
		return new LongInterval(
				Math.min(getStartValue(), interval.getStartValue()), 
				Math.max(getFinishValue(), interval.getFinishValue()));
	}
	
	//	________________________________________________________________________
	
	public Object clone() {
		return new LongInterval(getStartValue(), getFinishValue());
	}
	
	public DrawingTransform getTransform() {
		return new LongTransform(getStartValue(), getFinishValue() - getStartValue());
	}
}
