/*
 * @(#)BasicSplitPane.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.pane.basic;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.egantt.swing.SwingPane;

/**
 *  Extends JSplitPane to provide a component which has a JScrollPane visible
 *  in each section. The right section has the scrollbar overriden so that it
 *  can be utilised by a custom scrollbar.
 */
public class BasicSplitPane extends JPanel implements SwingPane
{
	private static final long serialVersionUID = -3104977481978858679L;

	private boolean applied = false;

	public BasicSplitPane()
	{
		super();
	}

}
