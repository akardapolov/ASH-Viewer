/*
 * @(#)BasicStatePainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.component.painter.state;

import java.awt.Component;
import java.util.Iterator;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.component.DrawingComponentPainter;
import com.egantt.drawing.component.painter.PartPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.swing.cell.CellState;

/**
  * Intent: A 2D implementation of a Drawing, uses the
  * @see com.egantt.model.drawing.DrawingState
  * in order to get out the <code>DrawingPart(s)</code>
  */
public class BasicStatePainter implements DrawingComponentPainter
{
	protected final DrawingContext context;
	
	public BasicStatePainter(DrawingContext context)
	{
		this.context = context;
	}

	// __________________________________________________________________________

	public void paint(Component c, GraphicsState graphics, CellState cellState)
	{
		DrawingState drawing = cellState.getDrawing();
		if (drawing == null)
			return;
		
		drawing.initialise();
		
		for (Iterator parts = drawing.parts(); parts.hasNext();)
		{
			DrawingPart part = (DrawingPart) parts.next();
			
			// paint the part it must exist with-in the DrawingContext
			PartPainter painter = (PartPainter) context.get(part.getPainter(), ContextResources.PART_PAINTER);
			if (painter == null)
				continue;
			
			painter.paint(c, part, graphics, cellState, context);
		}
		
		drawing.terminate();
	}
}
