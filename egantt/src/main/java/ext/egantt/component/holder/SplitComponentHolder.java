/*
 * @(#)SplitComponentHolder.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.component.holder;

import java.awt.Component;

import javax.swing.BoundedRangeModel;

import com.egantt.model.component.ComponentList;
import com.egantt.swing.pane.basic.SplitScrollPane;

import ext.egantt.component.ComponentHolder;

/**
 *  The most basic implementation of the split table thing
 */
public class SplitComponentHolder extends SplitScrollPane implements ComponentHolder
{	
	private static final long serialVersionUID = 7207030660661282954L;

	public SplitComponentHolder()
	{	
		super(SplitScrollPane.HORIZONTAL_SPLIT);
	}

	// __________________________________________________________________________

	public void setComponentList(ComponentList list)
	{
		setLeftComponent(list.get(0));
		setRightComponent(list.get(1));
	}

	public void setRangeModel(BoundedRangeModel model)
	{
		super.setRangeModel(model);
	}

	// __________________________________________________________________________

	public Component getComponent()
	{
		return this;
	}
}
