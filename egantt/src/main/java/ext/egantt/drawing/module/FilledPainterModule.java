package ext.egantt.drawing.module;

import com.egantt.drawing.painter.filled.FilledArcPainter;
import com.egantt.drawing.painter.filled.FilledOvalPainter;
import com.egantt.drawing.painter.filled.FilledRectanglePainter;
import com.egantt.drawing.painter.filled.FilledShapePainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import ext.egantt.drawing.DrawingModule;

public class FilledPainterModule implements DrawingModule {
	
	public static final String FILLED_SHAPE_PAINTER = "FilledShapePainter";

	public static final String FILLED_RECTANGLE_PAINTER = "FilledRectanglePainter";
		
	public static final String FILLED_ARC_PAINTER_OPEN = "FilledArcPainterOPEN";
	public static final String FILLED_ARC_PAINTER_CHORD = "FilledArcPainterCHORD";
	public static final String FILLED_ARC_PAINTER_PIE = "FilledArcPainterPIE";
	
	public static final String FILLED_OVAL_PAINTER = "FilledOvalPainter";

	//	________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
		// Filled Painters
		attributes.put(FILLED_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, new FilledArcPainter(FilledArcPainter.OPEN));
		attributes.put(FILLED_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, new FilledArcPainter(FilledArcPainter.CHORD));
		attributes.put(FILLED_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, new FilledArcPainter(FilledArcPainter.PIE));
		attributes.put(FILLED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, new FilledOvalPainter());
		attributes.put(FILLED_RECTANGLE_PAINTER, ContextResources.DRAWING_PAINTER, new FilledRectanglePainter());
		attributes.put(FILLED_SHAPE_PAINTER, ContextResources.DRAWING_PAINTER, new FilledShapePainter());
	}

	public void terminate(DrawingContext attributes) {
		attributes.put(FILLED_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, null);
		attributes.put(FILLED_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, null);
		attributes.put(FILLED_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, null);
		attributes.put(FILLED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(FILLED_RECTANGLE_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(FILLED_SHAPE_PAINTER, ContextResources.DRAWING_PAINTER, null);
	}
}	