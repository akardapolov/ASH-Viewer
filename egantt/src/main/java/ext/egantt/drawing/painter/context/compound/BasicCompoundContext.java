/*
 * @(#)BasicCompoundContext.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.drawing.painter.context.compound;

import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.drawing.DrawingPainter;

import com.egantt.awt.graphics.GraphicsState;

import com.egantt.drawing.painter.range.RangeModel;

import com.egantt.model.drawing.DrawingGranularity;

import com.egantt.model.drawing.painter.PainterResources;

import java.awt.Composite;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import java.awt.geom.AffineTransform;

import java.text.Format;

/**
 * 
 */
public class BasicCompoundContext extends
	com.egantt.awt.graphics.context.compound.BasicCompoundContext
{
	// __________________________________________________________________________
	
	public void setFormat(Object group, Format format)
	{
		put(PainterResources.FORMAT, group, format);
	}
	
	public void setFormat(Format format)
	{
		put(PainterResources.FORMAT, format);
	}
	
	// _________________________________________________________________________
	
	public void setInsets(Object group, Insets insets)
	{
		put(PainterResources.INSETS, group, insets);
	}
	
	public void setInsets(Insets insets)
	{
		put(PainterResources.INSETS, insets);
	}
	
	// __________________________________________________________________________
	
	public void setShape(Object group, Shape shape)
	{
		put(PainterResources.SHAPE, group, shape);
	}
	
	public void setShape(Shape shape)
	{
		put(PainterResources.SHAPE, shape);
	}
	
	// __________________________________________________________________________
	
	public void setFont(Object group,  Font font)
	{
		put(PainterResources.FONT, group, font);
	}
	
	public void setFont(Font font)
	{
		put(PainterResources.FONT, font);
	}

	// __________________________________________________________________________
	
	public void setComposite(Object group,  Composite composite)
	{
		put(PainterResources.COMPOSITE, group, composite);
	}
	
	public void setComposite(Composite composite)
	{
		put(PainterResources.COMPOSITE, composite);
	}
	
	// __________________________________________________________________________
	
	public void setTransform(Object group,  AffineTransform transform)
	{
		put(PainterResources.TRANSFORM, group, transform);
	}
	
	public void setTransform(AffineTransform transform)
	{
		put(PainterResources.TRANSFORM, transform);
	}
	
	// __________________________________________________________________________
	
	public void setPaint(Object group,  Paint paint)
	{
		put(PainterResources.PAINT, group, paint);
	}
	
	public void setPaint(Paint paint)
	{
		put(PainterResources.PAINT, paint);
	}
	
	// __________________________________________________________________________
	
	public void setStroke(Object group,  Stroke stroke)
	{
		put(PainterResources.STROKE, group, stroke);
	}
	
	public void setStroke(Stroke stroke)
	{
		put(PainterResources.STROKE, stroke);
	}
	
	// __________________________________________________________________________
	
	public void setDrawingGranularity(Object group,  DrawingGranularity granularity)
	{
		put(PainterResources.GRANULARITY, group, granularity);
	}
	
	public void setDrawingGranularity(DrawingGranularity granularity)
	{
		put(PainterResources.GRANULARITY, granularity);
	}
	
	// __________________________________________________________________________
	
	public void setDrawingGraphics(Object group,  GraphicsManager drawingGraphics)
	{
		put(PainterResources.DRAWING_GRAPHICS, group, drawingGraphics);
	}
	
	public void setDrawingGraphics(GraphicsManager drawingGraphics)
	{
		put(PainterResources.DRAWING_GRAPHICS, drawingGraphics);
	}
	
	// __________________________________________________________________________
	
	public void setDrawingPainter(Object group, DrawingPainter drawingPlotter)
	{
		put(PainterResources.DRAWING_PAINTER, group, drawingPlotter);
	}
	
	public void setDrawingPlotter(DrawingPainter drawingPlotter)
	{
		put(PainterResources.DRAWING_PAINTER, drawingPlotter);
	}
	
	// __________________________________________________________________________
	
	public void setGraphicsState(Object group,  GraphicsState graphicsState)
	{
		put(PainterResources.GRAPHICS_STATE, group, graphicsState);
	}
	
	public void setGraphicsState(GraphicsState graphicsState)
	{
		put(PainterResources.GRAPHICS_STATE, graphicsState);
	}
	
	// __________________________________________________________________________
	
	public void setRangeModel(Object group,  RangeModel rangeModel)
	{
		put(PainterResources.RANGE_MODEL, group, rangeModel);
	}
	
	public void setRangeModel(RangeModel rangeModel)
	{
		put(PainterResources.RANGE_MODEL, rangeModel);
	}
}
