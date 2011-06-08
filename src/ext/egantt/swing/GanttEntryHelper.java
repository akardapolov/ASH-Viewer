/*
 *
 */

package ext.egantt.swing;

import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.state.BasicDrawingState;
import ext.egantt.drawing.state.CalendarDrawingState;
import java.util.Date;
import java.util.List;

// Referenced classes of package ext.egantt.swing:
//            GanttDrawingPartHelper, GanttTable

public class GanttEntryHelper
{

    public GanttEntryHelper()
    {
        this(null);
    }

    public GanttEntryHelper(GanttDrawingPartHelper helper)
    {
        this.helper = helper == null ? new GanttDrawingPartHelper() : helper;
    }

    public DrawingState createCalendar()
    {
        return new CalendarDrawingState(GanttTable.axises);
    }

    public DrawingState createActivityEntry(Date startDate, Date endDate)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createActivityEntry(drawingState, startDate, endDate, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public DrawingState createActivityEntry(Date startDate, Date endDate, String context)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createActivityEntry(drawingState, startDate, endDate, context, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public DrawingState createActivityEntry(Date startDate, Date endDate, String painter, String context)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createActivityEntry(drawingState, startDate, endDate, painter, context, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public void createLinkEntry(DrawingState start, DrawingState finish)
    {
        helper.createLinkEntry(start, finish);
    }

    public void createLinkEntry(DrawingState start, DrawingState finish, String context)
    {
        helper.createLinkEntry(start, finish, context);
    }

    public DrawingState createShiftEntry(List drawingStates)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createShiftEntry(drawingState, drawingStates, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public DrawingState createShiftEntry(List drawingStates, String context)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createShiftEntry(drawingState, drawingStates, context, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public Object createMilestoneEntry(Date date)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createMilestoneEntry(drawingState, date, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public Object createMilestoneEntry(Date date, String painter)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createMilestoneEntry(drawingState, date, painter, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public Object createMilestoneEntry(Date date, String painter, String context)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createMilestoneEntry(drawingState, date, painter, context, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public Object createPercentageEntry(int i, Date date, Date date2)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createPercentageEntry(drawingState, i, date, date2, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    public Object createPercentageEntry(int i, Date date, Date date2, String context)
    {
        BasicDrawingState drawingState = helper.createDrawingState();
        com.egantt.model.drawing.part.ListDrawingPart part = helper.createDrawingPart(false);
        helper.createPercentageEntry(drawingState, i, date, date2, context, part);
        drawingState.addDrawingPart(part);
        return drawingState;
    }

    protected final GanttDrawingPartHelper helper;
}
