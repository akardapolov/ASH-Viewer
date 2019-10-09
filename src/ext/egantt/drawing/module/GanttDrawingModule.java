/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.drawing.painter.axis.AxisPercentagePainter;
import com.egantt.drawing.painter.gantt.GanttTaskPainter;
import com.egantt.drawing.painter.gantt.LinkTaskPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;
import ext.egantt.swing.GanttTable;

public class GanttDrawingModule
    implements DrawingModule
{

    public GanttDrawingModule()
    {
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
	
    public void initialise(DrawingContext attributes)
    {
        attributes.put(LINK_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, new LinkTaskPainter());
        attributes.put(SHIFT_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, new GanttTaskPainter());
        attributes.put(AXIS_PERCENTAGE_PAINTER, ContextResources.DRAWING_PAINTER, new AxisPercentagePainter(GanttTable.PERCENTAGE_AXIS));
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(LINK_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(SHIFT_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(AXIS_PERCENTAGE_PAINTER, ContextResources.DRAWING_PAINTER, null);
    }

    private static final String THIS = ext.egantt.drawing.module.GanttDrawingModule.class.getName();
    public static final String SHIFT_ENTRY_PLOTTER = (new StringBuilder()).append(THIS).append("-ShiftEntryPlotter").toString();
    public static final String LINK_ENTRY_PLOTTER = (new StringBuilder()).append(THIS).append("-LinkEntryPlotter").toString();
    public static final String AXIS_PERCENTAGE_PAINTER = (new StringBuilder()).append(THIS).append("-AxisPercentagePainter").toString();


}
