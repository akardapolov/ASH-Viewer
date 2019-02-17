/*
 * @(#)ComponentManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.component;

import javax.swing.JComponent;

import com.egantt.swing.component.ComponentContext;

/**
 *  Nanages a set of components
 */
public interface ComponentManager
{
	/**
	 *  Register a component for updating the screen repainting
	 */
	void registerComponent(JComponent component, ComponentContext componentContext);

	/**
	 *  Unregisters a component, to prevent screen updates
	 */
	void unregisterComponent(JComponent component);
}
