/*
 * @(#)ComponentPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.component;

import java.awt.Component;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.swing.cell.CellState;

public interface DrawingComponentPainter
{
	/**
	 *  Paints on the GraphicsState passed in, the component should be the
	 *  source of the Component.
	 */
	void paint(Component c, GraphicsState state, CellState cellState);
}