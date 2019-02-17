/*
 * @(#)CellState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell;

import java.awt.Rectangle;

import com.egantt.model.drawing.DrawingState;

public interface CellState
{
	// __________________________________________________________________________

	boolean isSelected();

	boolean hasFocus();

	// __________________________________________________________________________

	int getColumn();

	int getRow();

	// __________________________________________________________________________

	Object getSource();

	Object getValue();
	
	DrawingState getDrawing();
	
	// __________________________________________________________________________

	Rectangle getBounds();
}
