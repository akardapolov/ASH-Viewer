/*
 * @(#)AxisListener.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.event;

/**
  * A Listener for notifications about changes within the Axis
  */
public interface AxisListener
{
	void stateChanged(AxisEvent e);
}
