/*
 * @(#)GraphicsContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics;

public interface GraphicsContext
{
	/**
	 * Returns a value which provides easy access for user based objects
	 */
	Object get(Object key, Object type);
}
