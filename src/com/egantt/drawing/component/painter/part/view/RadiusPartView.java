/*
 * @(#)RadiusPartView.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.component.painter.part.view;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.AxisInterval;

import java.awt.Component;
import java.awt.Rectangle;

/**
 *  Adds support for treating the intervals as if there value is the radius
 *  of a circle.
 *  The first value being the start point and finish - start being the diameter
 */
public class RadiusPartView extends BasicPartView
{
	public final static int AXIS_VIEW_INTERVAL  = 0;
	public final static int	AXIS_VIEW_RADIUS  = 1;

	protected int policy [];

	// __________________________________________________________________________

	public void setPolicy(int policy [])
	{
		this.policy = policy;
	}

	// __________________________________________________________________________

	public Rectangle create(Component c, AxisInterval intervals [],
		DrawingTransform transforms[])
	{
		Rectangle rect = super.create(c, intervals, transforms);
		if (rect == null || policy == null)
			return rect;

		rect.translate(policy[0] == AXIS_VIEW_INTERVAL ? 0 : 0 - rect.width,
			policy[1] == AXIS_VIEW_INTERVAL ? 0 : 0 - rect.height);

		rect.setSize(policy[0] == AXIS_VIEW_INTERVAL ? rect.width : rect.width * 2,
						policy[1] == AXIS_VIEW_INTERVAL ? rect.height : rect.height * 2);
		return rect;
	}
}
