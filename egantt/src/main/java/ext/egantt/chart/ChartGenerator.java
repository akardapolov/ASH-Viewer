package ext.egantt.chart;

import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;

public interface ChartGenerator
{
	/**
	 *  Generates a <code>DrawingContext</code> from the scheme
	 */
	DrawingContext getDrawingContext();
	
	/**
	 *  Generates a <code>DrawingPart</code> from the scheme
	 */
	DrawingPart getDrawingPart(ChartModel model, DrawingContext context);
}