/*
 * @(#)ScrollingRange.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.scrolling;

import com.egantt.model.scrolling.range.event.RangeListener;

/**
 *  The underling data for a ScrollModel of any sort for instance a JScrollBar implementation
 */
public interface ScrollingRange
{
	/**
	 *  return the extent
	 */
	int getExtent();
	
	/**
	 *  return the range
	 */
	int getRange();
	
	/**
	 *  return the value
	 */
	int getValue();
	
	// __________________________________________________________________________
	
	/**
	 *  sets the value
	 */
	void setValue(int value);
	
	// __________________________________________________________________________
	
	void addRangeListener(RangeListener listener);
	
	void removeRangeListener(RangeListener listener);

	int getBlockIncrement();

	int getUnitIncrement();
}
