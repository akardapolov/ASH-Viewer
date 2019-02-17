/*
 * @(#)BasicElementFilter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.data.filter.dom;

import com.egantt.data.Filter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BasicElementFilter implements Filter
{
	protected final String key;
	protected final String value;

	public BasicElementFilter(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	// _________________________________________________________________________

	public boolean include(Object o)
	{
		Node node = (Node) o;
		if (!(node instanceof Element))
			return false;

		if (!(node.getParentNode() instanceof Element))
			return true;

		Element element = (Element) node.getParentNode();

		if (element.getAttributes().getLength() == 0)
			return true;

		String value = element.getAttribute(key);
		return value.equals(this.value)&& include(element);
	}
}
