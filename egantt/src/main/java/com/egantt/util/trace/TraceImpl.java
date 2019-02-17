/*
 * @(#)TraceImpl.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.util.trace;

import java.io.PrintStream;

/**
 *  Instances of this interface control trace messages with-in the application
 *  you may implement this interface to filter out trace messages to a different
 *  PrintWriter.
 */
public interface TraceImpl
{
	/**
	  *  Tracing enabled for this class
	  */
	public boolean getEnable(String clazzName);

	/**
	  * PrintStream used for standard logging
	  * this defaults to System.out
	  */
	public PrintStream getPrintStream();
}
