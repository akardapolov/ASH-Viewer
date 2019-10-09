package com.egantt.model.drawing.axis;

public interface MutableInterval extends AxisInterval, Cloneable
{
	/**
	 *  Sets the start value
	 */
	void setFinish(Object value);
	
	/**
	 *  Sets the finish value
	 */
	void setStart(Object value);	
	
	Object clone();
}