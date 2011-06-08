/*
 * @(#)CellRenderer.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell;

import com.egantt.swing.cell.CellState;

import javax.swing.JComponent;

public interface CellRenderer
{
	JComponent getComponent(CellState state, JComponent parent);
}
