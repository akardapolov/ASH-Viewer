/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.drawing.painter.bounded.*;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;

public class BoundedPainterModule
    implements DrawingModule
{

    public BoundedPainterModule()
    {
    }
    
	public void initialise(DrawingContext drawingcontext, List eventList) {
	}

    public void initialise(DrawingContext attributes)
    {
        attributes.put(BOUNDED_BOX_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedBoxPainter());
        attributes.put(BOUNDED_DIAMOND_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedDiamondPainter());
        attributes.put(BOUNDED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedOvalPainter());
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(BOUNDED_BOX_PAINTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BOUNDED_DIAMOND_PAINTER, ContextResources.DRAWING_PAINTER, null);
        attributes.put(BOUNDED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, null);
    }

    private static final String THIS = ext.egantt.drawing.module.BoundedPainterModule.class.getName();
    public static final String BOUNDED_BOX_PAINTER = (new StringBuilder()).append(THIS).append("-BoundedBoxPainter").toString();
    public static final String BOUNDED_DIAMOND_PAINTER = (new StringBuilder()).append(THIS).append("-BoundedDiamondPainter").toString();
    public static final String BOUNDED_OVAL_PAINTER = (new StringBuilder()).append(THIS).append("-BoundedOvalPainter").toString();

}
