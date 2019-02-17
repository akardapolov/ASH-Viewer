/**
 * @(#)DrawingGranularity.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.model.drawing;

import com.egantt.model.drawing.DrawingTransform;

import java.util.Iterator;

/**
  * A Granularity is a drawable view on a series of data it uses a transform
  * to help calculate the best level of the series to draw.
  */
public interface DrawingGranularity
{
	/**
     * A width is always integral but the calculation may cause un-realisticly large value
	  */
	long width(Object granularity, DrawingTransform transform, long width);

	// __________________________________________________________________________

	/**
     * Return the available granularities
     */
	Iterator keys();

	/**
     * Iterate through the given granularity
     */
	Iterator values(Object granularity, DrawingTransform transform, long width);
}
