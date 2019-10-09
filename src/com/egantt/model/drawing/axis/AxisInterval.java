/*
 * @(#)AxisInterval.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis;

import com.egantt.model.drawing.DrawingTransform;

public interface AxisInterval
{
	/**
	 *  Start of the interval
	 */
	Object getStart();

	/**
	 *  Finish of the interval
	 */
	Object getFinish();

	/**
	 *  Range of the Interval  getFinish() - getStart()
	 */
	Object getRange();

	// __________________________________________________________________________

	/**
	 *  Is this interval inside of this
	 */
	boolean contains(AxisInterval interval);

	/**
	 *  Do we contain this value
	 */
	boolean containsValue(Object o);

	// __________________________________________________________________________

	/**
	 *  Is there an intersection between the two intervals
	 */
	boolean intersects(AxisInterval interval);
	
	AxisInterval union(AxisInterval i);

	DrawingTransform getTransform();
}
