/*
 * @(#)BasicComponentContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.context;

import com.egantt.swing.component.ComponentContext;

import java.util.HashMap;
import java.util.Iterator;

/**
 *  A Basic context, which has the underlying structure of a HashMap
 */
public class BasicComponentContext implements ComponentContext
{
	protected final HashMap <Object, Object>values = new HashMap<Object, Object>();
	
	// __________________________________________________________________________
	
	public Object get(Object key)
	{
		return values.get(key);
	}
	
	public Object put(Object key, Object value)
	{
		return values.put(key, value);
	}
	
	// __________________________________________________________________________
	
	public Iterator keys()
	{
		return values.keySet().iterator();
	}
}
