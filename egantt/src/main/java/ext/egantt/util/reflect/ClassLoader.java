/**
 * @(#)ClassLoader.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.util.reflect;

import com.egantt.util.Trace;

import java.lang.reflect.Constructor;

/**
 *  A very basic ClassLoader: should be used as little as possible
 */
public class ClassLoader
{
	// __________________________________________________________________________

	/**
	 *  Invoke a class that takes no parameters in it's contructor
	 */
	public static Object invoke(String clazzName)
	{
		Object value = null;
		try
		{
		   Class clazz = Class.forName(clazzName);
			value = clazz != null ? clazz.newInstance() : null;
		}
		catch (Exception e)
		{
			e.printStackTrace(Trace.out);
		}
		return value;
	}
		
	/**
	 *  Invoke a class that taks parameters, this code requires the parameters to
	 *  to be in the correct order for the required constructor. For efficiency
	 *  as well as other reasons a best match is not really suitable.
	 */
	public static Object invoke(String clazzName, Object params [])
	{
		try
		{
		   Class clazz = Class.forName(clazzName);
			Constructor constructors [] =  clazz.getConstructors();

			Class params2 [] = new Class [params.length];
			for (int z =0; z < params.length; z++)
				params2 [z] = params[z] != null ? params[z].getClass() : null;

			for (int i=0; i < constructors.length; i++)
			{
				Class[] params1 = constructors[i].getParameterTypes();
				if (params1.length != params2.length)
					continue;
				
				boolean result = true;
				for (int z=0; z < params1.length && result; z++)
					if (params[z] != null && !params1[z].isAssignableFrom(params2[z]))
						result = false;

				if (!result)
					continue;

				return constructors[i].newInstance(params);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(Trace.out);
		}
		return null;
	}
}
