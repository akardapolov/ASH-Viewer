/**
 * @(#)DrawingStateHelper.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.drawing.state;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;

public class DrawingStateHelper
{
	public static final DrawingStateHelper instance = new DrawingStateHelper();

	// _________________________________________________________________________

	public BasicDrawingState createDrawingState(Object key, String partPainter,
		Object axises [], AxisInterval intervals[], String plotter, String state, String context)
	{
		// add our details into the model
		BasicDrawingState drawingState = new BasicDrawingState();

		// register the part with the state
		ListDrawingPart drawingPart = new ListDrawingPart(axises, partPainter);
		drawingState.addDrawingPart(drawingPart);

		// populate the drawing part
		drawingPart.add(key, intervals, plotter, state, context);
		return drawingState;
	}
	
	//	________________________________________________________________________
	
	
}
