package ext.egantt.drawing.module;


import com.egantt.drawing.painter.axis.AxisPercentagePainter;
import com.egantt.drawing.painter.gantt.GanttTaskPainter;
import com.egantt.drawing.painter.gantt.LinkTaskPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import ext.egantt.drawing.DrawingModule;
import ext.egantt.swing.GanttTable;
	// Bounded Painters 

public class GanttDrawingModule implements DrawingModule {
	private static final String THIS = GanttDrawingModule.class.getName();
	
	public static final String SHIFT_ENTRY_PLOTTER = THIS + "-ShiftEntryPlotter";
	public static final String LINK_ENTRY_PLOTTER = THIS + "-LinkEntryPlotter";
	public static final String AXIS_PERCENTAGE_PAINTER = THIS + "-AxisPercentagePainter";
	
	public void initialise(DrawingContext attributes) {
		attributes.put(LINK_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, new LinkTaskPainter());
		attributes.put(SHIFT_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER,new GanttTaskPainter());
		attributes.put(AXIS_PERCENTAGE_PAINTER, ContextResources.DRAWING_PAINTER, new AxisPercentagePainter(GanttTable.PERCENTAGE_AXIS));			
	}

	public void terminate(DrawingContext attributes) {	
		attributes.put(LINK_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(SHIFT_ENTRY_PLOTTER, ContextResources.DRAWING_PAINTER,null);
		attributes.put(AXIS_PERCENTAGE_PAINTER, ContextResources.DRAWING_PAINTER, null);
	}
}