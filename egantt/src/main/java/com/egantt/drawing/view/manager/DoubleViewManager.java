/*
 * @(#)DoubleViewManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.view.manager;

import com.egantt.drawing.view.ViewManager;

import com.egantt.model.drawing.DrawingAxis;

import com.egantt.model.drawing.axis.AxisView;

import com.egantt.model.drawing.axis.interval.DoubleInterval;

public class DoubleViewManager implements ViewManager
{
	protected AxisView view;
	protected double center = Double.MAX_VALUE; /** treat MAX_VALUE as null */
	
	// __________________________________________________________________________
	
	public void setView(AxisView view)
	{
		this.view = view;
	}
	
	// __________________________________________________________________________
	
	public DrawingAxis getAxis()
	{
		return view.getAxis();
	}

	public AxisView getView()
	{
		return view;
	}
	
	// __________________________________________________________________________
	
	public boolean setStart(Object start)
	{
		DoubleInterval visible = (DoubleInterval) view.getInterval();
		
		double startValue = ((Double) start).doubleValue();
		double range = ((Double) visible.getRange()).doubleValue();
		
		// update the mid point
		this.center = startValue + (range / 2);
		
		return view.setVisible(new DoubleInterval(startValue, startValue + range));
	}
	
	public boolean setFinish(Object finish)
	{
		DoubleInterval visible = (DoubleInterval) view.getInterval();
		double finishValue = visible.getFinishValue();
		double range = ((Double) visible.getRange()).doubleValue() / 2;
		
		// update the mid point
		this.center = range / 2;
		
		return view.setVisible(new DoubleInterval(finishValue - range, finishValue));
	}
	
	// __________________________________________________________________________
	
	public boolean translate(int x, boolean force)
	{
		DoubleInterval visible = (DoubleInterval) view.getInterval();
		double range = visible.getRangeValue() /2;
		
		this.center = (center == Double.MAX_VALUE)
			? range + visible.getStartValue(): this.center;
		
		double offset = (range / 50) * x;
	
		double startValue = center - range - offset;
		double finishValue = center + range + offset;
		
		if (force)
		{
			DoubleInterval interval = (DoubleInterval) getAxis().getInterval();
			double s = interval.getStartValue();
			double f = interval.getFinishValue();
			
			finishValue += (s > startValue) ?  s - startValue : 0;
			startValue -= (finishValue > f) ? finishValue -f : 0;
						
			startValue = Math.max(startValue, s);
			finishValue = Math.min(finishValue, f);
		}
		return view.setVisible(new DoubleInterval(startValue, finishValue));
	}
}
