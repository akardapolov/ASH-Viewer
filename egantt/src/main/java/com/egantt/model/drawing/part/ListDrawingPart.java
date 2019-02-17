/*
 * @(#)ListDrawingPart.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.model.drawing.part;

import com.egantt.model.drawing.axis.AxisInterval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
  * VectorModel is the default implementation of the DrawingModel and is
  * designed to be used by as a default implementation in most situation.
  *
  * ToDo: needs support for Fake 3D + Graphics3D (4D)
  */
public class ListDrawingPart extends AbstractDrawingPart implements MutableDrawingPart
{
	protected final Object painter;
	protected final List <Object>keys;
	protected final boolean summaryPart;

	protected Map <Object, AxisInterval[]>intervals = new HashMap<Object, AxisInterval[]>();
	protected Map <Object, Object>contexts = new HashMap<Object, Object>(); // EntryKey -- proxyKey
	protected Map <Object, Object>painters = new HashMap<Object, Object>(); // EntryKey --> plotterKey
	protected Map <Object, Object>states = new HashMap<Object, Object>(); // EntryKey -- proxyKey

	public ListDrawingPart(Object keyValues[], Object painter) {
		this (keyValues, painter, false);
	}
	
	public ListDrawingPart(Object keyValues[], Object painter, boolean summaryPart)
	{
		List <Object>keys = new ArrayList<Object>();
		{
			if (keyValues != null)
				for (int i=0; i < keyValues.length; i++)
					keys.add(i, keyValues[i]);
		}
		
		this.keys = keys;
		this.painter = painter;
		this.summaryPart = summaryPart;
	}

	// _________________________________________________________________________

	public boolean isSummaryPart() {
		return summaryPart;
	}

	// _________________________________________________________________________

	public void add(Object key, AxisInterval intervals[], Object painter, Object state, Object context)
	{
		this.intervals.put(key, intervals);

		// register the plotter & proxy
		contexts.put(key, context);
		painters.put(key, painter);
		states.put(key, state);
	}

	// _________________________________________________________________________

   public List <Object>keys()
	{
		return keys;
	}

	public Iterator values(AxisInterval intervals [])
	{
		List<Object> values = new ArrayList<Object>(this.intervals.size());
		for (Iterator iter = this.intervals.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry entry = (Map.Entry) iter.next();
			if (contains(intervals, (AxisInterval []) entry.getValue()))
				values.add(entry.getKey());
		}
		return this.intervals.keySet().iterator();
	}

	// __________________________________________________________________________

	public AxisInterval [] getInterval() {
		AxisInterval[] results = null;
		for (Iterator iter = intervals.values().iterator(); iter.hasNext();) {
			AxisInterval values [] = (AxisInterval []) iter.next();
			
			if (values == null)
				continue;
			
			// lazy load
			if (results == null)
				results = new AxisInterval[values.length];
			
			for (int i=0; i < values.length; i++)
				if (values[i] != null)
					results[i] = values[i].union(results[i]);
		}
		return results;
	}
	
	public AxisInterval [] getInterval(Object o, AxisInterval intervals[])
	{
		AxisInterval values [] = (AxisInterval []) this.intervals.get(o);
		if (values == null)
			return null;

		AxisInterval results [] = new AxisInterval[values.length];
		for (int i=0; i < results.length; i++)
			results[i] = values[i] != null || intervals.length <= i
				? values[i] : intervals[i];
		return results;
	}

	// __________________________________________________________________________

	public Object getPainter()
	{
		return painter;
	}

	// __________________________________________________________________________

	public Object getContext(Object key)
	{
		return contexts.get(key);
	}

	public Object getPainter(Object key)
	{
		return painters.get(key);
	}

	public Object getState(Object key)
	{
		return states.get(key);
	}

	// __________________________________________________________________________
	
	public void setContext(Object key, Object value)
	{
		contexts.put(key, value);
	}

	public void setPainter(Object key, Object value)
	{
		painters.put(key, value);
	}

	public void setState(Object key, Object value)
	{
		states.put(key, value);
	}

	//	________________________________________________________________________

	//	________________________________________________________________________

	//	________________________________________________________________________

	protected boolean contains(AxisInterval a [], AxisInterval b[])
	{
		for (int i=0; i < Math.min(a.length, b.length); i++)
			if (b[i] != null && !a[i].contains(b[i]))
				return false;
		return true;
	}
}
