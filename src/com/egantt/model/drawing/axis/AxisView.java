/*
 * @(#)AxisView.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis;

import com.egantt.model.drawing.DrawingAxis;
import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.view.event.ViewListener;

public interface AxisView
{
    /**
     * Indicates that the <code>Adjustable</code> has horizontal orientation.  
     */
    int HORIZONTAL = 0; 

    /**
     * Indicates that the <code>Adjustable</code> has vertical orientation.  
     */
    int VERTICAL = 1;    

    /**
     * Indicates that the <code>Adjustable</code> has no orientation.  
     */
    int NO_ORIENTATION = 2;    

    //	________________________________________________________________________
    
	/**
	 *  Returns the underlying interval
	 */
	AxisInterval getInterval();

	/**
	 *  Returns the transform
	 */
	DrawingTransform getTransform();

	/**
	 *  Returns the underlying transform
	 */
	DrawingAxis getAxis();

	//	 _______________________________________________________________________
	
	int getOrientation();
	
	// _________________________________________________________________________

	/**
	 *  Ensures that this interval is visible: how the visibility is set is not
	 *  specified by this interface.
	 */
	boolean setVisible(AxisInterval interval);

	// __________________________________________________________________________

	/**
	  * Add a new listener
	  */
	boolean addViewListener(ViewListener listener);

	/**
	  * Remove an existing listener
	  */
	boolean removeViewListener(ViewListener listener);
}
