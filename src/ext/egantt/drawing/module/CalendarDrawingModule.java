/*
 *
 */

package ext.egantt.drawing.module;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.manager.BasicGraphicsManager;
import com.egantt.drawing.component.painter.PartPainter;
import com.egantt.drawing.component.painter.part.BasicPartPainter;
import com.egantt.drawing.component.painter.part.PartView;
import com.egantt.drawing.component.painter.part.view.BasicPartView;
import com.egantt.drawing.painter.RangePainter;
import com.egantt.drawing.painter.range.model.GranularityRangeModel;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.axis.view.ViewResources;
import ext.egantt.drawing.DrawingModule;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

// Referenced classes of package ext.egantt.drawing.module:
//            TextCalendarModule, LineCalendarModule

public class CalendarDrawingModule
    implements DrawingModule
{
    protected final class LocalGranularityRangeModel extends GranularityRangeModel
    {

        protected boolean accepts(Object key, Object gran, Graphics g, Rectangle bounds)
        {
            if(bounds.width == 0)
            {
                return false;
            } else
            {
                RangePainter partPainter = (RangePainter)attributes.get((new StringBuilder()).append(CalendarDrawingModule.TIMELINE_TOP).append(CalendarDrawingModule.TEXT_PAINTER).toString(), ContextResources.DRAWING_PAINTER);
                GraphicsContext context = (GraphicsContext)attributes.get((new StringBuilder()).append(CalendarDrawingModule.TIMELINE_TOP).append(CalendarDrawingModule.TEXT_PAINTER).toString(), ContextResources.GRAPHICS_CONTEXT);
                long width = partPainter.width(key, gran, g, bounds, context);
                return width <= (long)bounds.width;
            }
        }

        final DrawingContext attributes;
        final CalendarDrawingModule this$0;

        public LocalGranularityRangeModel(int axisKey, int offset, DrawingContext attributes)
        {
        	super(axisKey, offset);
            this$0 = CalendarDrawingModule.this;
            this.attributes = attributes;
        }
    }


    public CalendarDrawingModule()
    {
        orientation = ViewResources.HORIZONTAL.intValue();
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
    
    public void initialise(DrawingContext attributes)
    {
        loadTextModule(TIMELINE_TOP, 2, false, attributes);
        loadLineModule(TIMELINE_TOP, 2, true, attributes);
        loadTextModule(TIMELINE_BOTTOM, 1, true, attributes);
        loadLineModule(TIMELINE_BOTTOM, 1, true, attributes);
    }

    public void terminate(DrawingContext drawingcontext)
    {
    }

    protected PartPainter createPainter(PartView view, GraphicsManager graphics)
    {
        BasicPartPainter painter = new BasicPartPainter();
        return painter;
    }

    protected void loadTextModule(String key, int offset, boolean value, DrawingContext attributes)
    {
        GranularityRangeModel model = new LocalGranularityRangeModel(orientation, offset, attributes);
        TextCalendarModule module = new TextCalendarModule((new StringBuilder()).append(key).append(TEXT_PAINTER).toString(), model, value);
        module.setGraphics(graphics);
        module.initialise(attributes);
        PartView view = new BasicPartView();
        attributes.put((new StringBuilder()).append(key).append(TEXT_PAINTER).toString(), ContextResources.PART_PAINTER, createPainter(view, graphics));
    }

    protected void loadLineModule(String key, int offset, boolean value, DrawingContext attributes)
    {
        GranularityRangeModel model = new LocalGranularityRangeModel(orientation, offset, attributes);
        LineCalendarModule module = new LineCalendarModule((new StringBuilder()).append(key).append(LINE_PAINTER).toString(), model);
        module.setGraphics(graphics);
        module.initialise(attributes);
        PartView view = new BasicPartView();
        attributes.put((new StringBuilder()).append(key).append(LINE_PAINTER).toString(), ContextResources.PART_PAINTER, createPainter(view, graphics));
    }

    public static String TIMELINE_BOTTOM = "TimelineBottom";
    public static String TIMELINE_TOP = "TimelineTop";
    public static String LINE_PAINTER = "-line";
    public static String TEXT_PAINTER = "-text";
    protected final int orientation;
    protected final GraphicsManager graphics = new BasicGraphicsManager();

}
