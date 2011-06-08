/*
 * @(#)RepaintManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.repaint;

import com.egantt.model.component.ComponentManager;

/**
 *  Allows for the repainting of a component
 */
public interface RepaintManager extends ComponentManager
{
	// _________________________________________________________________________

	/**
	 *  For convenience repaint can be called externally
	 */
	void repaint();
}
