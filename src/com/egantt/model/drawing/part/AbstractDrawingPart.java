/*
 * @(#)AbstractDrawingPart.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.part;

import com.egantt.model.drawing.DrawingPart;

import com.egantt.model.drawing.part.event.DrawingPartEvent;
import com.egantt.model.drawing.part.event.DrawingPartListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
  * Intent: to simply the model design
  */
public abstract class AbstractDrawingPart implements DrawingPart
{
	protected List <DrawingPartListener>drawingPartListeners = new ArrayList<DrawingPartListener>(2);

	// __________________________________________________________________________

	public synchronized void addDrawingPartListener(DrawingPartListener drawingPartListener)
	{
		drawingPartListeners.add(drawingPartListener);
	}

	public synchronized void removeDrawingPartListener(DrawingPartListener drawingPartListener)
	{
		drawingPartListeners.remove(drawingPartListener);
	}

	// __________________________________________________________________________

	protected synchronized void fireStateChanged(DrawingPartEvent event)
	{
	   for (Iterator iter = drawingPartListeners.iterator(); iter.hasNext();)
		   ((DrawingPartListener) iter.next()).stateChanged(event);
	}
}
