/*
 * @(#)AbstractHive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import com.egantt.util.Trace;

import java.io.InputStream;

import java.net.URL;

/**
 *   The base class for all hive implementations
 */
public abstract class AbstractHive implements HiveImpl
{	
	// __________________________________________________________________________
	
	public URL getURL(String fragment)
	{
		try
		{
			URL url = createURL(fragment);
			if (url == null)
				return null;
			
			InputStream is = url.openStream();
			if (is != null)
			{
				is.close();
				return url;
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace(Trace.out);
		}
		return null;
	}
	
	/**
	 *  Generates a new URL from the fragment i.e. data/test1.dat -break
	 *  to file://C:/mydata/data/test1.dat
	 */
	protected abstract URL createURL(String fragment) throws Throwable;
}
