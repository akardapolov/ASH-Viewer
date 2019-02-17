/*
 * @(#)CalendarConstants.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.model.drawing.granularity;

import java.util.Calendar;

/**
 *  Easy to use constants for the Calendar based granularities
 */
public interface CalendarConstants
{
	Object FORMAT_KEYS [] = new Object []
	{
		new Integer(Calendar.YEAR),
		new Integer(Calendar.MONTH),
		new Integer(Calendar.DAY_OF_MONTH),
		new Integer(Calendar.HOUR),
		new Integer(Calendar.MINUTE),
		new Integer(Calendar.SECOND),
		new Integer(Calendar.MILLISECOND)		
	};
}
