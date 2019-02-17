/*
 * @(#)DummyComponentContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.context;

import com.egantt.swing.component.ComponentContext;

import java.util.Collections;
import java.util.Iterator;

/**
 *  You may wish to use this class, if you have no intention of passing in a context
 */
public class DummyComponentContext implements ComponentContext
{
	public static final ComponentContext instance = new DummyComponentContext();
	
	// _________________________________________________________________________
	
	public Object get(Object key)
	{
		return null;
	}
	
	public Object put(Object key, Object value)
	{
		return null;
	}
	
	// _________________________________________________________________________
	
	public Iterator keys()
	{
		return Collections.EMPTY_SET.iterator();
	}
}
