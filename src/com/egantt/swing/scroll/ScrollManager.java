/*
 * @(#)ScrollManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.scroll;

import javax.swing.BoundedRangeModel;

/**
 *  For now this interface extends the sun range model interface not ideal
 */
public interface ScrollManager extends BoundedRangeModel
{
	//	________________________________________________________________________
	
	int getBlockIncrement();

	int getUnitIncrement();
}
