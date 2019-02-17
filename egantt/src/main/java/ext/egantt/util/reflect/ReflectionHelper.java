/*
 * @(#)ReflectionHelper.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.reflect;

import com.egantt.util.Trace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReflectionHelper
{
	public static Iterator fields(Class clazz)
	{
		Field fields [] = clazz.getFields();
		List <Object> list = new ArrayList<Object>(fields.length);
		for (int i=0; i < fields.length; i++)
		{
			String a = null;
			try
			{
				a = (String) fields[i].get(new Object());
			}
			catch (Throwable tr)
			{
				tr.printStackTrace(Trace.out);
			}
			
			if (a != null)
				list.add(a);
		}
		return list.iterator();
	}
	
	// __________________________________________________________________________
	
	public static void set(Object o, String field, Object value)
	{
		Method method = findMethod(o.getClass(), "set" + field);
		if (method == null)
			return;
		
		Object values[] = new Object [1];
		values[0] = value;
		
		try
		{
			method.invoke(o, values);
		}
		catch (Exception ex)
		{
			ex.printStackTrace(Trace.out);
		}
	}
	
	// __________________________________________________________________________
	
	protected static Method findMethod(Class clazz, String method)
	{
		Method methods [] = clazz.getMethods();
		
		for (int z=0; z < methods.length; z++)
		{
			if (!methods[z].getName().equals(method))
				continue;
			
			Class params [] = methods[z].getParameterTypes();
			if (params.length == 1)
				return methods[z];
		 }
		 return null;
	 }
}
