/*
 * @(#)DrawingContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

import java.util.Iterator;

/**
 *  <code>DrawingContext</code> is a Map which contains various properties
 */
public interface DrawingContext
{
	// __________________________________________________________________________

	/**
	 *  returns a single Object
	 */
	Object get(Object key, Class type);

	/**
	 *  Adds an element into the DrawingContext
	 */
	void put(Object key, Class type, Object value);

	// __________________________________________________________________________

	/**
	 *  Generates an Iterator to browse the context
	 */
	Iterator iterator(Object key, Class type);
}
