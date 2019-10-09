/*
 * @(#)DoubleTransform.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.transform;

import java.math.BigDecimal;
import java.math.MathContext;

import com.egantt.model.drawing.DrawingTransform;

public class DoubleTransform implements DrawingTransform
{
	private static final BigDecimal MAX_VALUE = new BigDecimal(Integer.toString(Integer.MAX_VALUE));
	private static final BigDecimal MIN_VALUE = new BigDecimal(Integer.toString(Integer.MIN_VALUE));
	
	protected double offset;
	protected double range;

	public DoubleTransform(double offset, double range)
	{
		this.offset = offset;
		this.range = range;
	}

	// __________________________________________________________________________

	public int transform(Object v, long pixels)
	{
		BigDecimal ppm = new BigDecimal(Double.toString(pixels), MathContext.DECIMAL128);
		ppm = ppm.divide(new BigDecimal(Double.toString(range), MathContext.DECIMAL128), MathContext.DECIMAL128);
		
		BigDecimal value = new BigDecimal(((Double) v).toString(), MathContext.DECIMAL128); // an assert
		value = value.subtract(new BigDecimal(Double.toString(offset), MathContext.DECIMAL128), MathContext.DECIMAL128);
		value = value.multiply(ppm, MathContext.DECIMAL128);
		
		if (value.compareTo(DoubleTransform.MIN_VALUE) < 0)
			return Integer.MIN_VALUE;
		
		if (value.compareTo(DoubleTransform.MAX_VALUE) > 0)
			return Integer.MAX_VALUE;
	
		return value.intValue();
	}

	public Object inverseTransform(long pixel, long pixels)
	{
		BigDecimal ppm = new BigDecimal(Double.toString(pixels), MathContext.DECIMAL128);
		ppm = ppm.divide(new BigDecimal(Double.toString(range), MathContext.DECIMAL128), MathContext.DECIMAL128);
		
		BigDecimal value = new BigDecimal(Double.toString(pixel), MathContext.DECIMAL128);
		value = value.divide(ppm, MathContext.DECIMAL128);
		value = value.add(new BigDecimal(Double.toString(offset), MathContext.DECIMAL128), MathContext.DECIMAL128);
		return new Double(value.doubleValue());
	}
}
