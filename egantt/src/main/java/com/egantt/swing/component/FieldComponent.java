/*
 * @(#)FieldComponent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component;

import java.awt.Component;

/**
 *
 */
public interface FieldComponent
{
	/**
	 *  Returns the value from the component
	 */
	Object getValue();
	
	/**
	 *  Sets the value in the component
	 */
	void setValue(Object value);
	
	// _________________________________________________________________________
	
	/**
	 *  Returns the underlying component
	 */
	Component getComponent();
}
