/*
 * @(#)ToolTipState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.tooltip;

import java.awt.event.MouseEvent;

import com.egantt.swing.cell.CellState;

/**
 *  Contains methods needed for generating tooltips, these are named the same as
 *  the methods in JComponent it is intended that your component that requires
 *  tooltips should fire it's notifications to an instance of this class.
 */
public interface ToolTipState
{
	/**
	 *  The current implementation is less than desired basically we interrogate
	 *  the state to find out what the underlying key is and from there it is
	 *  possible to get the Interval from the appropriate part.
	 * @param cellState 
	 */
	String getToolTipText(MouseEvent event, CellState cellState);
}
