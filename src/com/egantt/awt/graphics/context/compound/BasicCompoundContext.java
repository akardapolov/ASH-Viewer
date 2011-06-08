/*
 * @(#)BasicCompoundContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.context.compound;

import com.egantt.awt.graphics.context.CompoundContext;

import java.util.HashMap;
import java.util.Map;

/**
  * A very simple implementation a trivial map
  */
public class BasicCompoundContext implements CompoundContext
{
	protected static final String DEFAULT_GROUP = "defaultGroup";

	protected final Map <Object, Map <String, Object>> attributes = new HashMap<Object, Map <String, Object>>();

	//___________________________________________________________________________

	public boolean put(String type, Object group, Object value)
	{
		Map<String, Object> attrib = this.attributes.get(group);
		if (attrib == null)
		{
		   attrib = new HashMap<String, Object>();
		   this.attributes.put(group, attrib);
		}
		attrib.put(type, value);
		return true;
	}

	public boolean put(String type, Object value)
	{
		return put(type, DEFAULT_GROUP, value);
	}

	// __________________________________________________________________________

	public Object get(Object key, Object type, Object group)
	{
		Map attrib = (Map) attributes.get(group);
		if (attrib == null)
		   return group != DEFAULT_GROUP ? get(key, type, DEFAULT_GROUP) : null;

		Object result = attrib.get(type);
		return result != null || group == DEFAULT_GROUP
			? result : get(key, type, DEFAULT_GROUP);
	}

	public Object get(Object key, Object type)
	{
		return get(key, type, DEFAULT_GROUP);
	}
}
