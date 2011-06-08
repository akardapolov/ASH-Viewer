/*
 * @(#)DrawingToolTipState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.tooltip.state;

import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.tooltip.ToolTipState;

import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;

import com.egantt.model.drawing.axis.AxisInterval;

import java.awt.event.MouseEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JToolTip;

/**
 *  A twist on tooltips, to use the DrawingState to get the values, as the
 *  DrawingState has the ability to convert the location into a key
 */
public class DrawingToolTipState implements ToolTipState
{
	/**
	 *  Strange things can happen in JDK 1.3.x if the ToolTipManager
	 *  RETURNS NULL on #getToolTipText
	 */
	private static final String EMPTY_STRING = "";

	// __________________________________________________________________________

	/**
	 *  Creates a new instance of JToolTip this needs to be checked at some point
	 *  wether creating or caching this tooltip instance is the correct way of
	 *  going about things. It is uncertain from looking at the ToolTipManager code
	 *  to do this we need to test on all JDK's
	 */
	public JToolTip createToolTip()
	{
		return new JToolTip();
	}

	// __________________________________________________________________________

	/**
	 *  The current implementation is less than desired basically we interrogate
	 *  the state to find out what the underlying key is and from there it is
	 *  possible to get the Interval from the appropriate part.
	 */
	public String getToolTipText(MouseEvent event, CellState cellState)
	{
		DrawingState drawing = cellState.getDrawing();
		Object key = drawing != null ? drawing.getValueAt(event.getPoint()): null;
		if (key == null)
			return EMPTY_STRING;

		DateFormat format = SimpleDateFormat.getDateInstance();

		// iterate throught the drawing parts looking for a part that contains our key
		for (Iterator iter = drawing.parts(); iter.hasNext();)
		{
			DrawingPart part = (DrawingPart) iter.next();
			AxisInterval [] empty = new AxisInterval[0];

			// display the xAxis on the screen
			AxisInterval [ ]intervals = part.getInterval(key, empty);
			if (intervals != null && intervals[0] != null)
			{
				Date d1 = new Date(((Long)intervals[0].getStart()).longValue());
				Date d2 = new Date(((Long)intervals[0].getFinish()).longValue());
				return format.format(d1) + " to " + format.format(d2);
			}
		}
		return EMPTY_STRING;
	}
}
