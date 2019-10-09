/*
 *
 */

package ext.egantt.drawing.painter.context.compound;

import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.DrawingPainter;
import com.egantt.drawing.painter.range.RangeModel;
import com.egantt.model.drawing.DrawingGranularity;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.text.Format;

public class BasicCompoundContext extends com.egantt.awt.graphics.context.compound.BasicCompoundContext
{

    public BasicCompoundContext()
    {
    }

    public void setFormat(Object group, Format format)
    {
        put("Format", group, format);
    }

    public void setFormat(Format format)
    {
        put("Format", format);
    }

    public void setInsets(Object group, Insets insets)
    {
        put("Insets", group, insets);
    }

    public void setInsets(Insets insets)
    {
        put("Insets", insets);
    }

    public void setShape(Object group, Shape shape)
    {
        put("Shape", group, shape);
    }

    public void setShape(Shape shape)
    {
        put("Shape", shape);
    }

    public void setFont(Object group, Font font)
    {
        put("Font", group, font);
    }

    public void setFont(Font font)
    {
        put("Font", font);
    }

    public void setComposite(Object group, Composite composite)
    {
        put("Composite", group, composite);
    }

    public void setComposite(Composite composite)
    {
        put("Composite", composite);
    }

    public void setTransform(Object group, AffineTransform transform)
    {
        put("Transform", group, transform);
    }

    public void setTransform(AffineTransform transform)
    {
        put("Transform", transform);
    }

    public void setPaint(Object group, Paint paint)
    {
        put("Paint", group, paint);
    }

    public void setPaint(Paint paint)
    {
        put("Paint", paint);
    }

    public void setStroke(Object group, Stroke stroke)
    {
        put("Stroke", group, stroke);
    }

    public void setStroke(Stroke stroke)
    {
        put("Stroke", stroke);
    }

    public void setDrawingGranularity(Object group, DrawingGranularity granularity)
    {
        put("Granularity", group, granularity);
    }

    public void setDrawingGranularity(DrawingGranularity granularity)
    {
        put("Granularity", granularity);
    }

    public void setDrawingGraphics(Object group, GraphicsManager drawingGraphics)
    {
        put("Graphics", group, drawingGraphics);
    }

    public void setDrawingGraphics(GraphicsManager drawingGraphics)
    {
        put("Graphics", drawingGraphics);
    }

    public void setDrawingPainter(Object group, DrawingPainter drawingPlotter)
    {
        put("Painter", group, drawingPlotter);
    }

    public void setDrawingPlotter(DrawingPainter drawingPlotter)
    {
        put("Painter", drawingPlotter);
    }

    public void setGraphicsState(Object group, GraphicsState graphicsState)
    {
        put("State", group, graphicsState);
    }

    public void setGraphicsState(GraphicsState graphicsState)
    {
        put("State", graphicsState);
    }

    public void setRangeModel(Object group, RangeModel rangeModel)
    {
        put("Model", group, rangeModel);
    }

    public void setRangeModel(RangeModel rangeModel)
    {
        put("Model", rangeModel);
    }
}
