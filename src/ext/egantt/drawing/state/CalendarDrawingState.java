/*
 *
 */

package ext.egantt.drawing.state;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import ext.egantt.model.drawing.state.BasicDrawingState;

public class CalendarDrawingState extends BasicDrawingState
{

    public CalendarDrawingState(String keys[])
    {
        for(int i = 0; i < painters.length; i++)
        {
            AxisInterval intervals[] = new AxisInterval[2];
            intervals[0] = null;
            intervals[1] = new LongInterval(5L, 50L);
            String key = (new StringBuilder()).append(TIMELINE_TOP).append(painters[i]).toString();
            ListDrawingPart drawingPart = new ListDrawingPart(keys, key);
            drawingPart.add(new Object(), intervals, key, key, key);
            intervals = new AxisInterval[2];
            intervals[0] = null;
            intervals[1] = new LongInterval(50L, 95L);
            key = (new StringBuilder()).append(TIMELINE_BOTTOM).append(painters[i]).toString();
            drawingPart.add(new Object(), intervals, key, key, key);
            addDrawingPart(drawingPart);
        }

    }

    public static final String PART_PAINTER = "TimelinePartPainter";
    public static String painters[];
    public static String TIMELINE_TOP = "TimelineTop";
    public static String TIMELINE_BOTTOM = "TimelineBottom";
    public static String LINE_PAINTER = "-line";
    public static String TEXT_PAINTER = "-text";

    static 
    {
        painters = new String[2];
        painters[0] = "-line";
        painters[1] = "-text";
    }
}
