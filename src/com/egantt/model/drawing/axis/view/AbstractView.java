/*
 * @(#)AbstractView.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.egantt.model.drawing.DrawingAxis;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.view.event.ViewEvent;
import com.egantt.model.drawing.axis.view.event.ViewListener;

/**
 * AbstractAxis handles the basic implementation of an Axis which are not related
 * to the type of data that the Axis shall be supporting
 */
public abstract class AbstractView implements AxisView
{
	protected AxisInterval visible;  /** visible extents */
	protected DrawingAxis axis; /** maximum extents */

	protected Set <ViewListener>listeners = new HashSet<ViewListener>(); // notification list
	protected int orientation;

	public AbstractView(DrawingAxis axis, int orientation)
	{
		this.axis = axis;
		this.orientation = orientation;
	}
	
	// _________________________________________________________________________
	
	public int getOrientation() {
		return orientation;
	}

	// _________________________________________________________________________

	public DrawingAxis getAxis()
	{
		return axis;
	}

	public AxisInterval getInterval()
	{
		return visible != null ? visible : axis.getInterval();
	}

	// _________________________________________________________________________

	public DrawingTransform getTransform()
	{
		AxisInterval interval = visible != null
			?  visible
			:  axis.getInterval();
		return interval != null ? interval.getTransform() : null;
	}
	
//	 __________________________________________________________________________

	public synchronized boolean setVisible(AxisInterval visible)
	{
		if (! this.axis.getInterval().contains(visible))
			return false;

		this.visible = visible; // make changes

		fireStateChanged(new ViewEvent(this)); // notify changes
		return true;
	}

	// _________________________________________________________________________

	public synchronized boolean addViewListener(ViewListener listener)
	{
		return listeners.add(listener);
	}

	public synchronized boolean removeViewListener(ViewListener listener)
	{
		return listeners.remove(listeners);
	}

	// __________________________________________________________________________

	protected synchronized void fireStateChanged(ViewEvent event)
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext();)
			((ViewListener) iter.next()).stateChanged(event);
	}
}
