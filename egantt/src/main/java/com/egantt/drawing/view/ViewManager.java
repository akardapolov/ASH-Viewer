/*
 * @(#)ViewManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.view;

import com.egantt.model.drawing.DrawingAxis;

import com.egantt.model.drawing.axis.AxisView;

public interface ViewManager
{
	/**
	 *  returns the DrawingAxis for this view
	 */
	DrawingAxis getAxis();
	
	/**
	 *  returns the AxisView
	 */
	AxisView getView();
	
	// __________________________________________________________________________
	
	/**
	 *  Sets the start
	 */
	boolean setStart(Object start);
	
	/**
	 *  Sets the finish
	 */
	boolean setFinish(Object finish);
	
	// __________________________________________________________________________
	
	/**
	 *  Translating may temporarily affect the mid-point therefore you may specify
	 *  if this is acceptable.
	 */
	boolean translate(int x, boolean force);
}
