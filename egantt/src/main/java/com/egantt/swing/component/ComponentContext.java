/*
 * @(#)ComponentContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component;

import java.util.Iterator;

public interface ComponentContext
{
	/**
	 *  returns the value based on the key
	 */
	Object get(Object key);
	
	// __________________________________________________________________________
	
	Iterator keys();
}
