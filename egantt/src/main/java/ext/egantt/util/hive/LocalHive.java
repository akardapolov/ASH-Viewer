/*
 * @(#)LocalHive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import com.egantt.util.Trace;

import java.io.File;

import java.net.URL;

/**
 * This is a utility class used to find the CLASSDATA directory for this project,
 * from the CLASSPATH
 */
public class LocalHive extends AbstractHive
{
	/**
	 *  As this instance of the hive may or may not be initialised it is safest to
	 *  just set this flag in the code if you need to debug what is going wrong
	 */
	private static final String THIS = LocalHive.class.getName();
	private static final boolean TRACE = true;
	
	/**
	 *  this the order of precedence of the keys of which to retrieve the file
	 */
	protected String path;
	
	/**
	 * this is the file that we are looking for by iterating through
	 * the ClassPath directories.  This file will be located in the root
	 * directory relative to other important items, like images.  When we
	 * find this file, we find the root for the project really.
	 */
	public LocalHive(String baseFile, String protocol)
	{
		this.path = protocol + "://" +getLocation(baseFile, System.getProperty("java.class.path"));
		this.path = path.replace('\\', '/');
	}
	
	/**
	 *  Construct the hive using the local file protocol
	 */
	public LocalHive(String baseFile)
	{
		this (baseFile, "file");
	}
	
	// __________________________________________________________________________
	
	protected URL createURL(String fragment) throws Throwable
	{
		return new URL(path + "/" + fragment);
	}
	
	// __________________________________________________________________________
	
	/**
	 * Gets a complete listing of the ClassPath.  This is used to build the URL which
	 * will be returned by getURL
	 */
	protected String getLocation(String filename, String classPath)
	{
		for (int i=0; i >= 0;)
		{
			// position of the seperator character in the classpath
			int j = classPath.indexOf(File.pathSeparatorChar, i);
			
			String item = (j < 0) ? classPath.substring(i) : classPath.substring(i, j);
			i = (j < 0) ? -1 : j +1;
			
			if (TRACE) Trace.out.println(item);
			
			// attempt to locate a file that matches the item
			File f = new File(item);
			if (f.isDirectory())
			{
				// the classpath specifies a directory, try to open the file
				// CLASSDATA_INFO, relative to the directory
				if (new File(f, filename).isFile())
				{
					if (TRACE) Trace.out.println(THIS + ".getLocation " +
								"found file: " + item + "/" + filename);
					return item; // File found success
				}
			}
		}
		return null; // we did not find the CLASSDATA_INFO file, give up
	}
}
