/**
 * @(#)CalendarDrawingState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.drawing.state;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;

/**
 *  Used as quick mechanism for the header renderer in the table
 */
public class CalendarDrawingState extends BasicDrawingState
{
	public static final String PART_PAINTER = "TimelinePartPainter";

	public static String painters[] = new String [2];
	static
	{
		painters[0] = "-line";
		painters[1] = "-text";
	}

	public static String TIMELINE_TOP = "TimelineTop";
	public static String TIMELINE_BOTTOM = "TimelineBottom";

	public static String LINE_PAINTER = "-line";
	public static String TEXT_PAINTER = "-text";

	public CalendarDrawingState(String keys [])
	{
		for (int i=0; i < painters.length; i++)
		{
			AxisInterval intervals [] = new AxisInterval[2];
			intervals[0] = null;
			intervals[1] = new LongInterval(5,50);

			String key = TIMELINE_TOP + painters[i];

			ListDrawingPart drawingPart = new ListDrawingPart(keys, key);
			drawingPart.add(new Object(), intervals, key, key, key);

			intervals = new AxisInterval[2];
			intervals[0] = null;
			intervals[1] = new LongInterval(50, 95);

			key = TIMELINE_BOTTOM + painters[i];
			drawingPart.add(new Object(), intervals, key, key, key);

			addDrawingPart(drawingPart);
		}
	}
}
