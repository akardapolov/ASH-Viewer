/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.drawing.painter.basic.*;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;

public class BasicPainterModule
    implements DrawingModule
{

    public BasicPainterModule()
    {
    }
    
	public void initialise(DrawingContext drawingcontext, List eventList) {
	}

    public void initialise(DrawingContext attributes)
    {
        attributes.put(BASIC_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, new BasicArcPainter(0));
        attributes.put(BASIC_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, new BasicArcPainter(1));
        attributes.put(BASIC_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, new BasicArcPainter(2));
        attributes.put(BASIC_BORDER_PAINTER, ContextResources.DRAWING_PAINTER, new BasicBorderPainter());
        attributes.put(BASIC_LINE_PAINTER, ContextResources.DRAWING_PAINTER, new BasicLinePainter());
        attributes.put(BASIC_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, new BasicOvalPainter());
        attributes.put(BASIC_STRING_PAINTER, ContextResources.DRAWING_PAINTER, new BasicStringPainter());
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(BASIC_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_BORDER_PAINTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_LINE_PAINTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BASIC_STRING_PAINTER, ContextResources.DRAWING_PAINTER, null);
    }

    private static final String THIS = ext.egantt.drawing.module.BasicPainterModule.class.getName();
    public static final Object BASIC_BORDER_PAINTER = (new StringBuilder()).append(THIS).append("-BasicBorderPainter").toString();
    public static final Object BASIC_LINE_PAINTER = (new StringBuilder()).append(THIS).append("-BasicLinePainter").toString();
    public static final Object BASIC_OVAL_PAINTER = (new StringBuilder()).append(THIS).append("-BasicOvalPainter").toString();
    public static final String BASIC_ARC_PAINTER_OPEN = (new StringBuilder()).append(THIS).append("-BasicArcPainterOPEN").toString();
    public static final String BASIC_ARC_PAINTER_CHORD = (new StringBuilder()).append(THIS).append("-BasicArcPainterCHORD").toString();
    public static final String BASIC_ARC_PAINTER_PIE = (new StringBuilder()).append(THIS).append("-BasicArcPainterPIE").toString();
    public static final String BASIC_STRING_PAINTER = (new StringBuilder()).append(THIS).append("-BasicStringPainter").toString();

}
