/*
 *
 */

package ext.egantt.chart.generator;

import com.egantt.drawing.component.painter.part.BasicPartPainter;
import com.egantt.drawing.painter.filled.FilledArcPainter;
import com.egantt.model.drawing.*;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.LongAxis;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.context.BasicDrawingContext;
import com.egantt.model.drawing.painter.state.BasicPainterState;
import com.egantt.model.drawing.part.ListDrawingPart;
import ext.egantt.chart.ChartGenerator;
import ext.egantt.chart.ChartModel;
import ext.egantt.chart.scheme.BasicPaintScheme;
import ext.egantt.drawing.painter.context.BasicPainterContext;
import java.awt.Color;
import java.util.Iterator;

public class PieGenerator2D
    implements ChartGenerator
{

    public PieGenerator2D()
    {
    }

    public DrawingContext getDrawingContext()
    {
        BasicDrawingContext context = new BasicDrawingContext();
        context.put("painter", ContextResources.DRAWING_PAINTER, new FilledArcPainter(2));
        BasicPainterContext painterContext = new BasicPaintScheme();
        context.put("context", ContextResources.GRAPHICS_CONTEXT, painterContext);
        painterContext = new BasicPainterContext();
        painterContext.put("Paint", Color.black);
        context.put("context", ContextResources.GRAPHICS_CONTEXT, painterContext);
        BasicPartPainter partPainter = new BasicPartPainter();
        context.put("painter", ContextResources.PART_PAINTER, partPainter);
        context.put("state", ContextResources.PAINTER_STATE, new BasicPainterState());
        return context;
    }

    public DrawingPart getDrawingPart(ChartModel model, DrawingContext context)
    {
        Object keys[] = new String[2];
        keys[0] = "xAxis";
        keys[1] = "yAxis";
        LongAxis axis = new LongAxis();
        axis.setInterval(new LongInterval(0L, 100L));
        context.put(keys[0], ContextResources.AXIS_VIEW, Integer.valueOf(0));
        axis = new LongAxis();
        axis.setInterval(new LongInterval(0L, 100L));
        context.put(keys[1], ContextResources.AXIS_VIEW, Integer.valueOf(1));
        ListDrawingPart part = new ListDrawingPart(keys, "painter");
        AxisInterval intervals[] = new AxisInterval[2];
        intervals[0] = new LongInterval(10L, 90L);
        intervals[1] = new LongInterval(10L, 90L);
        for(Iterator iter = model.keys(); iter.hasNext(); part.add(model.getInterval(null, iter.next()), intervals, "painter", "state", "context"));
        return part;
    }

    protected BasicDrawingContext context;
}
