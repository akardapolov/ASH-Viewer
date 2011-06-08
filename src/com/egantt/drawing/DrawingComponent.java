/*
 * @(#)DrawingComponent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing;

import com.egantt.drawing.component.DrawingComponentPainter;
import com.egantt.swing.cell.CellState;

import com.egantt.awt.graphics.GraphicsState;

public interface DrawingComponent
{
	/**
	 * Sets the content for painting
	 */
	void setComponentPainter(DrawingComponentPainter plotter);

	/**
	 * Sets the virtual device, which is used for painting
	 */
	void setGraphicsState(GraphicsState state);

	void setCellState(CellState cellState);
}
