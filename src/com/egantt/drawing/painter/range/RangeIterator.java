/*
 * @(#)RangeIterator.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.range;

import java.util.Iterator;

public interface RangeIterator extends Iterator
{
	/**
	 *  returns a unique key, which reflects the position which has been reached
	 *  from with-in the iterators data structure
	 */
	Object getKey();
}
