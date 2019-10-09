/**
 * @(#)DrawingPart.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.model.drawing;

import java.util.Iterator;
import java.util.List;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.part.event.DrawingPartListener;


/**
  * The diagram model is the information behind the actual diagram panel for table
  * functionality it is advisable to extend the diagram model as maintaining two
  * models is easier than maintaining the one
  */
public interface DrawingPart
{

	//	________________________________________________________________________
	
	boolean isSummaryPart();
	
	// __________________________________________________________________________

	/**
	  * Add a new listener to the model
	  */
	void addDrawingPartListener(DrawingPartListener listener);

	/**
	  * Remove an existing listener from the model
	  */
	void removeDrawingPartListener(DrawingPartListener listener);

	// __________________________________________________________________________

	/**
	  * Returns the axis keys in use
	  */
	List keys();

	/**
	  * Returns the keys
	  */
	Iterator values(AxisInterval interval[]);

	// __________________________________________________________________________

	/**
	 *  Returns a unique reference to the painter for this part i.e. a PartPainter
	 */
	Object getPainter();

	// __________________________________________________________________________

	/**
	  * Returns the location on the given Axis
	  */
	AxisInterval [] getInterval(Object key, AxisInterval intervals []);
	
	AxisInterval [] getInterval();
	
	// __________________________________________________________________________

	/**
	  * Returns a unique reference to the proxy required for the paint operation
	  */
	Object getContext(Object key);

	/**
     * Returns a unique reference to the plotter required for the paint operation
	  */
	Object getPainter(Object key);

	/**
     * Returns a unique reference to the State required for the paint operation
	  */
	Object getState(Object key);
}
