/*
 * @(#)DrawingModule.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.drawing;

import com.egantt.model.drawing.DrawingContext;

/**
  * Shortcut to implementing a high level drawing component
  * instead of attempting to understand how the library works many users prefer
  * to use one of these trivial plugins
  */
public interface DrawingModule
{
	/**
	 *  initialise the module
	 */
	void initialise(DrawingContext attributes);

	/**
	 *  terminate the module
	 */
	 void terminate(DrawingContext attributes);
}
