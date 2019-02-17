/*
 * @(#)ComponentUtilities.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.model.drawing;

import com.egantt.swing.scroll.ScrollManager;

import com.egantt.model.component.ComponentManager;

/**
 *  A holder for a ComponentManager
 */
public interface ComponentUtilities
{
	/**
	 *  Returns a ComponentManager
	 */
	ComponentManager getManager();

	ScrollManager getScrollManager(int index);
}
