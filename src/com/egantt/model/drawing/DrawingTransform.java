/*
 * @(#)DrawingTransform.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

/**
  * Transforms convert between screen and world co-ordinates
  */
public interface DrawingTransform
{
	/**
	  * Transforms a value to it's pixel equivilant
	  */
	int transform(Object value, long axisSize);

	/**
	  * Transforms a pixel into a value
	  */
	Object inverseTransform(long pixel, long axisSize);
}