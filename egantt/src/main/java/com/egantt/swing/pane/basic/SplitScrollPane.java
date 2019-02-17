/*
 * @(#)SplitScrollPane.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.pane.basic;

import com.egantt.swing.SwingPane;
import com.egantt.swing.scroll.ScrollManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  Extends JSplitPane to provide a component which has a JScrollPane visible
 *  in each section. The right section has the scrollbar overriden so that it
 *  can be utilised by a custom scrollbar.
 */
public class SplitScrollPane extends BasicSplitPane implements SwingPane
{
	private static final long serialVersionUID = 5011039430920996486L;

	protected final JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

	protected JScrollPane rightComponent;
	protected BoundedRangeModel rangeModel;
	protected final ChangeListener changeListener = new LocalChangeListener();

	public SplitScrollPane(int type)
	{
		super(type);

		JScrollPane leftPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		leftPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		super.setLeftComponent(leftPane);

		rightComponent= new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rightComponent.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

		JPanel rightPane = new JPanel(new BorderLayout());
		rightPane.add(rightComponent, BorderLayout.CENTER);
		rightPane.add(scrollBar, BorderLayout.SOUTH);
		super.setRightComponent(rightPane);

		// synchronize the scroll models only works this way
		rightComponent.setVerticalScrollBar(leftPane.getVerticalScrollBar());
	}

	// __________________________________________________________________________

	public void setLeftComponent(Component component)
	{
		if (leftComponent == null) // not initialised yet see BasicSplitUI
		{
			super.setLeftComponent(component);
			return;
		}
		((JScrollPane)leftComponent).setViewportView(component);
	}

	public void setRightComponent(Component component)
	{
		if (rightComponent == null) // not initialised yet see BasicSplitUI
		{
			super.setRightComponent(component);
			return;
		}
		rightComponent.setViewportView(component);
	}

	// __________________________________________________________________________

	/**
	 * Set the model from the scroll pane
	 */
	public synchronized void setRangeModel(BoundedRangeModel rangeModel)
	{
		if (this.rangeModel != null)
		{
			this.rangeModel.removeChangeListener(changeListener);
		}
		
		// for the default scrollbar model lets use some defaults
		scrollBar.setBlockIncrement(rangeModel.getExtent() / 100);
		scrollBar.setUnitIncrement(rangeModel.getExtent() / 50);
		
		if (rangeModel instanceof ScrollManager) {
			ScrollManager scrollManager = (ScrollManager) rangeModel;
			scrollBar.setBlockIncrement(scrollManager.getBlockIncrement());
			scrollBar.setUnitIncrement(scrollManager.getUnitIncrement());
		}
		
		scrollBar.setModel(rangeModel);
		
		rangeModel.addChangeListener(changeListener);
		this.rangeModel = rangeModel;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	protected class LocalChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			setRangeModel(rangeModel);
			
		}	
	}
}