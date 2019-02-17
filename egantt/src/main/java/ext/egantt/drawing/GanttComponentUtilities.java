/**
 * @(#)GanttComponentUtilities.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.drawing;

import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.GraphicsState;
import com.egantt.awt.graphics.manager.BasicGraphicsManager;
import com.egantt.awt.graphics.state.GraphicsState2D;
import com.egantt.drawing.view.manager.LongViewManager;
import com.egantt.model.component.ComponentManager;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.axis.LongAxis;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.context.BasicDrawingContext;
import com.egantt.model.scrolling.range.view.BasicViewRange;
import com.egantt.swing.scroll.ScrollManager;
import com.egantt.swing.scroll.manager.BasicScrollManager;

import ext.egantt.component.field.manager.DefaultFieldManager;
import ext.egantt.drawing.module.BasicColorModule;
import ext.egantt.drawing.module.BasicPainterModule;
import ext.egantt.drawing.module.BoundedPainterModule;
import ext.egantt.drawing.module.CalendarDrawingModule;
import ext.egantt.drawing.module.EditorDrawingModule;
import ext.egantt.drawing.module.FilledPainterModule;
import ext.egantt.drawing.module.GanttDrawingModule;
import ext.egantt.drawing.module.GradientColorModule;
import ext.egantt.drawing.module.StandardDrawingModule;
import ext.egantt.model.drawing.ComponentUtilities;

import java.util.List;

public class GanttComponentUtilities implements ComponentUtilities
{
	protected GraphicsManager graphics;
	protected GraphicsState state;

	protected DrawingContext context;
	protected ScrollManager managers [] = new ScrollManager[2];

	private List eventList = null;
	
	String axises[];

	public GanttComponentUtilities(String axises[]) {
		this.graphics = new BasicGraphicsManager();
		this.state = new GraphicsState2D();
		this.axises = axises;
	}


	public GanttComponentUtilities(String axises[], List eventList)
	{
		managers = new ScrollManager[2];
		graphics = new BasicGraphicsManager();
		state = new GraphicsState2D();
		this.axises = axises;
		this.eventList = eventList;
	}
	// __________________________________________________________________________

	public void setDrawingGraphics(GraphicsManager graphics)
	{
		this.graphics = graphics;
	}

	public void setGraphicsState(GraphicsState state)
	{
		this.state = state;
	}

	// __________________________________________________________________________

	public DrawingContext getContext()
	{
		return context;
	}

	// __________________________________________________________________________

	public ComponentManager getManager()
	{
		this.context = createContexts();

		AxisInterval intervals [] = createIntervals(axises);
		for (int i=0; i < axises.length; i++)
		{
			LongAxis axis = new LongAxis();
			axis.setInterval(intervals[i]);

			initialiseContext(axises[i], axis.getView(i % 2), context);
		}
		return new DefaultFieldManager(state, context);
	}

	public ScrollManager getScrollManager(int pos)
	{
		return managers[pos];
	}

	// __________________________________________________________________________

	protected AxisInterval [] createIntervals(String axises [])
	{
		AxisInterval intervals [] = new AxisInterval[axises.length];
		for (int i=0; i < axises.length; i++)
			intervals [i] = new LongInterval(0, 100);		
		return intervals;
	}

	protected DrawingContext createContexts()
	{
		BasicDrawingContext context = new BasicDrawingContext();
		new BasicColorModule().initialise(context);
		new GradientColorModule().initialise(context, eventList);
		new BasicPainterModule().initialise(context);
		new BoundedPainterModule().initialise(context);
		new CalendarDrawingModule().initialise(context);
		new FilledPainterModule().initialise(context);
		new GanttDrawingModule().initialise(context);
		new StandardDrawingModule().initialise(context);
		new EditorDrawingModule().initialise(context);
		return context;
	}

	// _________________________________________________________________________

	protected void initialiseContext(String key, AxisView view, DrawingContext context)
	{
		LongViewManager manager = new LongViewManager();
		manager.setView(view);

		BasicViewRange viewRange = new BasicViewRange();
		viewRange.setManager(manager);

		BasicScrollManager scrollModel = new BasicScrollManager();
		scrollModel.setRangeModel(viewRange);

		context.put(key, ContextResources.AXIS_VIEW, view);
		context.put(key, ContextResources.SCROLL_MANAGER, scrollModel);

		if (managers[0] == null)
				managers[0] = scrollModel;
		context.put(key, ContextResources.VIEW_MANAGER, manager);
	}
}