/*
 * @(#)BasicTraceImpl.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.trace;

import com.egantt.util.trace.TraceImpl;

import ext.egantt.util.Hive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.URL;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
  * This class should be made into Abstract Class the implementor of which should register
  * itself with this class to make it the default
  */
public class BasicTraceImpl implements TraceImpl
{
	private static String THIS = BasicTraceImpl.class.getName();
	private static boolean TRACE = false; // value will be setup at the end of the static constructor

	public static final String DELIMITER = "=";
	public static final String TRACE_CONFIG = "trace.cfg";

	public PrintStream out = System.out;

	protected HashMap <String, Boolean> traceEnable = new HashMap<String, Boolean>();
	protected ResourceBundle bundle;

	public BasicTraceImpl()
	{
		URL url = Hive.getURL(TRACE_CONFIG);

		if (url != null)
		{
		   try
		   {
			   InputStream input = url.openStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(input));
				for (String line = line = in.readLine(); line != null; line = in.readLine())
				{
					// split based on the delimiter
					int i = line.indexOf(DELIMITER);
					if (i <= 0)
						continue;

					// line is something like test.jclass=true; name becomes something like test.jclass
				   // value becomes true or false
					String name = line.substring(0, i).trim();
					String value = line.substring(i + 1).trim();

					traceEnable.put(name, Boolean.valueOf(value).booleanValue() ? Boolean.TRUE : Boolean.FALSE);
				}
		      input.close();
		   }
		   catch (IOException io)
		   {  // this exception is not expecting so we must trace :(
				System.out.println(io);
		   }
		}

		// update the Tracing for this class based on the configuration
		TRACE = getEnable(THIS);
	}

	//___________________________________________________________________________

	public boolean getEnable(String s)
	{
		if (TRACE) out.println(THIS + ".getEnable() preforming lookup on key " + s);

		for (int i=0; i >=0; i=s.lastIndexOf('.'))
		{
			Boolean value = (Boolean) traceEnable.get(s);
			if (value != null)
				return value.booleanValue();

		   // next element
			s = s.substring(0, i);
		}
		return false;
	}

	public PrintStream getPrintStream()
	{
		return out;
	}

	//___________________________________________________________________________

	/**
	  * Method has to be synchronized just in case something else could be
	  * writing out to the trace
	  */
	public synchronized void setOut(PrintStream newOut)
	{
		out = newOut;
	}
}
