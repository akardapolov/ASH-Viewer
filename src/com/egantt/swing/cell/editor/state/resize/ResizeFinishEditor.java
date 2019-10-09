package com.egantt.swing.cell.editor.state.resize;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.MutableInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;

public class ResizeFinishEditor extends AbstractResizeEditor {

	boolean isMiddle = false;
	Point originalPoint;
	MutableInterval interval = null;
	MutableInterval savedInterval = null;
	
	public void mousePressed(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context) {
		this.interval =  getInterval(e.getPoint(), 5, drawing, axisKey);
		this.originalPoint = e.getPoint();
		this.savedInterval = (MutableInterval) interval != null ? (MutableInterval) interval.clone() : null;
		calculateCursor(interval, bounds, 5, e, axisKey, context);
	}

	public void mouseDragged(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context) {
		if (interval == null)
			return;
		
		interval.setStart(savedInterval.getStart());
		interval.setFinish(savedInterval.getFinish());

		updateDrawing(interval, bounds, e, drawing, axisKey, context);
	}

	public void mouseReleased(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context) {
		if (interval == null)
			return;

		interval.setStart(savedInterval.getStart());
		interval.setFinish(savedInterval.getFinish());
		updateDrawing(interval, bounds, e, drawing, axisKey, context);
	}
	
	protected void updateDrawing(MutableInterval interval, Rectangle bounds, MouseEvent e, DrawingState drawing, Object axisKey, DrawingContext context) {
		AxisView view = (AxisView) context.get(axisKey,
				ContextResources.AXIS_VIEW);
		
		int size;
		int pos;
		switch (view.getOrientation()) 
		{
			case AxisView.HORIZONTAL: 
				size = ((JComponent) e.getSource()).getSize().width;
				pos = e.getPoint().x;
				break;
			case AxisView.VERTICAL: 
				size = ((JComponent) e.getSource()).getSize().height;
				pos = e.getPoint().y;
				break;
			default:
				return;
		}

		DrawingTransform transform = view.getTransform();

		if (isMiddle) {
			interval.setFinish(transform.inverseTransform(pos, size));
		}
	}

	public void mouseMoved(MouseEvent e, Rectangle bounds, DrawingState drawing, Object axisKey, DrawingContext context) {
		AxisInterval interval = (LongInterval) getInterval(e.getPoint(),3,  drawing, axisKey);
		
		calculateCursor(interval, bounds, 3, e, axisKey, context);
	}
	
	//	________________________________________________________________________
	
	protected void calculateCursor(AxisInterval interval, Rectangle bounds, int offset, MouseEvent e, Object axisKey, DrawingContext context) {
		if (interval == null) {
			Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		e.getComponent().setCursor(cursor);
			return;
		}

		AxisView view = (AxisView) context.get(axisKey,
				ContextResources.AXIS_VIEW);
	
		int size;
		int pos;
		switch (view.getOrientation()) 
		{
			case AxisView.HORIZONTAL: 
				size = bounds.width;
				pos = e.getPoint().x;
				break;
			case AxisView.VERTICAL: 
				size = bounds.height;
				pos = e.getPoint().y;
				break;
			default:
				return;
		}
		
		DrawingTransform transform = view.getTransform();
		int left = transform.transform(interval.getStart(), size);
		int right = transform.transform(interval.getFinish(), size);

		isMiddle = (pos >= left - offset && pos - offset <= right) ;

		if (isMiddle) {
			Cursor cursor = new Cursor(Cursor.MOVE_CURSOR);
			e.getComponent().setCursor(cursor);
		}
		else
		{
			Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			e.getComponent().setCursor(cursor);
		}
	}
}