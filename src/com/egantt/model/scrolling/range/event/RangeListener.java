/*
 * @(#)RangeListener.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.scrolling.range.event;

public interface RangeListener
{
	/**
	 *  Notifies the outer range that a changes has happend
	 */
	void stateChanged(RangeEvent event);
}
