/*
 * @(#)BasicFilteredNavigator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data.navigator;

import com.egantt.data.Filter;
import com.egantt.data.Navigator;

public class BasicFilteredNavigator implements Navigator
{
	protected final Navigator navigator;
	protected Filter filter;

	public BasicFilteredNavigator(Navigator navigator)
	{
		this.navigator = navigator;
	}

	// _________________________________________________________________________

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	// _________________________________________________________________________

	public Object current()
	{
		Object current = navigator.current();
		return filter.include(current) ? current : next();
	}

	// _________________________________________________________________________

	public Object first()
	{
		Object first = navigator.first();
		return filter.include(first) ? first : next();
	}

	public Object last()
	{
		Object last = navigator.last();
		return filter.include(last) ? last : previous();
	}

	// _________________________________________________________________________

	public Object next()
	{
		Object next = navigator.next();
		while (next != null && !(filter.include(next)))
			next = navigator.next();
		return next;
	}

	public Object previous()
	{
		Object prev = navigator.next();
		while (prev != null && !(filter.include(prev)))
			prev = navigator.next();
		return prev;
	}
}
