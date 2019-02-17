/**
 * @(#)AbstractLinkTaskPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.painter.gantt;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.drawing.DrawingPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.StateResources;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.interval.DrawingStateInterval;
import com.egantt.model.drawing.painter.PainterState;
import com.egantt.swing.cell.CellState;
import com.egantt.util.Trace;
import org.jdesktop.swingx.JXTable;

import java.awt.*;

public abstract class AbstractLinkTaskPainter implements DrawingPainter {
	private static final boolean TRACE = Trace
			.getEnable(AbstractLinkTaskPainter.class.getName());

	protected String axis = "xAxis";

	// ________________________________________________________________________

	public Shape paint(Object key, Graphics g, Rectangle bounds,
			PainterState state, GraphicsContext context) {
		CellState cellState = (CellState) state.get(StateResources.CELL_STATE);
		DrawingPart part = (DrawingPart) state.get(StateResources.DRAWING_PART);

		AxisView view = (AxisView) context
				.get(axis, ContextResources.AXIS_VIEW);
		if (view == null) {
			if (TRACE)
				Trace.out.println("paint: Axis not defined in context [" + axis
						+ "]");
			return bounds;
		}

		JXTable table = (JXTable) cellState.getSource();
		DrawingState drawingState = (DrawingState) table.getValueAt(cellState
				.getRow(), cellState.getColumn());

		DrawingStateInterval interval = (DrawingStateInterval) part
				.getInterval(key, new AxisInterval[] {})[2];

		int startIndex = Integer.MAX_VALUE;
		int finishIndex = Integer.MIN_VALUE;
		for (int i = 0; i < interval.size(); i++) {

			int newIndex = indexOfDrawingState(table, interval.get(i),
					cellState);
			if (newIndex >= 0) {
				startIndex = Math.min(startIndex, newIndex);
				finishIndex = Math.max(finishIndex, newIndex);
			}
		}

		int index = indexOfDrawingState(table, drawingState, cellState);

		AxisInterval localInterval = DrawingStateInterval.getInterval(
				drawingState, axis);

		if (index == startIndex && interval.contains(drawingState)) {
			paintStart(interval, localInterval, view, key, g, bounds, state,
					context);
		} else if (index == finishIndex && interval.contains(drawingState)) {
			paintFinish(interval, localInterval, view, key, g, bounds, state,
					context);
		} else if (cellState.getRow() >= startIndex
				&& cellState.getRow() <= finishIndex) {
			paintOther(interval, view, key, g, bounds, state, context);
		}
		return bounds;
	}

	public long width(Object key, Graphics g, Rectangle bounds,
			GraphicsContext context) {
		return bounds.width;
	}

	// ________________________________________________________________________

	protected abstract void paintStart(DrawingStateInterval interval,
			AxisInterval localInterval, AxisView view, Object key, Graphics g,
			Rectangle bounds, PainterState state, GraphicsContext context);

	protected abstract void paintOther(DrawingStateInterval interval,
			AxisView view, Object key, Graphics g, Rectangle bounds,
			PainterState state, GraphicsContext context);

	protected abstract void paintFinish(DrawingStateInterval interval,
			AxisInterval localInterval, AxisView view, Object key, Graphics g,
			Rectangle bounds, PainterState state, GraphicsContext context);

	// ________________________________________________________________________

	protected int indexOfDrawingState(JXTable table, DrawingState state,
			CellState cellState) {
		for (int i = 0; i < table.getRowCount(); i++) {
			Object value = table.getValueAt(i, cellState.getColumn());
			if (value != null && value.equals(state))
				return i;
		}
		return -1;
	}
}
