/*
 * @(#)CalendarIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.model.drawing.granularity.iterator;

import com.egantt.model.drawing.DrawingTransform;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class CalendarIterator implements Iterator
{
	protected Calendar position = Calendar.getInstance();
	protected DrawingTransform transform;
	
	protected boolean returnValue = true;
	
	protected int field;
	protected int step;
	
	protected long finish;
	
	/** field is Calendar.field */
	public CalendarIterator(DrawingTransform transform, int field, int step,
		long width, boolean needsRounding)
	{
		this.field = field;
		this.step = step;
		this.transform = transform;

		// calculate the start & finish of the interval from the transform
		long start =  ((Long) transform.inverseTransform(0, width)).longValue();
		this.finish = ((Long) transform.inverseTransform(width, width)).longValue();
		
		// update the position
		// position.setTimeInMillis(start); // 1.4.x SDK Only
		position.setTime(new Date(start));  // 1.3.x SDK+

		// performing rounding is the norm so that a month starts at a month not where the interval starts
		if (needsRounding)
			for (int i=field + 1; i < 14; i++)
			   position.clear(i);
	}

	// __________________________________________________________________________
	
	public boolean hasNext()
	{
		return returnValue;
	}

	public Object next()
	{
		// long value = position.getTimeInMillis(); // 1.4.x SDK Only
		long value = position.getTime().getTime();  // 1.3.x SDK+
		this.returnValue = value < finish;
		position.add(field, step); // next element
		return new Long(value);
	}

	// __________________________________________________________________________

	public void remove()
	{
	}
}
