/*
 * @(#)AbstractViewRange.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.scrolling.range.view;

import com.egantt.drawing.view.ViewManager;

import com.egantt.model.drawing.axis.view.event.ViewEvent;
import com.egantt.model.drawing.axis.view.event.ViewListener;

import com.egantt.model.scrolling.ScrollingRange;

import com.egantt.model.scrolling.range.event.RangeEvent;
import com.egantt.model.scrolling.range.event.RangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractViewRange implements ScrollingRange
{
	protected ViewManager manager;
	
	protected transient LocalViewListener listener;
	protected transient List<RangeListener> listeners = new ArrayList<RangeListener>();
	
	// __________________________________________________________________________
		
	public void setManager(ViewManager manager)
	{
		if (this.manager != null)
			this.manager.getView().removeViewListener(listener);
		else
			listener = new LocalViewListener(this);
		
		this.manager = manager;
		manager.getView().addViewListener(listener);
	}
	
	// __________________________________________________________________________
	
	public void addRangeListener(RangeListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeRangeListener(RangeListener listener)
	{
		listeners.remove(listener);
	}
	
	//___________________________________________________________________________
	
	protected void notifyChanges(RangeEvent event)
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext();)
			((RangeListener) iter.next()).stateChanged(event);
	}
	
	//___________________________________________________________________________

	protected class LocalViewListener implements ViewListener
	{
		protected Object propogationID;

		public LocalViewListener(Object propogationID)
		{
		   this.propogationID = propogationID;
		}

		public void stateChanged(ViewEvent e)
		{
		   notifyChanges(new RangeEvent(e));
		}
	}
}
