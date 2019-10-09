/*
 *
 */

package ext.egantt.chart;

import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;

// Referenced classes of package ext.egantt.chart:
//            ChartModel

public interface ChartGenerator
{

    public abstract DrawingContext getDrawingContext();

    public abstract DrawingPart getDrawingPart(ChartModel chartmodel, DrawingContext drawingcontext);
}
