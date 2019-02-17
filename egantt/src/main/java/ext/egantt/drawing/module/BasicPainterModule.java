package ext.egantt.drawing.module;

import com.egantt.drawing.painter.basic.BasicArcPainter;
import com.egantt.drawing.painter.basic.BasicBorderPainter;
import com.egantt.drawing.painter.basic.BasicLinePainter;
import com.egantt.drawing.painter.basic.BasicOvalPainter;
import com.egantt.drawing.painter.basic.BasicStringPainter;
import com.egantt.drawing.painter.filled.FilledArcPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import ext.egantt.drawing.DrawingModule;

public class BasicPainterModule implements DrawingModule{

	private static final String THIS = BasicPainterModule.class.getName();
	
	public static final Object BASIC_BORDER_PAINTER = THIS + "-BasicBorderPainter";
	public static final Object BASIC_LINE_PAINTER = THIS + "-BasicLinePainter";
	public static final Object BASIC_OVAL_PAINTER = THIS + "-BasicOvalPainter";	
	public static final String BASIC_ARC_PAINTER_OPEN = THIS + "-BasicArcPainterOPEN";
	public static final String BASIC_ARC_PAINTER_CHORD = THIS + "-BasicArcPainterCHORD";
	public static final String BASIC_ARC_PAINTER_PIE = THIS + "-BasicArcPainterPIE";
	public static final String BASIC_STRING_PAINTER = THIS + "-BasicStringPainter";

	public void initialise(DrawingContext attributes) {
		attributes.put(BASIC_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, new BasicArcPainter(FilledArcPainter.OPEN));
		attributes.put(BASIC_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, new BasicArcPainter(FilledArcPainter.CHORD));
		attributes.put(BASIC_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, new BasicArcPainter(FilledArcPainter.PIE));
		attributes.put(BASIC_BORDER_PAINTER, ContextResources.DRAWING_PAINTER, new BasicBorderPainter());
		// put(BASIC_ICON_PAINTER, ContextResources.DRAWING_PAINTER, new BasicIconPainter());
		attributes.put(BASIC_LINE_PAINTER, ContextResources.DRAWING_PAINTER, new BasicLinePainter());
		attributes.put(BASIC_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, new BasicOvalPainter());
		attributes.put(BASIC_STRING_PAINTER, ContextResources.DRAWING_PAINTER, new BasicStringPainter());
	}

	public void terminate(DrawingContext attributes) {
		attributes.put(BASIC_ARC_PAINTER_OPEN, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BASIC_ARC_PAINTER_CHORD, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BASIC_ARC_PAINTER_PIE, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BASIC_BORDER_PAINTER, ContextResources.DRAWING_PAINTER, null);
		// put(BASIC_ICON_PAINTER, ContextResources.DRAWING_PAINTER, new BasicIconPainter());
		attributes.put(BASIC_LINE_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BASIC_OVAL_PAINTER, ContextResources.DRAWING_PAINTER, null);
		attributes.put(BASIC_STRING_PAINTER, ContextResources.DRAWING_PAINTER, null);
	}
	
}