/*
 * @(#)AbstractAxis.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis;

import com.egantt.model.drawing.DrawingAxis;

import com.egantt.model.drawing.axis.event.AxisEvent;
import com.egantt.model.drawing.axis.event.AxisListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
  * AbstractAxis handles the basic implementation of an Axis which are not related
  * to the type of data that the Axis shall be supporting
  */
public abstract class AbstractAxis implements DrawingAxis
{
	protected AxisInterval interval;
	protected ArrayList <AxisListener> listeners = new ArrayList<AxisListener>(2); /** notification list */

	// __________________________________________________________________________

	public AxisInterval getInterval()
	{
		return interval;
	}

	// __________________________________________________________________________

	public synchronized void setInterval(AxisInterval interval)
	{
		this.interval = interval;
		fireStateChanged(new AxisEvent(this)); // notify changes
	}

	// __________________________________________________________________________

	public boolean addAxisListener(AxisListener listener)
	{
		synchronized (listeners)
		{
		   return listeners.add(listener);
		}
	}

	public boolean removeAxisListener(AxisListener listener)
	{
		synchronized (listeners)
		{
		   return listeners.remove(listeners);
		}
	}

	// __________________________________________________________________________

	protected void fireStateChanged(AxisEvent event)
	{
		synchronized (listeners)
		{
		   for (Iterator iter = listeners.iterator(); iter.hasNext();)
			   ((AxisListener) iter.next()).stateChanged(event);
		}
	}
}
