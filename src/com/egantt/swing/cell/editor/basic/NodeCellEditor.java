/*
 * @(#)NodeCellEditor.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.editor.basic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.egantt.swing.cell.CellState;
import com.egantt.swing.cell.editor.AbstractCellEditor;
import com.egantt.swing.cell.renderer.basic.NodeCellRenderer;
import com.egantt.swing.component.repaint.RepaintManager;
import com.egantt.swing.table.model.row.RowExpander;

public class NodeCellEditor extends AbstractCellEditor
{
	protected final NodeCellRenderer renderer = new NodeCellRenderer();
	protected final RepaintManager manager;
	protected final RowExpander expander;

	protected transient LocalMouseListener listener;

	protected transient CellState state;

	public NodeCellEditor(RowExpander expander, RepaintManager manager)
	{
		this.expander = expander;
		this.manager = manager;
	}

	public Object getValue() {
		return null;
	}
	
	// __________________________________________________________________________

	public JComponent getComponent(CellState state,JComponent parent)
	{
		this.state = state;

		if (listener == null)
		{
			listener = new LocalMouseListener();
			renderer.addMouseListener(listener);
		}

		return renderer.getComponent(state, parent);
	}

	/// _________________________________________________________________________
	
	public void cancelCellEditing()
	{
		if (listener != null)
		{
			renderer.removeMouseListener(listener);
			this.listener = null;
		}
	}

	// __________________________________________________________________________

	protected class LocalMouseListener implements MouseListener
	{
		// _______________________________________________________________________

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}

		// _______________________________________________________________________

		public void mouseClicked(MouseEvent e)
		{
			if (!(e.getClickCount() == 2))
				return;

			expander.toggle(state.getRow(), state.getColumn(), state.getValue());
			manager.repaint();
		}

		public void mouseEntered(MouseEvent e)
		{
		}

		public void mouseExited(MouseEvent e)
		{
		}
	}
}