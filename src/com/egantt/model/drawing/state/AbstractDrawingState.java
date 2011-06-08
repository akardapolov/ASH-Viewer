/*
 * @(#)AbstractDrawingState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.state;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.view.event.ViewEvent;
import com.egantt.model.drawing.axis.view.event.ViewListener;
import com.egantt.model.drawing.part.event.DrawingPartEvent;
import com.egantt.model.drawing.part.event.DrawingPartListener;
import com.egantt.model.drawing.state.event.DrawingStateEvent;
import com.egantt.model.drawing.state.event.DrawingStateListener;

/**
  * The simplest implementation of a DrawingState notifies the container
  * that a repaint has occured
  */
public abstract class AbstractDrawingState implements DrawingState
{
	protected DrawingPartListener drawingPartListener = new LocalDrawingPartListener();
	protected ViewListener viewListener = new LocalViewListener();
	protected List<DrawingStateListener> stateListeners = new ArrayList<DrawingStateListener>(1); // assumes 1x manager
	protected List<AxisView> viewListeners = new ArrayList<AxisView>(4); // 4x(3D), 3x(Fake3D), 2x(2D)

	protected Map <Object, Shape>bounds = new HashMap<Object, Shape>();

	//___________________________________________________________________________

	public synchronized boolean put(Object key, AxisView axisView)
	{
		return addViewListener(axisView);
	}

	public synchronized boolean put(Object key, Shape bounds)
	{
		return this.bounds.put(key, bounds) == null;
	}

	public boolean contains(Object key)
	{
		return this.bounds.containsKey(key);
	}

	// _________________________________________________________________________

	public Object getValueAt(Point _location) {
		return getValueAt(_location, 2, 2);
	}
	
	public Object getValueAt(Point _location, int xOffset, int yOffset)
	{
		Point2D location = new Point2D.Double(_location.getX(), _location.getY());
		
		Rectangle locationBounds = new Rectangle(_location.x - xOffset, _location.y - yOffset, xOffset * 2, yOffset * 2) ;
		for (Iterator iter = this.bounds.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry entry = (Map.Entry) iter.next();

			Shape shape = (Shape) entry.getValue();
			if (!shape.getBounds().intersects(locationBounds))
				continue;
			
			if (shape.contains(location) || shape.intersects(locationBounds))
				return entry.getKey();
		}
		return null;
	}

	// _________________________________________________________________________

	public void initialise()
	{
		clearAxisListeners();
		clearBounds();
	}

	public void terminate()
	{
	}

	// __________________________________________________________________________

	public boolean addDrawingStateListener(DrawingStateListener listener)
	{
		return stateListeners.add(listener);
	}

	public boolean removeDrawingStateListener(DrawingStateListener listener)
	{
	   return stateListeners.remove(listener);
	}

	// __________________________________________________________________________

	protected synchronized boolean addViewListener(AxisView axisView)
	{
		return viewListeners.add(axisView) && axisView.addViewListener(viewListener);
	}

	protected synchronized boolean removeViewListener(AxisView axisView)
	{
		return axisView.removeViewListener(viewListener) && viewListeners.remove(viewListener);
	}

	// __________________________________________________________________________

	protected synchronized void clearAxisListeners()
	{
		// clear axis listeners
		for (Iterator iter = viewListeners.iterator(); iter.hasNext();)
		{
		   AxisView axisView = (AxisView) iter.next();
			axisView.removeViewListener(viewListener);
		   iter.remove();
		}
	}

	//___________________________________________________________________________

	protected synchronized void clearBounds()
	{
		this.bounds.clear();
	}

	//___________________________________________________________________________

	protected void fireChanged(EventObject e)
	{
		// use the real propogation ID, we never generated anything we are just
		// forwarding it on behalf of another object is this the right thing?
		DrawingStateEvent event = new DrawingStateEvent(e.getSource());
		for (Iterator iter = stateListeners.iterator(); iter.hasNext();)
		   ((DrawingStateListener) iter.next()).stateChanged(event);
	}

	/**
	  * listens to n Axises
	  */
	protected class LocalViewListener implements ViewListener
	{
		public void stateChanged(ViewEvent event)
		{
		   fireChanged(event);
		}
	}

	//___________________________________________________________________________

	/**
	  * Listens to n drawings
	  */
	protected class LocalDrawingPartListener implements DrawingPartListener
	{
		public void stateChanged(DrawingPartEvent event)
		{
		   fireChanged(event);
		}
	}
}
