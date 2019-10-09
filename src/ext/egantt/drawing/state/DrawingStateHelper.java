/*
 *
 */

package ext.egantt.drawing.state;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import ext.egantt.model.drawing.state.BasicDrawingState;

public class DrawingStateHelper
{

    public DrawingStateHelper()
    {
    }

    public BasicDrawingState createDrawingState(Object key, String partPainter, Object axises[], AxisInterval intervals[], String plotter, String state, String context)
    {
        BasicDrawingState drawingState = new BasicDrawingState();
        ListDrawingPart drawingPart = new ListDrawingPart(axises, partPainter);
        drawingState.addDrawingPart(drawingPart);
        drawingPart.add(key, intervals, plotter, state, context);
        return drawingState;
    }

    public static final DrawingStateHelper instance = new DrawingStateHelper();

}
