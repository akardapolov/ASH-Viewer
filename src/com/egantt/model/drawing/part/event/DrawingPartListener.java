/*
 * @(#)DrawingPartListener.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.part.event;

public interface DrawingPartListener
{
	/**
	 *  A ChangeEvent has occured
	 */
	void stateChanged(DrawingPartEvent event);
}
