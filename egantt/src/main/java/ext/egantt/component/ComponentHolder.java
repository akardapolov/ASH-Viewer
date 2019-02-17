/*
 * @(#)ComponentHolder.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.component;

import java.awt.Component;

/**
 *  A TableComponent takes in a TableState in-order to provide it with it's
 *  underlying data that it requires
 */
public interface ComponentHolder
{
	/**
	 *  Returns a component based on the specified model
	 */
	Component getComponent();
}
