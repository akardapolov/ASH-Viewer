/**
 * @(#)DefaultFieldManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.component.field.manager;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.component.DrawingComponent2D;
import com.egantt.drawing.component.painter.state.BasicStatePainter;
import com.egantt.model.component.ComponentManager;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.state.AbstractDrawingState;
import com.egantt.model.drawing.state.BasicDrawingState;
import com.egantt.model.drawing.state.SingletonDrawingState;
import com.egantt.swing.cell.CellEditor;
import com.egantt.swing.cell.editor.adapter.JTableEditorAdapter;
import com.egantt.swing.cell.editor.state.ManagedStateEditor;
import com.egantt.swing.cell.renderer.adapter.JTableRendererAdapter;
import com.egantt.swing.cell.renderer.state.ManagedStateRenderer;
import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.repaint.manager.DrawingRepaintManager;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;

/**
 *  An intermediate solution until a proper FieldManager can be written
 *  and dealt with.
 */
public class DefaultFieldManager implements ComponentManager
{
	protected final DrawingContext context;
	protected final DrawingRepaintManager stateManager = new DrawingRepaintManager();
	protected final GraphicsState state;

	public DefaultFieldManager(GraphicsState state, DrawingContext context)
	{
		this.state = state;
		this.context = context;
	}

	// __________________________________________________________________________

	public void registerComponent(JComponent component, ComponentContext componentContext)
	{
		stateManager.registerComponent(component, componentContext);

		if (component instanceof JXTable)
			setFields((JXTable) component, componentContext);
	}

	public void unregisterComponent(JComponent component)
	{
	}

	// __________________________________________________________________________

	protected void setFields(JXTable table, ComponentContext componentContext)
	{
		
		{	
			ManagedStateRenderer renderer = createStateRenderer(state, context, stateManager, componentContext);
			table.getTableHeader().setDefaultRenderer(new JTableRendererAdapter(renderer, false, true));
			table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 30));
			table.setDefaultRenderer(AbstractDrawingState.class, new JTableRendererAdapter(renderer, false, false));
		}
		
		{	
			ManagedStateRenderer renderer = createStateRenderer(state, context, stateManager, componentContext);
			ManagedStateEditor editor = new ManagedStateEditor(renderer, stateManager, context);
			
			JTableEditorAdapter editorAdapter = new JTableEditorAdapter(editor);
			table.setDefaultEditor(SingletonDrawingState.class, editorAdapter);
			table.setDefaultEditor(BasicDrawingState.class, editorAdapter);
			table.setDefaultEditor(AbstractDrawingState.class, editorAdapter);
		}
	}

	// __________________________________________________________________________

	protected CellEditor createStateEditor(GraphicsState state, DrawingContext context,
		DrawingRepaintManager repaintManager, ComponentContext componentContext)
	{
		return new ManagedStateEditor(createStateRenderer(state, context, repaintManager, componentContext), repaintManager, context);
	}
	
//	protected static final DrawingComponent2D drawing = new DrawingComponent2D();
	
	private ManagedStateRenderer createStateRenderer(GraphicsState state, DrawingContext context,
		DrawingRepaintManager repaintManager, ComponentContext componentContext)
	{
		// create the drawingComponent with the appropriate context + graphicsDevice
		DrawingComponent2D component = new DrawingComponent2D();
		component.setGraphicsState(state);

		// component height is used by the header-renderer width is of no importance
		component.setPreferredSize(new java.awt.Dimension(0, 30)); // 18

		// a managed state cell renderer, will update the StateManager with the last
		// known state to listenTo: note this is not ideal but the lack of a complete
		// model of all states makes this neccesary for now.
		ManagedStateRenderer cellRenderer = new ManagedStateRenderer(
			component, repaintManager, componentContext);
		cellRenderer.setComponentPainter(new BasicStatePainter(context));
		return  cellRenderer;
	}
}