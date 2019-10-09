/*
 * @(#)DrawingAxis.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;

import com.egantt.model.drawing.axis.event.AxisListener;

/**
  * An Axis is a data representation of an Axis on a graph this interface is
  * indended to support linear and logarithmic Axis's
  */
public interface DrawingAxis
{
	//_get bounds________________________________________________________________

	AxisView getView(int orientation);

	/**
	  * Returns the maximum bounds of the Axis
     */
	AxisInterval getInterval();

	DrawingTransform getTransform();
	
	//_set bounds _______________________________________________________________

	/**
     * Update the maximum bounds to the one specified
     */
	void setInterval(AxisInterval interval);

	//_notification______________________________________________________________

	/**
     * Adds a listener to the Axis
	  */
	boolean addAxisListener(AxisListener listener);

	/**
	  * Remove an existing listener from the Axis
	  */
	boolean removeAxisListener(AxisListener listener);
}
