/*
 * @(#)ViewResources.java
 *
 * Copyright 2002 DayOrganiser LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.view;

/**
  * DrawingAxisConstants interface
  */
public interface ViewResources
{
	/**
	 *  HorizontalView
	 */
	Integer HORIZONTAL = new Integer(0);

	/**
	 *  VerticalView
	 */
	Integer VERTICAL = new Integer(1);

	/**
	 *  DepthView / ZView intended for fake 3D / real 3D
	 */
	Integer DEPTH = new Integer(2);

	/**
	 *  Angle for real 3D only
	 */
	Integer ANGLE = new Integer(3);
}

