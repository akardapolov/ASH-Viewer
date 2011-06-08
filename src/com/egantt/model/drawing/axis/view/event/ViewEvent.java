/*
 * @(#)ViewEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.view.event;

import java.util.EventObject;

/**
 *  ViewEvent is used to notify interested parties that
 *  state of an Axis has changed
 */
public class ViewEvent extends EventObject
{
	private static final long serialVersionUID = 5610964589725881379L;

	public ViewEvent(Object propogationID)
	{
		super(propogationID);
	}
}
