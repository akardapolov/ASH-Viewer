/*
 * @(#)UIComponentContext.java
 *
 * Copyright 2002 EGANTT LLP LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.context;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.UIManager;

import com.egantt.swing.component.ComponentContext;

/**
 *  Uses the UIManager, provides a simple way to get the values from the UI
 *  into a ComponentContext
 */
public class UIComponentContext implements ComponentContext
{
	private static final String [] prefixes = {"cell"};
	protected final String type;
	
	public UIComponentContext(String type)
	{
		this.type = type + ".";
	}
	
	// __________________________________________________________________________
	
	public Object get(Object key)
	{
		Object value = UIManager.get(type + key.toString().toLowerCase());
		if (value != null)
			return value;
		
		for (int i=0; i < prefixes.length && value == null; i++)
			value = UIManager.get(type + prefixes[i] + key);
		return value;
	}

	// _________________________________________________________________________
	
	public Iterator keys()
	{
		return new KeyedIterator(type, UIManager.getDefaults().keys());
	}
	
	// _________________________________________________________________________
	
	protected static class KeyedIterator implements Iterator
	{
		protected final String key;
		protected final Enumeration enumerator;
		
		protected Object position;
		
		public KeyedIterator(String key, Enumeration enumerator)
		{
			this.enumerator = enumerator;
			this.key = key;
			
			next(); // move the first element
		}
		
		// _____________________________________________________________________
		
		public boolean hasNext()
		{
			return position != null;
		}
		
		// _____________________________________________________________________
		
		public Object next()
		{
			Object value = position;
			while (enumerator.hasMoreElements())
			{
				Object o = enumerator.nextElement();
				if (!(o instanceof String))
					continue;
				
				String s = (String) o;
				int i = s.indexOf(key);
				if (i < 0)
					continue;
				
				this.position = enumerator.nextElement();
			}
			return value;
		}
		
		// _____________________________________________________________________
		
		public void remove()
		{
		}
	}
 }
