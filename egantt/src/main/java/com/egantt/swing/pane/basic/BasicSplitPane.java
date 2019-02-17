/*
 * @(#)BasicSplitPane.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.pane.basic;

import javax.swing.JSplitPane;

import com.egantt.swing.SwingPane;

/**
 *  Extends JSplitPane to provide a component which has a JScrollPane visible
 *  in each section. The right section has the scrollbar overriden so that it
 *  can be utilised by a custom scrollbar.
 */
public class BasicSplitPane extends JSplitPane implements SwingPane
{
	private static final long serialVersionUID = -3104977481978858679L;

	private boolean applied = false;

	public BasicSplitPane(int type)
	{
		super(type);
		setDividerSize(2); // any less than 2 pixels isn't resizable anymore is ugly
	}

	// __________________________________________________________________________

	/**
	 *  see javax.swing.plaf.BasicScrollPaneUI for more information
	 */
	public int getDividerLocation()
	{
		if (!applied)
		{
			applied = true;
			double proportionalLocation = 0.5;

			int location = getOrientation() == VERTICAL_SPLIT
				? (int)((double)(getHeight() - getDividerSize()) * proportionalLocation)
         	: (int)((double)(getWidth() - getDividerSize()) *proportionalLocation);

			setDividerLocation(location);
		}
		return super.getDividerLocation();
	}
}
