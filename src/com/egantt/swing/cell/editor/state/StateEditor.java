package com.egantt.swing.cell.editor.state;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingState;

public interface StateEditor {
	
	//	________________________________________________________________________
	
	public void mousePressed(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context);
	
	public void mouseReleased(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context);

	public void mouseMoved(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context);

	public void mouseDragged(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context);
}