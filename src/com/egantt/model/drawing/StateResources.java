/**
 * @(#)StateResources.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.model.drawing;

import java.awt.Component;
import java.awt.Shape;

import com.egantt.swing.cell.CellState;

/**
 *  <code>StateResources</code> Interface to define a set of resources that may 
 *  appear in a DrawingState;
 */
public interface StateResources
{
	String COMPONENT = Component.class.getName();

	String GRANULARITY_KEY = "GranularityKey";

	String SHAPE = Shape.class.getName();

	/** An array of transforms[] */
	String TRANSFORMS = "Transforms[]";

	String DRAWING_PART = DrawingPart.class.getName();

	String CELL_STATE = CellState.class.getName();
}
