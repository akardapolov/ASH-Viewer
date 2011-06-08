package com.egantt.swing.cell.editor.state;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;

import javax.swing.JComponent;

import com.egantt.model.drawing.ContextConstants;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingState;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.cell.editor.AbstractCellEditor;
import com.egantt.swing.cell.renderer.state.ManagedStateRenderer;
import com.egantt.swing.component.repaint.RepaintManager;

public class ManagedStateEditor extends AbstractCellEditor {
	protected final ManagedStateRenderer renderer;

	protected final DrawingContext context;
	protected final RepaintManager manager;
	
	protected transient MouseListener mouseListener;
	protected transient MouseMotionListener motionListener;
	
	protected transient StateEditor stateEditor;
	
	protected DrawingState drawing;
	protected JComponent parent;
	protected CellState cellState;
	protected Rectangle bounds;
	
	public ManagedStateEditor(ManagedStateRenderer renderer,
			RepaintManager manager, DrawingContext context) {
		this.context = context;
		this.manager = manager;
		this.renderer = renderer;
	}

	//	________________________________________________________________________
	
	public Object getValue() {
		return drawing;
	}
	

	public boolean isCellEditable(EventObject eo) {
		return getAxisKey(context) != null;
	}
	
	//	________________________________________________________________________

	public JComponent getComponent(CellState cellState, JComponent parent) {
		this.cellState = cellState;
		this.drawing = (DrawingState) cellState.getValue();
		this.parent = parent;
		
		if (mouseListener == null) {
			mouseListener = new LocalMouseListener();
			motionListener = new LocalMotionListener();

			parent.addMouseListener(mouseListener);
			parent.addMouseMotionListener(motionListener);
		}
		return renderer.getComponent(cellState, parent);
	}
	
	public void cancelEditing() {
		if (parent != null)
		{
			parent.removeMouseListener(mouseListener);
			parent.removeMouseMotionListener(motionListener);
			mouseListener = null;
			motionListener = null;
		}
		this.parent = null;
	}

	public void stopEditing() {
		if (parent != null)
		{
			parent.removeMouseListener(mouseListener);
			parent.removeMouseMotionListener(motionListener);
			mouseListener = null;
			motionListener = null;
		}
		this.parent = null;
	}

	//	________________________________________________________________________

	protected class LocalMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			Object axisKey = context.get(ContextConstants.EDITING_AXIS, ContextResources.OTHER_PROPERTY); 
			if (stateEditor != null)
				stateEditor.mouseDragged(e, cellState.getBounds(), drawing, axisKey, context);
			
			manager.repaint();
		}

		/**
		 * Invoked when the mouse button has been moved on a component (with no
		 * buttons down).
		 */
		public void mouseMoved(MouseEvent e) { 
			
			Object axisKey = context.get(ContextConstants.EDITING_AXIS, ContextResources.OTHER_PROPERTY);
			if (stateEditor != null)
				stateEditor.mouseMoved(e, cellState.getBounds(), drawing, axisKey, context);
		}
	}
	
	//	________________________________________________________________________
	
	protected StateEditor getAxisKey(DrawingContext context) {
		Object modeKey = context.get(ContextConstants.EDITING_MODE, ContextResources.OTHER_PROPERTY);
		return modeKey != null ? (StateEditor) context.get(modeKey, ContextResources.STATE_EDITOR) : null;
	}
	
	//	________________________________________________________________________

	protected class LocalMouseListener extends MouseAdapter {

		/**
		 * Invoked when the mouse button has been clicked (pressed and released)
		 * on a component.
		 */
		public void mousePressed(MouseEvent e) {
			stateEditor = getAxisKey(context);
			
			Object axisKey = context.get(ContextConstants.EDITING_AXIS, ContextResources.OTHER_PROPERTY);
			if (stateEditor != null)
				stateEditor.mousePressed(e, cellState.getBounds(), drawing, axisKey, context);
			else 
				stopEditing();
		}
		
		public void mouseReleased(MouseEvent e) {
			Object axisKey = context.get(ContextConstants.EDITING_AXIS, ContextResources.OTHER_PROPERTY);
			if (stateEditor != null)
				stateEditor.mouseReleased(e, cellState.getBounds(), drawing, axisKey, context);
			manager.repaint();
		}
	}
}