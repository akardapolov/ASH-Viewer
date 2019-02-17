/*
 * @(#)ZoomAction.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.actions.view;

import com.egantt.drawing.view.ViewManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
  * Zooms in to the appropriate zoom level using the ViewManager
  */
public class ZoomAction extends AbstractAction
{
	private static final long serialVersionUID = 5665163388765156432L;

	protected ViewManager manager;
	protected int step = 0;

	public ZoomAction(String name, Icon icon)
	{
		super(name, icon);
	}
	
	public ZoomAction(String name, Icon icon, int step, ViewManager manager) {
		super(name, icon);
		this.step = step;
		this.manager = manager;
	}
	
	// _________________________________________________________________________
	
	public void setManager(ViewManager manager)
	{
		this.manager = manager;
	}
	
	// _________________________________________________________________________
	
	public void setStep(int step)
	{
		this.step = step;
	}
	
	// _________________________________________________________________________

	public void actionPerformed(ActionEvent event)
	{
		manager.translate(step, true);
	}
}
