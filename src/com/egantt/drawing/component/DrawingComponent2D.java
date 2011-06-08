/**
 * @(#)DrawingComponent2D.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.component;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.DrawingComponent;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.component.tooltip.ToolTipState;
import com.egantt.swing.component.tooltip.state.DrawingToolTipState;
import com.egantt.util.Trace;

/**
  * Intent: A 2D implementation of a Drawing
  */
public class DrawingComponent2D extends JComponent implements DrawingComponent
{
	private static final long serialVersionUID = -4762638988341558612L;

	protected transient ToolTipState state = new DrawingToolTipState();

	protected DrawingComponentPainter painter;
	protected GraphicsState graphics;
	protected CellState cellState;

	// _________________________________________________________________________

	public void setCellState(CellState cellState) {
		this.cellState = cellState;
	}
	
	public void setComponentPainter(DrawingComponentPainter painter)
	{
		this.painter = painter;
	}
	
	public void setGraphicsState(GraphicsState graphics)
	{
		this.graphics = graphics;
	}

	public void setToolTipState(ToolTipState state)
	{
		this.state = state;
	}

	// __________________________________________________________________________

	public String getToolTipText(MouseEvent event)
	{
		return state == null 
			? super.getToolTipText(event) 
			: state.getToolTipText(event, cellState);
	}

	// _________________________________________________________________________

	/**
	 *  Delegates, the painting of this component to a series of graphics objects'
	 *  known as painters. These painters, do not write to sun graphics instead
	 *  they write to a GraphicsState object which writes everything out in one go.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (isPaintingTile())
			return;

		graphics.initialise(g);

		try
		{
			painter.paint(this, graphics, cellState);
		}
		catch (Throwable throwable)
		{
		   throwable.printStackTrace(Trace.out);
		}

		graphics.terminate(g);
	}
}
