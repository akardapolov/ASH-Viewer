/*
 * @(#)LongAxis.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.interval.LongInterval;

import com.egantt.model.drawing.axis.view.LongView;

import com.egantt.model.drawing.transform.LongTransform;

/**
  * A trivial implementation of an Axis
  */
public class LongAxis extends AbstractAxis
{
	public DrawingTransform getTransform()
	{
		LongInterval interval = (LongInterval) this.interval;
		return new LongTransform(interval.getStartValue(), interval.getRangeValue());
	}
			
	public AxisView getView(int orientation)
	{
		return new LongView(this, orientation);
	}
}
