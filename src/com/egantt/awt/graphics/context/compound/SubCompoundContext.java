/*
 * @(#)SubCompoundContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.context.compound;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.awt.graphics.context.CompoundContext;

public class SubCompoundContext implements GraphicsContext
{
	protected final CompoundContext context;
	protected final Object group;
	
	public SubCompoundContext(Object group, CompoundContext context)
	{
		this.context = context;
		this.group = group;
	}
	
	// __________________________________________________________________________

	public Object get(Object key, Object type)
	{
		return context.get(key, type, group);
	}

	public Object get(Object key, Object type, Object group)
	{
		return context.get(key, type, group);
	}
}
