/*
 * @(#)BasicScrollManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.scroll.manager;

import com.egantt.model.scrolling.ScrollingRange;

import com.egantt.model.scrolling.range.event.RangeEvent;
import com.egantt.model.scrolling.range.event.RangeListener;

import javax.swing.event.ChangeEvent;

public class BasicScrollManager extends AbstractScrollManager
{
	protected RangeListener listener;
	protected ScrollingRange model;

	//	________________________________________________________________________

	public void setRangeModel(ScrollingRange model)
	{
		if (this.model != null)
			model.removeRangeListener(listener);
		else
			this.listener = new LocalRangeListener();

		this.model = model;
		model.addRangeListener(listener);
	}

	//	________________________________________________________________________

   public int getMinimum()
	{
		return 0;
	}

	public int getMaximum()
	{
		return model.getRange();
	}

	//	________________________________________________________________________

	public int getExtent()
	{
		return model.getExtent();
	}

	public int getValue()
	{
		return model.getValue();
	}

	//	________________________________________________________________________

	public void setValue(int value)
	{
		model.setValue(value);
   }

	//	________________________________________________________________________
	
	public int getBlockIncrement() {
		return model.getBlockIncrement(); 
	}

	public int getUnitIncrement() {
		return model.getUnitIncrement();
	}
	
	//	________________________________________________________________________

	protected class LocalRangeListener implements RangeListener
	{
	   public void stateChanged(RangeEvent event)
	   {
			fireChanged(new ChangeEvent(event.getSource()));
	   }
	}
}
