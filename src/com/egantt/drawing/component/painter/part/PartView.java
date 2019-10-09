/*
 * @(#)PartView.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.component.painter.part;

import com.egantt.model.drawing.DrawingTransform;

import com.egantt.model.drawing.axis.AxisInterval;

import java.awt.Component;
import java.awt.Rectangle;

public interface PartView
{
	Rectangle create(Component c, AxisInterval intervals [], DrawingTransform transforms[]);
}
