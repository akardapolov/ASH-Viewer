/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.drawing.painter.filled.*;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;

public class FilledPainterModule
    implements DrawingModule
{

    public FilledPainterModule()
    {
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
	
    public void initialise(DrawingContext attributes)
    {
        attributes.put("FilledArcPainterOPEN", ContextResources.DRAWING_PAINTER, new FilledArcPainter(0));
        attributes.put("FilledArcPainterCHORD", ContextResources.DRAWING_PAINTER, new FilledArcPainter(1));
        attributes.put("FilledArcPainterPIE", ContextResources.DRAWING_PAINTER, new FilledArcPainter(2));
        attributes.put("FilledOvalPainter", ContextResources.DRAWING_PAINTER, new FilledOvalPainter());
        attributes.put("FilledRectanglePainter", ContextResources.DRAWING_PAINTER, new FilledRectanglePainter());
        attributes.put("FilledShapePainter", ContextResources.DRAWING_PAINTER, new FilledShapePainter());
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put("FilledArcPainterOPEN", ContextResources.DRAWING_PAINTER, null);
        attributes.put("FilledArcPainterCHORD", ContextResources.DRAWING_PAINTER, null);
        attributes.put("FilledArcPainterPIE", ContextResources.DRAWING_PAINTER, null);
        attributes.put("FilledOvalPainter", ContextResources.DRAWING_PAINTER, null);
        attributes.put("FilledRectanglePainter", ContextResources.DRAWING_PAINTER, null);
        attributes.put("FilledShapePainter", ContextResources.DRAWING_PAINTER, null);
    }

    public static final String FILLED_SHAPE_PAINTER = "FilledShapePainter";
    public static final String FILLED_RECTANGLE_PAINTER = "FilledRectanglePainter";
    public static final String FILLED_ARC_PAINTER_OPEN = "FilledArcPainterOPEN";
    public static final String FILLED_ARC_PAINTER_CHORD = "FilledArcPainterCHORD";
    public static final String FILLED_ARC_PAINTER_PIE = "FilledArcPainterPIE";
    public static final String FILLED_OVAL_PAINTER = "FilledOvalPainter";
}
