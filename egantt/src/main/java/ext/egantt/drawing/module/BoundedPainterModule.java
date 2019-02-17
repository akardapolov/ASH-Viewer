package ext.egantt.drawing.module;

import com.egantt.drawing.painter.bounded.BoundedBoxPainter;
import com.egantt.drawing.painter.bounded.BoundedDiamondPainter;
import com.egantt.drawing.painter.bounded.BoundedOvalPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import ext.egantt.drawing.DrawingModule;

public class BoundedPainterModule implements DrawingModule{

	private static final String THIS = BoundedPainterModule.class.getName();
	
	public static final String BOUNDED_BOX_PAINTER = THIS + "-BoundedBoxPainter";
	public static final String BOUNDED_DIAMOND_PAINTER = THIS + "-BoundedDiamondPainter";
	public static final String BOUNDED_OVAL_PAINTER = THIS + "-BoundedOvalPainter";

	//	________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
		attributes.put(BOUNDED_BOX_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedBoxPainter());
		attributes.put(BOUNDED_DIAMOND_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedDiamondPainter());
		attributes.put(BOUNDED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, new BoundedOvalPainter());
	}

	public void terminate(DrawingContext attributes) {
		attributes.put(BOUNDED_BOX_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BOUNDED_DIAMOND_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BOUNDED_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, null);
	}
}
		