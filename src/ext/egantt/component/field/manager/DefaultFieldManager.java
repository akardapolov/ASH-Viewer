/*
 *
 */

package ext.egantt.component.field.manager;

import com.egantt.awt.graphics.GraphicsState;
import com.egantt.drawing.component.DrawingComponent2D;
import com.egantt.drawing.component.painter.state.BasicStatePainter;
import com.egantt.model.component.ComponentManager;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.model.drawing.state.*;
import com.egantt.swing.cell.CellEditor;
import com.egantt.swing.cell.editor.adapter.JTableEditorAdapter;
import com.egantt.swing.cell.editor.state.ManagedStateEditor;
import com.egantt.swing.cell.renderer.adapter.JTableRendererAdapter;
import com.egantt.swing.cell.renderer.state.ManagedStateRenderer;
import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.repaint.manager.DrawingRepaintManager;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class DefaultFieldManager
    implements ComponentManager
{

    public DefaultFieldManager(GraphicsState state, DrawingContext context)
    {
        this.state = state;
        this.context = context;
    }

    public void registerComponent(JComponent component, ComponentContext componentContext)
    {
        stateManager.registerComponent(component, componentContext);
        if(component instanceof JTable)
            setFields((JTable)component, componentContext);
    }

    public void unregisterComponent(JComponent jcomponent)
    {
    }

    protected void setFields(JTable table, ComponentContext componentContext)
    {
        ManagedStateRenderer renderer = createStateRenderer(state, context, stateManager, componentContext);
        table.getTableHeader().setDefaultRenderer(new JTableRendererAdapter(renderer, false, true));
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.setDefaultRenderer(com.egantt.model.drawing.state.AbstractDrawingState.class, new JTableRendererAdapter(renderer, false, false));
        renderer = createStateRenderer(state, context, stateManager, componentContext);
        ManagedStateEditor editor = new ManagedStateEditor(renderer, stateManager, context);
        JTableEditorAdapter editorAdapter = new JTableEditorAdapter(editor);
        table.setDefaultEditor(com.egantt.model.drawing.state.SingletonDrawingState.class, editorAdapter);
        table.setDefaultEditor(ext.egantt.model.drawing.state.BasicDrawingState.class, editorAdapter);
        table.setDefaultEditor(com.egantt.model.drawing.state.AbstractDrawingState.class, editorAdapter);
    }

    protected CellEditor createStateEditor(GraphicsState state, DrawingContext context, DrawingRepaintManager repaintManager, ComponentContext componentContext)
    {
        return new ManagedStateEditor(createStateRenderer(state, context, repaintManager, componentContext), repaintManager, context);
    }

    private ManagedStateRenderer createStateRenderer(GraphicsState state, DrawingContext context, DrawingRepaintManager repaintManager, ComponentContext componentContext)
    {
        DrawingComponent2D component = new DrawingComponent2D();
        component.setGraphicsState(state);
        component.setPreferredSize(new Dimension(0, 30));
        ManagedStateRenderer cellRenderer = new ManagedStateRenderer(component, repaintManager, componentContext);
        cellRenderer.setComponentPainter(new BasicStatePainter(context));
        return cellRenderer;
    }

    protected final DrawingContext context;
    protected final DrawingRepaintManager stateManager = new DrawingRepaintManager();
    protected final GraphicsState state;
}
