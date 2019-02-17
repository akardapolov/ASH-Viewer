package com.egantt.swing.cell.editor.state.resize;

import java.awt.Point;
import java.util.Iterator;

import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.MutableInterval;
import com.egantt.swing.cell.editor.state.StateEditor;

public abstract class AbstractResizeEditor implements StateEditor{
	
	public MutableInterval getInterval(Point point, int buffer, DrawingState drawing, Object axisKey) {
		Object key = drawing.getValueAt(point, buffer, buffer);
		return (MutableInterval) getInterval(key, drawing, axisKey);
	}
	
	//	________________________________________________________________________
	
	protected AxisInterval getInterval(Object key, DrawingState drawing, Object axisKey) {
		for (Iterator iter = drawing.parts(); iter.hasNext();) {
			DrawingPart part = (DrawingPart) iter.next();
			if (part.isSummaryPart())
				continue;
			
			AxisInterval[] interval = part.getInterval(key,
					new AxisInterval[] {});
			int index = part.keys().indexOf(axisKey);
			if (index < 0)
				continue;
			
			if (interval != null && index < interval.length && interval[index] != null)
				return interval[index];
		}
		return null;
	}
}