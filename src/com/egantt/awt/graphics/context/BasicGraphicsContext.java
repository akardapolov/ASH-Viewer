/*
 * @(#)BasicGraphicsContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.context;

import com.egantt.awt.graphics.GraphicsContext;

import java.util.HashMap;
import java.util.Map;

/**
  * A very simple implementation a trivial map
  */
public class BasicGraphicsContext implements GraphicsContext
{
	protected static final String DEFAULT_GROUP = "defaultGroup";

	protected Map <Object, Object>attributes = new HashMap<Object, Object>();

	//___________________________________________________________________________

	public Object get(Object key, Object type)
	{
		return this.attributes.get(type);
	}

	public boolean put(Object type, Object value)
	{
		this.attributes.put(type, value);
		return true;
	}
}
