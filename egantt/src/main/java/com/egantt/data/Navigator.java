/*
 * @(#)Navigator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data;

/**
 *  A Navigator is a way to work through a list of objects:
 *  it works by storing a current position and will move through the list
 *  based on the position that you give it
 */
public interface Navigator
{
	/**
	 *  Returns the value at the current element
	 */
	Object current();
	
	/**
	 *  Moves the cursor forward +1 to the next element and returns it's value
	 */
	Object next();
	
	/**
	 *  Moves the cursor back -1 to the previous element and returns it's value
	 */
	Object previous();
	
	// _________________________________________________________________________
	
	/**
	 *  Moves to the first element in the list
	 */
	Object first();
	
	/**
	 *  Moves to the last element in the list
	 */
	Object last();
}
