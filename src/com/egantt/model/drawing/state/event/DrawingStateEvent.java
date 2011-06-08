/*
 * @(#)DrawingStateEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.state.event;

import java.util.EventObject;

public class DrawingStateEvent extends EventObject
{
	private static final long serialVersionUID = -3380415663275546714L;

	public DrawingStateEvent(Object source)
	{
		super(source);
	}
}
