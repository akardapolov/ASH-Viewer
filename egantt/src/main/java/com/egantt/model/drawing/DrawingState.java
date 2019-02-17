/*
 * @(#)DrawingState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

import com.egantt.model.drawing.axis.AxisView;

import com.egantt.model.drawing.state.event.DrawingStateListener;

import java.awt.Point;
import java.awt.Shape;

import java.util.Iterator;

/**
  * This interface, is designed to remember the state of a drawing between repaints
  */
public interface DrawingState
{
	// __________________________________________________________________________

	void initialise();

	void terminate();

	// __________________________________________________________________________

	boolean addDrawingStateListener(DrawingStateListener listener);

	boolean removeDrawingStateListener(DrawingStateListener listener);

	// __________________________________________________________________________

	/**
	  * Intent: converts a location back into a unique idenitifier
	  * @param @see.java.awt.Point: mouse location from the <code>Drawing</code>
	  * @return @see java.lang.Object unique idenfifier likely to return null
	  */
	Object getValueAt(Point location);
	
	Object getValueAt(Point _location, int xOffset, int yOffset);

	/**
	  * Intent: to iterate through the Drawing Parts
	  * return: @see java.util.Iterator the uppermost drawingParts are returned first
	  */
	Iterator parts();

	// __________________________________________________________________________

	/**
	  * Intent: to listen to an Axis and provide notification changes
	  * @param Axis axis to listen to
     * @return: false if already listened to
	  */
	boolean put(Object key, AxisView axisView);

	/**
	  * Intent: to register the bounds to a given unique key, there can be
	  * n bounds for 1 key.
	  * @param key: unique identifier
	  * @param bounds: the drawing bounds
	  * @return if the bound --> key have not been added before return true
	  */
	boolean put(Object key, Shape bounds);


	// __________________________________________________________________________

	/**
	  * Intent: has the drawing state been notified about this object before
	  * @return true if the key has been previously added
	  */
	boolean contains(Object key);
}
