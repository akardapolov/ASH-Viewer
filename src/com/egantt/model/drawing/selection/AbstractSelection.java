/*
 * @(#)AbstractSelection.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.selection;

import java.awt.event.MouseEvent;
import java.util.Vector;

import com.egantt.model.drawing.DrawingSelection;

/**
  * The AbstractDrawingSelection is designed to take some of the heart-ache out of the design
  * of the model design which is a bit complicated for most users
  */
public abstract class AbstractSelection implements DrawingSelection
{
	protected Vector changeListeners = new Vector(2);
/**
	protected boolean clearSelection()
	{
	   boolean result = false;
	   for (Iterator iter = selection.iterator(); iter.hasNext();)
			result = fireSelected(iter.next(), false) || result;
	   return result;
	}

   protected boolean deselectOthers(Object o)
	{
	   boolean result = false;
	   for (Iterator iter = selection.iterator(); iter.hasNext();)
	   {
			Object z = iter.next();
			if (z != o)
			   result = fireSelected(o,false) || result;
	   }
	   return result;
	}

	protected boolean fireSelected(Object o, boolean selected)
   {
		return selected(o) ? selection.add(o) : selection.remove(o);
	}

	//_ info_____________________________________________________________________

	protected boolean selectable(Object o)
	{
		return true;
	}

	protected boolean selected(Object o)
	{
		return selection.contains(o);
	}

**/
	//___________________________________________________________________________

	/** warning returning true will fire a repaint
		despite the design of select to be away from gui events in this case
		the mouse event actually provides a lot of information crutial to selectionfs
	*/
	public boolean select(Object o, MouseEvent e)
	{
		boolean shiftDown = e.isShiftDown();
		boolean ctrlDown = e.isControlDown();

		if (!selectable(o))
			return (!shiftDown && !ctrlDown) ? false : clearSelection();

		if (shiftDown && selected(o))
		   return false;

		boolean changes = false;
		if (!shiftDown && !ctrlDown)
			changes = deselectOthers(o);

		return shiftDown ? fireSelected(o,true) || changes : toggleSelected(o) || changes;
	}

	//_request changes___________________________________________________________

	protected abstract boolean clearSelection();

	protected abstract boolean deselectOthers(Object o);

	protected boolean toggleSelected(Object o)
	{
	   return selected(o) ? fireSelected(o,false) : fireSelected(o,true);
	}

	protected abstract boolean fireSelected(Object o, boolean selected);

	//_info______________________________________________________________________

	protected abstract boolean selectable(Object o);

	protected abstract boolean selected(Object o);

	//___________________________________________________________________________
}
