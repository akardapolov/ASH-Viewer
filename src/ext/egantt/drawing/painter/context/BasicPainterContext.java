/*
 *
 */

package ext.egantt.drawing.painter.context;

import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.GraphicsState;
import com.egantt.awt.graphics.context.BasicGraphicsContext;
import com.egantt.drawing.DrawingPainter;
import com.egantt.drawing.painter.range.RangeModel;
import com.egantt.model.drawing.DrawingGranularity;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.Format;
import javax.swing.border.Border;

public class BasicPainterContext extends BasicGraphicsContext
{

    public BasicPainterContext()
    {
    }

    public void setBorder(Border border)
    {
        put("Border", border);
    }

    public void setFormat(Format format)
    {
        put("Format", format);
    }

    public void setShape(Shape shape)
    {
        put("Shape", shape);
    }

    public void setFont(Font font)
    {
        put("Font", font);
    }

    public void setComposite(Composite composite)
    {
        put("Composite", composite);
    }

    public void setTransform(AffineTransform transform)
    {
        put("Transform", transform);
    }

    public void setPaint(Paint paint)
    {
        put("Paint", paint);
    }

    public void setStroke(Stroke stroke)
    {
        put("Stroke", stroke);
    }

    public void setDrawingGranularity(DrawingGranularity granularity)
    {
        put("Granularity", granularity);
    }

    public void setDrawingGraphics(GraphicsManager drawingGraphics)
    {
        put("Graphics", drawingGraphics);
    }

    public void setDrawingPainter(DrawingPainter drawingPlotter)
    {
        put("Painter", drawingPlotter);
    }

    public void setInsets(Insets insets)
    {
        put("Insets", insets);
    }

    public void setGraphicsState(GraphicsState graphicsState)
    {
        put("State", graphicsState);
    }

    public void setRangeModel(RangeModel rangeModel)
    {
        put("Model", rangeModel);
    }
}
