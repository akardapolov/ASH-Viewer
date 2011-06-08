/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.drawing.component.painter.part.BasicPartPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.painter.state.BasicPainterState;
import ext.egantt.drawing.DrawingModule;

public class StandardDrawingModule
    implements DrawingModule
{

    public StandardDrawingModule()
    {
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
	
    public void initialise(DrawingContext attributes)
    {
        attributes.put(DEFAULT_PAINTER_STATE, ContextResources.PAINTER_STATE, new BasicPainterState());
        attributes.put(DEFAULT_PART_PAINTER, ContextResources.PART_PAINTER, new BasicPartPainter());
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(DEFAULT_PAINTER_STATE, ContextResources.PAINTER_STATE, null);
        attributes.put(DEFAULT_PART_PAINTER, ContextResources.PART_PAINTER, null);
    }

    public static final String THIS = ext.egantt.drawing.module.StandardDrawingModule.class.getName();
    public static final String DEFAULT_PAINTER_STATE = (new StringBuilder()).append(THIS).append("-DefaultPainterState").toString();
    public static final String DEFAULT_PART_PAINTER = (new StringBuilder()).append(THIS).append("-DefaultPartPainter").toString();

}
