/*
 * @(#)DoubleAxis.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.AxisView;

import com.egantt.model.drawing.axis.interval.DoubleInterval;

import com.egantt.model.drawing.axis.view.DoubleView;

import com.egantt.model.drawing.transform.DoubleTransform;

/**
  * An implementation of an Axis for doubles
  */
public class DoubleAxis extends AbstractAxis
{
	public DrawingTransform getTransform()
	{
		DoubleInterval interval = (DoubleInterval) this.interval;
		return new DoubleTransform(interval.getStartValue(), interval.getRangeValue());
	}
	
	public AxisView getView(int orientation)
	{
		return new DoubleView(this, orientation);
	}
}
