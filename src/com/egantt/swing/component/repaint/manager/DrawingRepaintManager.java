/*
 * @(#)DrawingRepaintManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.repaint.manager;

import com.egantt.model.drawing.DrawingState;

import com.egantt.model.drawing.state.event.DrawingStateEvent;
import com.egantt.model.drawing.state.event.DrawingStateListener;
import com.egantt.swing.component.ComponentContext;

import javax.swing.JComponent;

/**
 *  This is the repaint notification metchanism for DrawingStates, to notify a
 *  group that they have changed
 */
public class DrawingRepaintManager extends BasicRepaintManager
{
	protected DrawingState drawingState;
	protected LocalStateListener listener; // listener is disposed of when not required
		
	// _________________________________________________________________________
	
	public synchronized void setDrawingState(DrawingState drawingState)
	{
		if (this.drawingState != null)
			drawingState.removeDrawingStateListener(listener);
		
		this.drawingState = drawingState; // the new drawingState
		
		if (listener != null)
			drawingState.addDrawingStateListener(listener);
	}
	
	// _________________________________________________________________________
	
	public synchronized void registerComponent(JComponent component, ComponentContext context)
	{
		if (listener == null)
		{
			this.listener = new LocalStateListener();
			if (drawingState != null)
				drawingState.addDrawingStateListener(listener);
		}
		super.registerComponent(component, context);
	}
	
	public synchronized void unregisterComponent(JComponent component)
	{
		if (listener != null && components.size() == 1)
		{
			if (drawingState != null)
				drawingState.removeDrawingStateListener(listener);
			this.listener = null;
		}
		super.unregisterComponent(component);
	}
	
	// __________________________________________________________________________
	
	protected class LocalStateListener implements DrawingStateListener
	{
		public void stateChanged(DrawingStateEvent event)
		{
			repaint();
		}
	}
}
