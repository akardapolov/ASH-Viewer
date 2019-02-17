package ext.egantt.drawing.module;

import com.egantt.drawing.component.painter.part.BasicPartPainter;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.painter.state.BasicPainterState;

import ext.egantt.drawing.DrawingModule;

public class StandardDrawingModule implements DrawingModule {

	public static final String THIS = StandardDrawingModule.class.getName();
	
	public final static String DEFAULT_PAINTER_STATE = THIS + "-DefaultPainterState";
	public final static String DEFAULT_PART_PAINTER = THIS + "-DefaultPartPainter";
	
	//	________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
		attributes.put(DEFAULT_PAINTER_STATE, ContextResources.PAINTER_STATE, new BasicPainterState());
		attributes.put(DEFAULT_PART_PAINTER, ContextResources.PART_PAINTER, new BasicPartPainter());
	}

	public void terminate(DrawingContext attributes) {
		attributes.put(DEFAULT_PAINTER_STATE, ContextResources.PAINTER_STATE, null);
		attributes.put(DEFAULT_PART_PAINTER, ContextResources.PART_PAINTER, null);
	}
}