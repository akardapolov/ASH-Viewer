/*
 * @(#)Filter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data;

/**
 *  A Filter provides the mechanism to wade through a list of objects with ease
 */
public interface Filter
{
	boolean include(Object o);
}
