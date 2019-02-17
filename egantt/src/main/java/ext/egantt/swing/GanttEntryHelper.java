package ext.egantt.swing;

import java.util.Date;
import java.util.List;

import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;

import ext.egantt.drawing.state.CalendarDrawingState;

public class GanttEntryHelper {

	protected final GanttDrawingPartHelper helper;

	public GanttEntryHelper() {
		this(null);
	}

	public GanttEntryHelper(GanttDrawingPartHelper helper) {
		this.helper = helper != null ? helper : new GanttDrawingPartHelper();
	}

	// ________________________________________________________________________

	public DrawingState createCalendar() {
		return new CalendarDrawingState(GanttTable.axises);
	}

	// ________________________________________________________________________

	public DrawingState createActivityEntry(Date startDate, Date endDate) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createActivityEntry(drawingState, startDate, endDate, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public DrawingState createActivityEntry(Date startDate, Date endDate,
			String context) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createActivityEntry(drawingState, startDate, endDate, context,
				part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public DrawingState createActivityEntry(Date startDate, Date endDate,
			String painter, String context) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createActivityEntry(drawingState, startDate, endDate, painter,
				context, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	// ________________________________________________________________________

	public void createLinkEntry(DrawingState start, DrawingState finish) {
		helper.createLinkEntry(start, finish);
	}

	public void createLinkEntry(DrawingState start, DrawingState finish,
			String context) {
		helper.createLinkEntry(start, finish, context);
	}

	// ________________________________________________________________________

	public DrawingState createShiftEntry(List<DrawingState> drawingStates) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);

		helper.createShiftEntry(drawingState, drawingStates, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public DrawingState createShiftEntry(List<DrawingState> drawingStates,
			String context) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);

		helper.createShiftEntry(drawingState, drawingStates, context, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	// ________________________________________________________________________

	public Object createMilestoneEntry(Date date) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createMilestoneEntry(drawingState, date, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public Object createMilestoneEntry(Date date, String painter) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createMilestoneEntry(drawingState, date, painter, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public Object createMilestoneEntry(Date date, String painter, String context) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createMilestoneEntry(drawingState, date, painter, context, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	// ________________________________________________________________________

	public Object createPercentageEntry(int i, Date date, Date date2) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createPercentageEntry(drawingState, i, date, date2, part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}

	public Object createPercentageEntry(int i, Date date, Date date2,
			String context) {
		BasicDrawingState drawingState = helper.createDrawingState();
		ListDrawingPart part = helper.createDrawingPart(false);
		helper.createPercentageEntry(drawingState, i, date, date2, context,
				part);
		drawingState.addDrawingPart(part);
		return drawingState;
	}
}