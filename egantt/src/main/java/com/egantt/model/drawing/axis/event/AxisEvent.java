/*
 * @(#)AxisEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.axis.event;

import javax.swing.event.ChangeEvent;

/**
 * AxisEvent is used to notify interested parties that
 * state of an Axis has changed
 */
public class AxisEvent extends ChangeEvent
{
	private static final long serialVersionUID = 3651257934933480138L;

	public AxisEvent(Object source)
	{
		super(source);
	}
}
