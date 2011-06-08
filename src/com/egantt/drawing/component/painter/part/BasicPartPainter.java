/**
 * @(#)BasicPartPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.drawing.component.painter.part;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.graphics.GraphicsManager;
import com.egantt.awt.graphics.context.CompoundContext;
import com.egantt.awt.graphics.manager.BasicGraphicsManager;
import com.egantt.drawing.DrawingPainter;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.StateResources;
import com.egantt.model.drawing.painter.PainterState;
import com.egantt.swing.cell.CellState;

public class BasicPartPainter extends AbstractPartPainter
{
	protected GraphicsManager graphics = new BasicGraphicsManager();

	// _________________________________________________________________________

	public void setGraphicsManager(GraphicsManager graphics)
	{
		this.graphics = graphics;
	}

	// _________________________________________________________________________

	protected void paint(Object key, Component c, DrawingPart part, CellState cellState, 
			DrawingTransform transforms[], Graphics g, DrawingContext context)
	{
		// Graphics configuration
		Iterator contexts = context.iterator(part.getContext(key), ContextResources.GRAPHICS_CONTEXT);
		Iterator painters = context.iterator(part.getPainter(key), ContextResources.DRAWING_PAINTER);

		// Paint code
		PainterState painterState = null; 
		{
			Object stateKey = part.getState(key);
			if (stateKey == null)
			{
				if (true) System.out.println("paint - Drawing part: '" + part + "' has no state defined for: '" + stateKey + "'");
				return;
			}
			painterState = (PainterState) context.get(part.getState(key), ContextResources.PAINTER_STATE);
		}
		
		if (painterState == null) {
			if (true) System.out.println("paint - Drawing part: '" + part + "' has no state defined for part state: '" + part.getState(key) + "'");
			return;
		}
		
		painterState.initialise();

		painterState.put(StateResources.COMPONENT, c);
		painterState.put(StateResources.CELL_STATE, cellState);
		painterState.put(StateResources.DRAWING_PART, part);
		painterState.put(StateResources.TRANSFORMS, transforms);

		paintComponent(key, g, g.getClipBounds(), painterState, painters, contexts, context);

		painterState.terminate();
	}

	// __________________________________________________________________________

	protected void paintComponent(Object key, Graphics device, Rectangle deviceBounds,
			PainterState state, Iterator painters, Iterator contexts, DrawingContext context)
	{
		while (contexts.hasNext() && painters.hasNext())
		{
			final GraphicsContext graphicsContext =
				new LocalGraphicsContext((GraphicsContext) contexts.next(), context);

			DrawingPainter drawingPainter = (DrawingPainter) painters.next();
			drawingPainter.paint(key, graphics.create(key, device, graphicsContext),
				deviceBounds, state, graphicsContext);
		}
	}
	
	//	________________________________________________________________________
	
	protected final class LocalGraphicsContext implements CompoundContext {
	
		protected final GraphicsContext graphicsContext;
		protected final DrawingContext context;
		
		public LocalGraphicsContext(GraphicsContext graphicsContext, DrawingContext context) {
			this.graphicsContext = graphicsContext;
			this.context = context;
		}
		
		public Object get(Object key, Object type) {
		
			Object value = graphicsContext.get(key, type);
			if (value != null)
				return value;
			
			return (type instanceof Class) ? context.get(key, (Class) type) : null;
		}

		public Object get(Object key, Object group, Object type) {
			if (! (graphicsContext instanceof CompoundContext))
				return get(key, type);
			
			Object value = ((CompoundContext)graphicsContext).get(key, group, type);
			if (value != null)
				return value;
			
			return (type instanceof Class) ? context.get(key, (Class) type) : null;
		}
	}
}