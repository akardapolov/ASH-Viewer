/*
 * @(#)ManagedStateCellRenderer.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.renderer.state;

import com.egantt.drawing.DrawingComponent;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.repaint.manager.DrawingRepaintManager;

public class ManagedStateRenderer extends BasicStateRenderer
{
	protected final DrawingRepaintManager manager;

	public ManagedStateRenderer(DrawingComponent drawingComponent,
		DrawingRepaintManager manager, ComponentContext context)
	{
		super (drawingComponent, context);
		this.manager = manager;
	}

	// __________________________________________________________________________

	@Override
	protected void initialize(CellState cellState) {
		super.initialize(cellState);
		
		manager.setDrawingState(cellState.getDrawing());
	}
}
