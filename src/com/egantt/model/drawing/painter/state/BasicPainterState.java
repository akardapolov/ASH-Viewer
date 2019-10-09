/*
 * @(#)BasicPainterState.java
 *
 * Copyright 2002 DayOrganiser LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.painter.state;

import com.egantt.model.drawing.painter.PainterState;

import java.util.HashMap;
import java.util.Map;

public class BasicPainterState implements PainterState
{
	/** the size of the map will increase  based on experience */
	protected Map<Object, Object> values  = new HashMap<Object, Object>(0);
	
	// __________________________________________________________________________
	
	public void initialise()
	{
	}
	
	public void terminate()
	{
		values.clear();
	}
	
	// __________________________________________________________________________
	
	public Object get(Object type)
	{
		return values.get(type);
	}
	
	public Object put(Object type, Object value)
	{
		return values.put(type, value);
	}
}
