/*
 * @(#)BasicCalendarGranularity.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.model.drawing.granularity;

import com.egantt.model.drawing.DrawingGranularity;
import com.egantt.model.drawing.DrawingTransform;

import ext.egantt.model.drawing.granularity.iterator.CalendarIterator;

import com.egantt.model.drawing.granularity.iterator.ArrayIterator;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class BasicCalendarGranularity implements DrawingGranularity
{
	protected final Object granularities[];
	protected final boolean round;
	protected final int step;

	public BasicCalendarGranularity(int step, Object granularities[])
	{
		this (step, true, granularities);

	}

	protected BasicCalendarGranularity(int step, boolean round, Object granularities[])
	{
		this.granularities = granularities;
		this.round = round;
		this.step = step;
	}

	//___________________________________________________________________________

	public long width(Object granularity, DrawingTransform transform, long width)
	{
		long start = ((Long) transform.inverseTransform(0, width)).longValue();

		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date(start) );
		cal.add(((Integer) granularity).intValue(), step);

		return transform.transform(new Long(cal.getTime().getTime()), width);
	}

	//___________________________________________________________________________

	public Iterator keys()
	{
		return new ArrayIterator(granularities, 1); // one element at a time
	}

	public Iterator values(Object granularity, DrawingTransform transform, long width)
	{
		return new CalendarIterator(transform, ((Integer) granularity).intValue(), step, width, round);
	}
}
