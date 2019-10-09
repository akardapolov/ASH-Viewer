/*
 * @(#)XYToolTipState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.tooltip.state;

import java.awt.event.MouseEvent;

import javax.swing.JToolTip;

import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.tooltip.ToolTipState;

/**
 *  The XY ToolTipManager is a very useful tool for debugging purposes it shows
 *  the underlying
 */
public final class XYToolTipState implements ToolTipState
{
	/**
	 *  As this class is never intended to be extended please use the instance
	 *  instead of creating mutliple to reduce memory allocation.
	 */
	public static final XYToolTipState instance = new XYToolTipState();

	// __________________________________________________________________________

	public JToolTip createToolTip()
	{
		return new JToolTip();
	}

	// __________________________________________________________________________

	public String getToolTipText(MouseEvent event, CellState state)
	{
		return "x = " + event.getX() + " y = " + event.getY();
	}
}
