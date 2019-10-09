/*
 * @(#)LongViewManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.view.manager;

import java.math.BigDecimal;

import com.egantt.drawing.view.ViewManager;

import com.egantt.model.drawing.DrawingAxis;

import com.egantt.model.drawing.axis.AxisView;

import com.egantt.model.drawing.axis.interval.LongInterval;

public class LongViewManager implements ViewManager
{
	protected AxisView view;
	protected long center = Long.MAX_VALUE; /** treat MAX_VALUE as null */
	
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
		LongInterval visible = (LongInterval) view.getInterval();
		
		long startValue = ((Long) start).longValue();
		long range = ((Long) visible.getRange()).longValue();
		
		// update the mid point
		this.center = startValue + (range / 2);
		
		return view.setVisible(new LongInterval(startValue, startValue + range));
	}
	
	public boolean setFinish(Object finish)
	{
		LongInterval visible = (LongInterval) view.getInterval();
		long finishValue = visible.getFinishValue();
		long range = ((Long) visible.getRange()).longValue() / 2;
		
		// update the mid point
		this.center = range / 2;
		
		return view.setVisible(new LongInterval(finishValue - range, finishValue));
	}
	
	// __________________________________________________________________________
	
	public boolean translate(int x, boolean force)
	{
		LongInterval visible = (LongInterval) view.getInterval();
		long range = visible.getRangeValue() /2;
		
		this.center = (center == Long.MAX_VALUE)
			? range + visible.getStartValue(): this.center;
		
			
		long offset = 0; 
		{
			BigDecimal result = new BigDecimal(Long.toString(range));
			result = result.divide(new BigDecimal(Double.toString(50d)));
			result = result.multiply(new BigDecimal(Integer.toString(x)));
			offset = result.longValue();
		}
	
		long startValue = center - range - offset;
		long finishValue = center + range + offset;
		
		if (force)
		{
			LongInterval interval = (LongInterval) getAxis().getInterval();
			long s = interval.getStartValue();
			long f = interval.getFinishValue();
			
			finishValue += (s > startValue) ?  s - startValue : 0;
			startValue -= (finishValue > f) ? finishValue -f : 0;
						
			startValue = Math.max(startValue, s);
			finishValue = Math.min(finishValue, f);
		}
		return view.setVisible(new LongInterval(startValue, finishValue));
	}
}
