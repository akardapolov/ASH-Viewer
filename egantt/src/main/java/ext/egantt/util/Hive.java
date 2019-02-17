/*
 * @(#)Hive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util;

import ext.egantt.util.hive.HiveImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

/**
 *  The hive is not as such a requirement to use the library, but has become
 *  a very useful utility class for the examples and server frameworks.
 *
 *  The purpose of a hive is to resolve relative paths into absolute paths
 *  with-in user-space. The advantages that this can have is that the same
 *  code to access files with-in an application can be easily ported to use
 *  another set of paths in an application or applet.
 *
 *  When using the E-Gantt SDK you are advised "against" using the Hive the
 *  alternative is true if you are extending E-Gantt Server edition.
 */
public final class Hive
{
	protected static HiveImpl hiveImpl; /** implementation of the hive */
	
	// __________________________________________________________________________
	
	/**
	 *  Set the hive implementation:
	 *  note: for consistency reasons set once "it should be enough"
	 */
	public static void setHiveImpl(HiveImpl hiveImpl)
	{
		Hive.hiveImpl = hiveImpl;
	}
	
	// __________________________________________________________________________
	
	/**
	 *  A convenience method to generate a reader from the URL
	 */
	public static BufferedReader getReader(String urlFragment) throws IOException
	{
		URL url = getURL(urlFragment);
		if (url == null)
			return null;
		
		InputStream inputStream = url.openStream();
		return new BufferedReader(new InputStreamReader(inputStream));
	}
	
	/**
	 *  A forwarder method calls HiveImpl.getURL()
	 */
	public static URL getURL(String urlFragment)
	{
		return hiveImpl != null ? hiveImpl.getURL(urlFragment) : null;
	}
}
