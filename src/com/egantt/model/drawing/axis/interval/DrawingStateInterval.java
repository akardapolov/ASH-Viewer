package com.egantt.model.drawing.axis.interval;

import java.util.Iterator;
import java.util.List;

import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.axis.AxisInterval;

public class DrawingStateInterval implements AxisInterval {

	protected final Object axis;
	protected final List <DrawingState> drawingStates;
	
	public DrawingStateInterval(Object axis, List <DrawingState> drawingStates) {
		this.axis = axis;
		this.drawingStates = drawingStates;
	}

	//	________________________________________________________________________
	
	public DrawingState get(int index) {
		return drawingStates.get(index);
	}
	
	public boolean contains(DrawingState state) {
		return drawingStates.contains(state);
	}
	
	public int indexOf(DrawingState state) {
		return drawingStates.indexOf(state);
	}
	
	public int size() {
		return drawingStates.size();
	}
	//	________________________________________________________________________
	
	
	
	public Object getStart() {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.getStart() : null;
	}

	public Object getFinish() {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.getFinish() : null;
	}

	public Object getRange() {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.getRange() : null;
	}

	public boolean contains(AxisInterval rhs) {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.contains(rhs) : false;
	}

	public boolean containsValue(Object rhs) {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.containsValue(rhs) : false;
	}

	public boolean intersects(AxisInterval rhs) {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.intersects(rhs) : false;
	}

	public AxisInterval union(AxisInterval rhs) {
		AxisInterval lhs = getInterval();
		return lhs != null ? lhs.union(rhs) : rhs;
	}	
	
	//	________________________________________________________________________
	
	protected AxisInterval getInterval() {
		AxisInterval result = null;
		for (Iterator iter = drawingStates.iterator(); iter.hasNext();) {
			final DrawingState state = (DrawingState) iter.next();
		
			AxisInterval interval = getInterval(state, axis);
			result = result != null 
				? result.union(interval) : interval;
		}
		return result;
	}
	
	//	________________________________________________________________________
	
	public static AxisInterval getInterval(DrawingState state, Object axis) {
		AxisInterval result = null;
		for (Iterator parts = state.parts(); parts.hasNext();) {
			final DrawingPart part = (DrawingPart) parts.next();
			if (part.isSummaryPart())
				continue;
		 
			int index = part.keys().indexOf(axis);
			if (index < 0)
				continue;
		
			AxisInterval intervals[] = part.getInterval();
			if (intervals == null || intervals[index] == null)
				continue;
		
			result = intervals[index].union(result);
		}
		return result;
	}

	public DrawingTransform getTransform() {
		AxisInterval interval = getInterval();
		return interval != null ? interval.getTransform() : null;
	}
}