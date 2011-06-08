/*
 * @(#)EmptyElementFilter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data.filter.dom;

import com.egantt.data.Filter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EmptyElementFilter implements Filter
{
	// _________________________________________________________________________

	public boolean include(Object o)
	{
		Node node = (Node) o;
		if (!(node instanceof Element))
			return false;

		return node.getAttributes().getLength() > 0;
	}
}
