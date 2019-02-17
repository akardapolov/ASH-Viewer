package ext.egantt.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.DrawingStateInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;

import ext.egantt.drawing.module.BoundedPainterModule;
import ext.egantt.drawing.module.FilledPainterModule;
import ext.egantt.drawing.module.GanttDrawingModule;
import ext.egantt.drawing.module.GradientColorModule;
import ext.egantt.drawing.module.StandardDrawingModule;

public class GanttDrawingPartHelper {
	
	protected final ListDrawingPart linkDrawingPart = new ListDrawingPart(GanttTable.axises, StandardDrawingModule.DEFAULT_PART_PAINTER, true);
	
	
	//	________________________________________________________________________
	
	public BasicDrawingState createDrawingState() {
	
		BasicDrawingState drawingState = new BasicDrawingState();
		drawingState.addDrawingPart(linkDrawingPart);
		return drawingState;
	}
	
	public ListDrawingPart createDrawingPart(boolean summaryPart) {
		// register the part with the state
		ListDrawingPart drawingPart = new ListDrawingPart(GanttTable.axises, StandardDrawingModule.DEFAULT_PART_PAINTER, summaryPart);
		return drawingPart;
	}


	//	________________________________________________________________________
	
	public void createActivityEntry(Object key, Date startDate, Date endDate, ListDrawingPart part) {
		createActivityEntry(key, startDate, endDate, GradientColorModule.RED_GRADIENT_CONTEXT, part);
	}
	
	public void createActivityEntry(Object key, Date startDate, Date endDate, String context, ListDrawingPart part) {
		createActivityEntry(key, startDate, endDate, FilledPainterModule.FILLED_RECTANGLE_PAINTER, context, part);
	}
	
	public DrawingPart createActivityEntry(Object key, Date startDate, Date endDate, String painter, String context, ListDrawingPart part) {
		AxisInterval intervals [] = new AxisInterval [2];
		intervals[0] = new LongInterval(startDate.getTime(), endDate.getTime());
		intervals[1] = new LongInterval(5, 95);

		part.add(key, intervals,  painter , StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
		return part;
	}
	
	//	________________________________________________________________________
	
	public void createLinkEntry(DrawingState start, DrawingState finish) {
		createLinkEntry(start, finish,  GradientColorModule.GREEN_GRADIENT_CONTEXT);
	}
	
	public void createLinkEntry(DrawingState start, DrawingState finish, String context) {
		
		{
			ArrayList <DrawingState>drawingStates = new ArrayList<DrawingState>(2);
			drawingStates.add(start);
			drawingStates.add(finish);
		
			AxisInterval intervals [] = new AxisInterval [3];
			intervals[0] = null;
			intervals[1] = new LongInterval(0, 100);
			intervals[2] = new DrawingStateInterval("xAxis", drawingStates);

			linkDrawingPart.add(new Object(), intervals, GanttDrawingModule.LINK_ENTRY_PLOTTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
		}
	}
	
	//	________________________________________________________________________
	
	public void createShiftEntry(Object key, List <DrawingState> drawingState, ListDrawingPart part) {
		createShiftEntry(key, drawingState, GradientColorModule.BLACK_GRADIENT_CONTEXT, part);
	}
	
	public void createShiftEntry(Object key, List <DrawingState> drawingState, String context, ListDrawingPart part) {
		AxisInterval intervals [] = new AxisInterval [2];
		intervals[0] = new DrawingStateInterval("xAxis", drawingState);
		intervals[1] = new LongInterval(5, 95);

		part.add(key, intervals, GanttDrawingModule.SHIFT_ENTRY_PLOTTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
	}

	//	________________________________________________________________________
	
	public void createMilestoneEntry(Object key, Date date, ListDrawingPart part) {
		createMilestoneEntry(key, date, BoundedPainterModule.BOUNDED_DIAMOND_PAINTER, part);
	}

	public void createMilestoneEntry(Object key, Date date, String painter, ListDrawingPart part) {
		createMilestoneEntry(key, date, painter, GradientColorModule.BLUE_GRADIENT_CONTEXT, part);
	}
	
	public void createMilestoneEntry(Object key, Date date, String painter, String context, ListDrawingPart part) {
		AxisInterval intervals [] = new AxisInterval [2];
		intervals[0] = new LongInterval(date.getTime(), Long.MAX_VALUE);
		intervals[1] = new LongInterval(17, 83);

		part.add(key, intervals, painter, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
	}
	
	//	________________________________________________________________________

	public void createPercentageEntry(Object key, int i, Date date, Date date2, ListDrawingPart part) {
		createPercentageEntry(key, i, date, date2, GradientColorModule.ORANGE_GRADIENT_CONTEXT, part);
	}
	
	public void createPercentageEntry(Object key, int i, Date date, Date date2, String context, ListDrawingPart part) {
		AxisInterval intervals [] = new AxisInterval [3];
		intervals[0] = new LongInterval(date.getTime(), date2.getTime());
		intervals[1] = new LongInterval(17, 83);
		intervals[2] = new LongInterval(0, i);

		part.add(new Object(), intervals, GanttDrawingModule.AXIS_PERCENTAGE_PAINTER, StandardDrawingModule.DEFAULT_PAINTER_STATE, context);
	}
}