/*
 * @(#)PainterResources.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.painter;

/**
  * Constants that should be used inconjuction with a GraphicsContext
  */
import com.egantt.swing.graphics.GraphicsResources;

public interface PainterResources extends GraphicsResources
{
	/**
	  * @see com.egantt.model.Granularity
	  */
	String GRANULARITY = "Granularity";

	String DRAWING_GRAPHICS = "Graphics";

	String DRAWING_PAINTER = "Painter";

	String GENERAL_PATH = "GeneralPath";
	String GRAPHICS_STATE = "State";
	String RANGE_MODEL = "Model";
}
