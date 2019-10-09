/*
 *
 */

package ext.egantt.drawing.module;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.state.GraphicsState2D;
import com.egantt.drawing.DrawingPainter;
import com.egantt.drawing.painter.RangePainter;
import com.egantt.drawing.painter.format.BasicFormatPainter;
import com.egantt.drawing.painter.range.BasicRangePainter;
import com.egantt.drawing.painter.range.model.GranularityRangeModel;
import com.egantt.model.drawing.*;
import com.egantt.model.drawing.painter.state.BasicPainterState;
import ext.egantt.drawing.painter.context.compound.BasicCompoundContext;
import ext.egantt.model.drawing.granularity.CachedCalendarGranularity;
import ext.egantt.model.drawing.granularity.CalendarConstants;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

public class TextCalendarModule
{

    public TextCalendarModule(String key, GranularityRangeModel model, boolean formatType)
    {
        this.formatType = formatType;
        this.model = model;
        this.key = key;
        granularity = new CachedCalendarGranularity(1, CalendarConstants.FORMAT_KEYS);
    }

    public void setGraphics(GraphicsManager graphics)
    {
        this.graphics = graphics;
    }

    public void setRangeModel(GranularityRangeModel model)
    {
        this.model = model;
    }

    public void initialise(DrawingContext context)
    {
        context.put(key, ContextResources.GRAPHICS_CONTEXT, createContext());
        context.put(key, ContextResources.DRAWING_PAINTER, createPainter());
        context.put(key, ContextResources.PAINTER_STATE, new BasicPainterState());
    }

    public void terminate(DrawingContext context)
    {
        context.put(key, ContextResources.GRAPHICS_CONTEXT, null);
        context.put(key, ContextResources.DRAWING_PAINTER, null);
        context.put(key, ContextResources.PAINTER_STATE, null);
    }

    protected GraphicsContext createContext()
    {
        BasicCompoundContext context = new BasicCompoundContext();
        context.setDrawingGranularity(granularity);
        context.setFont(font);
        context.setPaint(Color.black);
        context.setFormat(new Integer(13), new SimpleDateFormat(" hh:mm:ss"));
        context.setFormat(new Integer(12), new SimpleDateFormat(formatType ? " hh:mm:'xx'" : " hh:mm:'xx' dd MMM yyyy"));
        context.setFormat(new Integer(10), new SimpleDateFormat(formatType ? " hh:'xx'" : " hh:'xx' dd MMM yyyy"));
        context.setFormat(new Integer(5), new SimpleDateFormat(formatType ? " E dd/m/yy" : " dd MMMM yyyy"));
        context.setFormat(new Integer(4), new SimpleDateFormat(formatType ? "MMM" : " ww MM/yy"));
        context.setFormat(new Integer(3), new SimpleDateFormat(formatType ? "M" : "M yyyy"));
        context.setFormat(new Integer(2), new SimpleDateFormat(formatType ? " MMM" : " MMMM yyyy"));
        context.setFormat(new Integer(1), new SimpleDateFormat(" yyyy"));
        return context;
    }

    protected DrawingPainter createPainter()
    {
        RangePainter painter = new BasicRangePainter(graphics, false);
        painter.setModel(model);
        painter.setPainter(new BasicFormatPainter());
        painter.setState(new GraphicsState2D());
        return painter;
    }

    private static final Font font = new Font("SanSerif", 0, 9);
    protected final DrawingGranularity granularity;
    protected final boolean formatType;
    protected final String key;
    protected GranularityRangeModel model;
    protected GraphicsManager graphics;

}
