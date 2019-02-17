/**
 * @(#)BasicPainterContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.drawing.painter.context;

import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.drawing.DrawingPainter;

import com.egantt.awt.graphics.GraphicsState;

import com.egantt.drawing.painter.range.RangeModel;

import com.egantt.model.drawing.DrawingGranularity;

import com.egantt.model.drawing.painter.PainterResources;

import java.awt.Composite;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import java.awt.geom.AffineTransform;

import java.text.Format;

import javax.swing.border.Border;

/**
 *  A wrapper around the default implementation of the PainterContext intendend
 *  to provide an easy to use context for beginners, or for experimentation
 *  contains a set method for every type contained in the ContextResources
 */
public class BasicPainterContext extends com.egantt.awt.graphics.context.BasicGraphicsContext
{
	// __________________________________________________________________________

	public void setBorder(Border border)
	{
		put(PainterResources.BORDER, border);
	}

	public void setFormat(Format format)
	{
		put(PainterResources.FORMAT, format);
	}

	public void setShape(Shape shape)
	{
		put(PainterResources.SHAPE, shape);
	}

	public void setFont(Font font)
	{
		put(PainterResources.FONT, font);
	}

	public void setComposite(Composite composite)
	{
		put(PainterResources.COMPOSITE, composite);
	}

	public void setTransform(AffineTransform transform)
	{
		put(PainterResources.TRANSFORM, transform);
	}

	public void setPaint(Paint paint)
	{
		put(PainterResources.PAINT, paint);
	}

	public void setStroke(Stroke stroke)
	{
		put(PainterResources.STROKE, stroke);
	}

	public void setDrawingGranularity(DrawingGranularity granularity)
	{
		put(PainterResources.GRANULARITY, granularity);
	}

	public void setDrawingGraphics(GraphicsManager drawingGraphics)
	{
		put(PainterResources.DRAWING_GRAPHICS, drawingGraphics);
	}

	public void setDrawingPainter(DrawingPainter drawingPlotter)
	{
		put(PainterResources.DRAWING_PAINTER, drawingPlotter);
	}

	public void setInsets(Insets insets)
	{
		put(PainterResources.INSETS, insets);
	}

	public void setGraphicsState(GraphicsState graphicsState)
	{
		put(PainterResources.GRAPHICS_STATE, graphicsState);
	}

	public void setRangeModel(RangeModel rangeModel)
	{
		put(PainterResources.RANGE_MODEL, rangeModel);
	}
}
