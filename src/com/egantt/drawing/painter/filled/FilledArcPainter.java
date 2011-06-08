/*
 * @(#)FilledArcPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.filled;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.drawing.DrawingPainter;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.painter.PainterState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import java.awt.geom.Arc2D;

/**
 *  Arc painter is a different instance than the standard shape painter,
 *  as we need to pass in parameters on the fly
 *  the type is constant you should use this to specify the ArcType Arc2D.*;
 */
public class FilledArcPainter implements DrawingPainter
{
    /**
     * The closure type for an open arc with no path segments
     * connecting the two ends of the arc segment.
     */
    public final static int OPEN = Arc2D.OPEN;

    /**
     * The closure type for an arc closed by drawing a straight
     * line segment from the start of the arc segment to the end of the 
     * arc segment.
     */
    public final static int CHORD = Arc2D.CHORD;

    /**
     * The closure type for an arc closed by drawing straight line
     * segments from the start of the arc segment to the center
     * of the full ellipse and from that point to the end of the arc segment.
     */
    public final static int PIE = Arc2D.PIE;
	protected final Arc2D arc;

	public FilledArcPainter(int type)
	{
		this.arc = new Arc2D.Double(type);
	}

	// __________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds, PainterState state, GraphicsContext context)
	{
		AxisInterval  interval = (AxisInterval) key;

		double startAngle = ((Number) interval.getStart()).doubleValue();
		double finishAngle = ((Number) interval.getFinish()).doubleValue();

		arc.setFrame(bounds.x, bounds.y, bounds.width, bounds.height);
		arc.setAngleStart(startAngle);
		arc.setAngleExtent(finishAngle - startAngle);

		Graphics2D g2d = (Graphics2D) g;
		g2d.fill(arc);
		return null;
	}

	public long width(Object key, java.awt.Graphics g, Rectangle bounds, GraphicsContext context)
	{
		return arc.getBounds().width;
	}
}