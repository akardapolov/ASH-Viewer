/**
 * @(#)PartPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.component.painter;

import java.awt.Component;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.swing.cell.CellState;

public interface PartPainter
{
	void paint(Component c, DrawingPart part, GraphicsState graphics, CellState cellState, DrawingContext context);
}
