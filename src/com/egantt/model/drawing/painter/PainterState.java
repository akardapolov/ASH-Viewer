/*
 * @(#)PainterState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.painter;

/**
 *  A transient state, which is passed around, to all painters which are contained
 *  under the same unique key
 */
public interface PainterState
{
	// __________________________________________________________________________
	
	/**
	 *  initialises the state
	 */
	void initialise();
	
	/**
	 *  terminates the state indicating it's contents are ready to be garbage
	 *  collected at the next chance
	 */
	void terminate();
	
	// __________________________________________________________________________
	
	/**
	 *
	 */
	Object get(Object type);
	
	/**
	 *
	 */
	Object put(Object type, Object value);
}
