package ext.egantt.chart.generator;

import java.awt.Color;

import com.egantt.drawing.component.painter.part.BasicPartPainter;
import com.egantt.drawing.painter.generator.PathGeneratorPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.LongAxis;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.context.BasicDrawingContext;
import com.egantt.model.drawing.painter.PainterResources;
import com.egantt.model.drawing.painter.state.BasicPainterState;
import com.egantt.model.drawing.part.ListDrawingPart;

import ext.egantt.chart.ChartGenerator;
import ext.egantt.chart.ChartModel;
import ext.egantt.chart.scheme.BasicPaintScheme;
import ext.egantt.drawing.painter.context.BasicPainterContext;

public class LineGenerator2D implements ChartGenerator
{
	protected BasicDrawingContext context;
	
	/**
	 * @see ext.egantt.chart.ChartGenerator#getDrawingContext()
	 */
	public DrawingContext getDrawingContext() 
	{	
		BasicDrawingContext context = new BasicDrawingContext();

		context.put("painter", ContextResources.DRAWING_PAINTER, new PathGeneratorPainter(1, 100));
		//context.put("painter", ContextResources.DRAWING_PAINTER, new BasicShapePainter());

		BasicPainterContext painterContext = new BasicPaintScheme();
		context.put("context", ContextResources.GRAPHICS_CONTEXT, painterContext);

		painterContext = new BasicPainterContext();
		painterContext.put(PainterResources.PAINT, Color.black);
		context.put("context", ContextResources.GRAPHICS_CONTEXT, painterContext);
		
		BasicPartPainter partPainter = new BasicPartPainter();

		context.put("painter", ContextResources.PART_PAINTER, partPainter);
		context.put("state", ContextResources.PAINTER_STATE, new BasicPainterState());
		
		return context;
	}
		
	/**
	 * @see ext.egantt.chart.ChartGenerator#getDrawingPart()
	 */
	public DrawingPart getDrawingPart(ChartModel model, DrawingContext context) 
	{
		Object keys[] = new String[2];
		keys[0] = "xAxis";
		keys[1] = "yAxis";
		
		LongAxis axis = new LongAxis();
		axis.setInterval(new LongInterval(0, 100));

		context.put(keys[0], ContextResources.AXIS_VIEW, axis.getView(AxisView.HORIZONTAL));

		axis = new LongAxis();
		axis.setInterval(new LongInterval(0, 100));
		context.put(keys[1], ContextResources.AXIS_VIEW, axis.getView(AxisView.VERTICAL));
				
		ListDrawingPart part = new ListDrawingPart(keys, "painter");
		AxisInterval intervals [] = new AxisInterval[2];
		intervals[0] = new LongInterval(10, 90);
		intervals[1] = new LongInterval(10, 90);
		
		Object values [] = new Object [10000];
		for (int i=0; i < 9999; i = i +2)
		{
			values[i] = new Long( (long) i);
			values[i+1] = new Long( (long) i * 5);
		}
		values[10] = new Long(999);
		part.add(values, intervals, "painter", "state", "context");
		return part;
	}
}