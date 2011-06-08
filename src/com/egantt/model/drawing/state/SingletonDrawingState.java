/*
 * @(#)SingletonDrawingState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.state;

import java.util.Iterator;

import com.egantt.model.drawing.DrawingPart;

/**
  * The simplest implementation of a DrawingState notifies the container
  * that a repaint has occured
  */
public class SingletonDrawingState extends AbstractDrawingState
{
	protected final DrawingPart part;
		
	public SingletonDrawingState(DrawingPart part)
	{
		this.part = part;
	}

	// __________________________________________________________________________	

	
	public Iterator parts()
	{
		return new SingletonIterator(part);
	}

	// __________________________________________________________________________	
		
	protected class SingletonIterator implements Iterator
	{
		protected Object value;
		
		public SingletonIterator(Object value)
		{
			this.value = value;
		}

		// ______________________________________________________________________
		
		public boolean hasNext()
		{
			return value != null;		
		}
		
		// ______________________________________________________________________
		
		public Object next()
		{
			Object value = this.value;
			this.value = null;
			return value;
		}
		
		// ______________________________________________________________________
		
		/**
		 *  Not applicable in this instance
		 */
		public void remove()
		{
		}
	}
}
