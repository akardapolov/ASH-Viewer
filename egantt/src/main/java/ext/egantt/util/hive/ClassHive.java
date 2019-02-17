/*
 * @(#)ClassHive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import java.net.URL;

/**
 *  Utilises the ClassLoader, expects the contents to be contained with-in the jar
 */
public class ClassHive extends AbstractHive
{
	protected final ClassLoader classLoader;
	
	public ClassHive(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}
	
	// _________________________________________________________________________
	
	/**
	 *  Gets the URL as a resource from with-in the jar
	 *  <code>classLoader.getResource(relativePath);</code>
	 */
	protected URL createURL(String relativePath) throws Throwable
	{
		return classLoader.getResource(relativePath);
	}
}
