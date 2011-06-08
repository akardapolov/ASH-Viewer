/*
 * @(#)CompoundContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.context;

import com.egantt.awt.graphics.GraphicsContext;

public interface CompoundContext extends GraphicsContext
{
	/**
	 *  Returns a Map which provides easy access for user based objects
	 */
	Object get(Object key, Object group, Object name);
}
