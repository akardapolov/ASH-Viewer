/*
 * @(#)Trace.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.util;

import com.egantt.util.trace.TraceImpl;

import java.io.PrintStream;

/**
  * Static wrapper for traceable's, allows user based tracing to be enabled
  */
public class Trace
{
	public static PrintStream out = System.out;

	protected static TraceImpl traceable;

	//____________________________________________________________________

	public static boolean getEnable(String s)
	{
		return traceable != null ? traceable.getEnable(s) : false;
	}

	//___________________________________________________________________________

	/**
	  * Register your traceable here, if you indend to trace this sourcecode
	  * you may call set with null if you do not want to trace
	  */
	public static void setTraceImpl(TraceImpl traceable)
	{
		Trace.traceable = traceable;
		Trace.out = traceable.getPrintStream();
	}
}
