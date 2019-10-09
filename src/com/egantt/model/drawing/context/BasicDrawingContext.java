/**
 * @(#)BasicDrawingContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.model.drawing.context;

import com.egantt.model.drawing.DrawingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BasicDrawingContext implements DrawingContext
{
	protected Map <Object, Map<Class,Collection<Object>>> attributes = new HashMap<Object, Map<Class, Collection<Object>>>();

	// __________________________________________________________________________

	public Object get(Object key, Class type)
	{
		Map attribs = (Map) attributes.get(key);
		if (attribs == null)
		{
			return (key != null && key.getClass().isInstance(type)) ? key : null;
		}

		Collection values = (Collection) attribs.get(type);
		return values != null ? values.iterator().next() : null;
	}

	// __________________________________________________________________________

	public Iterator iterator(Object key, Class classType)
	{
		Map attribs = (Map) attributes.get(key);
		if (attribs == null)
		{
			if (key != null && key.getClass().isInstance(classType))
			{
				ArrayList<Object> values  = new ArrayList<Object>();
				values.add(key);
				return values.iterator();
			}
			return Collections.EMPTY_SET.iterator();
		}

		
		Collection values = (Collection) attribs.get(classType);
		return values != null ? values.iterator() : Collections.EMPTY_SET.iterator();
	}

	// __________________________________________________________________________

	public void put(Object key, Class type, Object value) {
		put(key, type, value, false);
	}
	protected void put(Object key, Class type, Object value, boolean append)
	{
		Map<Class, Collection<Object>> attribs = attributes.get(key);
		if (attribs == null)
		{
		   attribs = new HashMap<Class, Collection<Object>>();
		   attributes.put(key, attribs);
		}

		Collection<Object> values = attribs.get(type);
		if (values == null)
		{
		   values = new ArrayList<Object>();
		   attribs.put(type, values);
		}
		if (!append)
			values.clear();
		values.add(value);
	}
}
