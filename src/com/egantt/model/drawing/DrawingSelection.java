/*
 * @(#)DrawingSelection.java
 *
 * Copyright 2002 DayOrganiser LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

import java.awt.event.MouseEvent;

public interface DrawingSelection
{
	/**
	 *  returning true will fire a repaint
	 *	passing in a mouse event may be hacky but it's a full supply of info that
	 *  the model needs to work out whats happening with the selection if your not happy
	 *  all you have to do is create your own selection model interface and your own
	 *  mouse listener and of course your own model (so don't hassle me on this)
	 */
	boolean select(Object select, MouseEvent event);
}
