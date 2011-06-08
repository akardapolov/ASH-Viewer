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

public class MoveResizeEditor extends AbstractResizeEditor {

	boolean isFinish = false;
	boolean isStart = false;
	boolean isMiddle = false;
	Point originalPoint;
	MutableInterval interval = null;
	private MutableInterval savedInterval;
	
	//	________________________________________________________________________
	
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
		int originalPos;
		switch (view.getOrientation()) 
		{
			case AxisView.HORIZONTAL: 
				size = ((JComponent) e.getSource()).getSize().width;
				pos = e.getPoint().x;
				originalPos = originalPoint.x; 
				break;
			case AxisView.VERTICAL: 
				size = ((JComponent) e.getSource()).getSize().height;
				pos = e.getPoint().y;
				originalPos = originalPoint.y;
				break;
			default:
				return;
		}

		DrawingTransform transform = view.getTransform();

		int startX = transform.transform(interval.getStart(), size);
		int finishX = transform.transform(interval.getFinish(), size);
		
		if (isStart)
			interval.setStart(transform.inverseTransform(startX - ((originalPos - pos) *2), size));
		else if (isFinish)
			interval.setFinish(transform.inverseTransform(finishX - ((originalPos - pos)*2), size));
		else if (isMiddle) {
			Object start = transform.inverseTransform(startX
					- (originalPos - pos), size);
			if (!view.getAxis().getInterval().containsValue(start))
				return;

			Object finish = transform.inverseTransform(finishX
					- (originalPos - pos), size);
			if (!view.getAxis().getInterval().containsValue(finish))
				return;

			interval.setStart(start);
			interval.setFinish(finish);
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
		
		
		isStart = (pos + offset >= left && pos - offset <= left);
		isFinish = (pos + offset >= right && pos - offset <= right);
		isMiddle = (pos >= left && pos <= right) && (!isStart) && (!isFinish);

		if (isStart) {
			
			Cursor cursor;
			if (view.getOrientation() == AxisView.VERTICAL)
				cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
			else 
				cursor = new Cursor(Cursor.W_RESIZE_CURSOR);

			e.getComponent().setCursor(cursor);
		}
		else if (isFinish) 
		{
			Cursor cursor;
			if (view.getOrientation() == AxisView.VERTICAL)
				cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
			else 
				cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
		
			e.getComponent().setCursor(cursor);
		}
		else if (isMiddle) {
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