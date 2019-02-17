/*
 * @(#)HeadlessRootPane.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.pane.headless;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;

/**
 *  <code>HeadlessRootPane</code> is implemented to replace the root pane, that
 *  would normally sit underneath a JApplet or JFrame.
 *  This becomes essentially the root of the drawing but this component does not have a peer
 */
public class HeadlessRootPane extends JRootPane
{
	// __________________________________________________________________________
	
	private static final long serialVersionUID = 4487014218651101587L;

	/**
	 *  Extended to add, the addNotify() which updates the componentPeer
	 */
	protected Container createContentPane()
	{
		Container container = super.createContentPane();
		container.addNotify();
		return container;
	}

	/**
	 *  Extended to add, the addNotify() which updates the componentPeer
	 */
	protected Component createGlassPane()
	{
		Component component = super.createContentPane();
		component.addNotify();
		return component;
	}

	/**
	 *  Extended to add, the addNotify() which updates the componentPeer
	 */
	protected JLayeredPane createLayeredPane()
	{
		JLayeredPane pane = super.createLayeredPane();
		pane.addNotify();
		return pane;
	}

	// __________________________________________________________________________

	/**
	 *  Forces the RootPane to always be showing not an issue for Headless Graphics
	 */
	public boolean isShowing()
	{
		return true;
	}
}
