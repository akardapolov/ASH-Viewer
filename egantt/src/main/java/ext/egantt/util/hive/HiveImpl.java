/*
 * @(#)HiveImpl.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import java.net.URL;

/**
 *  The HiveImpl interface is an exception ot the naming rule used by E-Gantt.
 *  The exception is caused because Hive itself is a static class.
 *
 *  note: Hive is a static class for convenience reasons, HiveImpl does all
 *  of the work for Hive i.e. an implementation for Hive.
 */
public interface HiveImpl
{
	/**
	 *  creates an absolute URL from a relative path
	 */
	URL getURL(String path);
}
