/*
 * @(#)BasicListFilter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data.filter.basic;

import com.egantt.data.Filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicListFilter implements Filter
{
	protected final boolean requireAll;

	protected List <Filter>filters = new ArrayList<Filter>(5);

	public BasicListFilter(boolean requireAll)
	{
		this.requireAll = requireAll;
	}

	// _________________________________________________________________________

	public void add(Filter filter)
	{
		filters.add(filter);
	}

	public void remove(Filter filter)
	{
		filters.remove(filter);
	}

	// _________________________________________________________________________

	public boolean include(Object o)
	{
		for (Iterator iter = filters.iterator(); iter.hasNext();)
			if (!((Filter) iter.next()).include(o))
				return !requireAll;
		return requireAll;
	}
}
