/*
 *
 */

package ext.egantt.swing;

import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.DrawingStateInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import ext.egantt.model.drawing.state.BasicDrawingState;
import ext.egantt.drawing.module.*;
import java.util.*;

// Referenced classes of package ext.egantt.swing:
//            GanttTable

public class GanttDrawingPartHelper
{

    public GanttDrawingPartHelper()
    {
        linkDrawingPart = new ListDrawingPart(GanttTable.axises, StandardDrawingModule.DEFAULT_PART_PAINTER, true);
    }

    public BasicDrawingState createDrawingState()
    {
        BasicDrawingState drawingState = new BasicDrawingState();
        drawingState.addDrawingPart(linkDrawingPart);
        return drawingState;
    }

    public ListDrawingPart createDrawingPart(boolean summaryPart)
    {
        ListDrawingPart drawingPart = new ListDrawingPart(GanttTable.axises, StandardDrawingModule.DEFAULT_PART_PAINTER, summaryPart);
        return drawingPart;
    }

    public void createActivityEntry(Object key, Date startDate, Date endDate, ListDrawingPart part)
    {
        createActivityEntry(key, startDate, endDate, "GradientColorContext.RED", part);
    }

    public void createActivityEntry(Object key, Date startDate, Date endDate, String context, ListDrawingPart part)
    {
        createActivityEntry(key, startDate, endDate, "FilledRectanglePainter", context, part);
    }

    public DrawingPart createActivityEntry(Object key, Date startDate, Date endDate, String painter, String context, ListDrawingPart part)
    {
        AxisInterval intervals[] = new AxisInterval[2];
        intervals[0] = new LongInterval(startDate.getTime(), endDate.getTime());
        intervals[1] = new LongInterval(5L, 95L);
        part.add(key, intervals, painter, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
        return part;
    }

    public void createLinkEntry(DrawingState start, DrawingState finish)
    {
        createLinkEntry(start, finish, "GradientColorContext.GREEN");
    }

    public void createLinkEntry(DrawingState start, DrawingState finish, String context)
    {
        ArrayList drawingStates = new ArrayList(2);
        drawingStates.add(start);
        drawingStates.add(finish);
        AxisInterval intervals[] = new AxisInterval[3];
        intervals[0] = null;
        intervals[1] = new LongInterval(0L, 100L);
        intervals[2] = new DrawingStateInterval("xAxis", drawingStates);
        linkDrawingPart.add(new Object(), intervals, GanttDrawingModule.LINK_ENTRY_PLOTTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
    }

    public void createShiftEntry(Object key, List drawingState, ListDrawingPart part)
    {
        createShiftEntry(key, drawingState, "GradientColorContext.BLACK", part);
    }

    public void createShiftEntry(Object key, List drawingState, String context, ListDrawingPart part)
    {
        AxisInterval intervals[] = new AxisInterval[2];
        intervals[0] = new DrawingStateInterval("xAxis", drawingState);
        intervals[1] = new LongInterval(5L, 95L);
        part.add(key, intervals, GanttDrawingModule.SHIFT_ENTRY_PLOTTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
    }

    public void createMilestoneEntry(Object key, Date date, ListDrawingPart part)
    {
        createMilestoneEntry(key, date, BoundedPainterModule.BOUNDED_DIAMOND_PAINTER, part);
    }

    public void createMilestoneEntry(Object key, Date date, String painter, ListDrawingPart part)
    {
        createMilestoneEntry(key, date, painter, "GradientColorContext.BLUE", part);
    }

    public void createMilestoneEntry(Object key, Date date, String painter, String context, ListDrawingPart part)
    {
        AxisInterval intervals[] = new AxisInterval[2];
        intervals[0] = new LongInterval(date.getTime(), 0x7fffffffffffffffL);
        intervals[1] = new LongInterval(17L, 83L);
        part.add(key, intervals, painter, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
    }

    public void createPercentageEntry(Object key, int i, Date date, Date date2, ListDrawingPart part)
    {
        createPercentageEntry(key, i, date, date2, "GradientColorContext.ORANGE", part);
    }

    public void createPercentageEntry(Object key, int i, Date date, Date date2, String context, ListDrawingPart part)
    {
        AxisInterval intervals[] = new AxisInterval[3];
        intervals[0] = new LongInterval(date.getTime(), date2.getTime());
        intervals[1] = new LongInterval(17L, 83L);
        intervals[2] = new LongInterval(0L, i);
        part.add(new Object(), intervals, GanttDrawingModule.AXIS_PERCENTAGE_PAINTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
    }

    protected final ListDrawingPart linkDrawingPart;
}
