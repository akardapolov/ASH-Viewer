/*
 * @(#)BasicHive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import java.net.URL;

/**
 *  The most basic implementation just relies on the contents of the ClassPath
 */
public class BasicHive extends AbstractHive
{
	protected final URL url;

	public BasicHive(URL url)
	{
		this.url = url;
	}

	// _________________________________________________________________________

	protected URL createURL(String relativePath) throws Throwable
	{
		return new URL(url, relativePath);
	}
}
