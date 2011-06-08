/*
 *
 */

package ext.egantt.actions.table;

import com.egantt.model.drawing.*;
import com.egantt.swing.cell.editor.state.StateEditor;
import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StateEditorTool
    implements DrawingTool
{
    protected class LocalMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(e.isPopupTrigger())
                return;
            stateEditor = getAxisKey(table.getDrawingContext());
            if(stateEditor == null)
                return;
            Rectangle cellRect = getCellRect((JTable)e.getComponent(), e.getPoint());
            if(cellRect == null)
                return;
            DrawingState state = getDrawingState((JTable)e.getComponent(), e.getPoint());
            DrawingContext context = table.getDrawingContext();
            MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
            if(state != null && context != null && evt != null)
            {
                stateEditor.mousePressed(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
                e.getComponent().repaint();
                glassPane.repaint();
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            if(e.isPopupTrigger())
                return;
            if(stateEditor == null)
                return;
            Rectangle cellRect = getCellRect((JTable)e.getComponent(), e.getPoint());
            if(cellRect == null)
                return;
            DrawingState state = getDrawingState((JTable)e.getComponent(), e.getPoint());
            DrawingContext context = table.getDrawingContext();
            MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
            if(state != null && context != null && evt != null)
            {
                stateEditor.mouseReleased(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
                e.getComponent().invalidate();
                glassPane.repaint();
            }
        }

        final StateEditorTool this$0;

        protected LocalMouseListener()
        {
            this$0 = StateEditorTool.this;            
        }
    }

    protected class LocalGlassPane extends JPanel
    {

        public void paintComponent(Graphics g1)
        {
        }

        private static final long serialVersionUID = 1L;
        final StateEditorTool this$0;

        LocalGlassPane()
        {
            this$0 = StateEditorTool.this;
            setOpaque(false);
        }
    }

    protected class LocalMotionListener
        implements MouseMotionListener
    {

        public void mouseDragged(MouseEvent e)
        {
            if(e.isPopupTrigger())
                return;
            if(stateEditor == null)
                return;
            Rectangle cellRect = getCellRect((JTable)e.getComponent(), e.getPoint());
            if(cellRect == null)
                return;
            DrawingState state = getDrawingState((JTable)e.getComponent(), e.getPoint());
            DrawingContext context = table.getDrawingContext();
            MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
            if(state != null && context != null && evt != null)
            {
                stateEditor.mouseDragged(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
                e.getComponent().repaint();
                glassPane.repaint();
            }
        }

        public void mouseMoved(MouseEvent e)
        {
            if(e.isPopupTrigger())
                return;
            if(stateEditor == null)
                return;
            Rectangle cellRect = getCellRect((JTable)e.getComponent(), e.getPoint());
            if(cellRect == null)
                return;
            DrawingState state = getDrawingState((JTable)e.getComponent(), e.getPoint());
            DrawingContext context = table.getDrawingContext();
            MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
            if(state != null && context != null && evt != null)
            {
                stateEditor.mouseMoved(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
                e.getComponent().repaint();
                glassPane.repaint();
            }
        }

        final StateEditorTool this$0;

        protected LocalMotionListener()
        {
            this$0 = StateEditorTool.this;
        }
    }


    public StateEditorTool(Object axisKey, Object modeKey)
    {
        this.axisKey = axisKey;
        this.modeKey = modeKey;
    }

    public void intialize(GanttTable table)
    {
        this.table = table;
        if(mouseListener == null)
        {
            mouseListener = new LocalMouseListener();
            motionListener = new LocalMotionListener();
            glassPane = new LocalGlassPane();
            table.addMouseListener(mouseListener);
            table.addMouseMotionListener(motionListener);
            //table.add(glassPane, GanttTable.CENTER_ALIGNMENT); //PALETTE_LAYER);
           // SwingUtilities.updateComponentTreeUI(table);
        }
    }

    public void terminate()
    {
        if(table != null)
        {
            table.removeMouseListener(mouseListener);
            table.removeMouseMotionListener(motionListener);
            //table.remove(glassPane);
            mouseListener = null;
            motionListener = null;
            glassPane = null;
        }
        table = null;
    }

    public void paintComponent(Graphics g1)
    {
    }

    protected DrawingState getDrawingState(JTable table, Point location)
    {
        int row = table.rowAtPoint(location);
        int column = table.columnAtPoint(location);
        if(row < 0 || column < 0)
        {
            return null;
        } else
        {
            Object value = table.getValueAt(row, column);
            return (value instanceof DrawingState) ? (DrawingState)value : null;
        }
    }

    protected Rectangle getCellRect(JTable table, Point location)
    {
        int row = table.rowAtPoint(location);
        int column = table.columnAtPoint(location);
        if(row < 0 || column < 0)
            return null;
        else
            return table.getCellRect(row, column, true);
    }

    protected Rectangle getTranslatedCellRect(Rectangle cellRect)
    {
        return new Rectangle(0, 0, cellRect.width, cellRect.height);
    }

    protected MouseEvent getTranslatedMouseEvent(MouseEvent evt, Rectangle cellRect)
    {
        Point location = new Point(evt.getPoint());
        location.translate(-cellRect.x, -cellRect.y);
        Component component = ((Component) (SwingUtilities.getAncestorOfClass(ext.egantt.swing.GanttTable.class, evt.getComponent()) == null ? evt.getComponent() : ((Component) (SwingUtilities.getAncestorOfClass(ext.egantt.swing.GanttTable.class, evt.getComponent())))));
        return location == null ? null : new MouseEvent(component, evt.getID(), evt.getWhen(), evt.getModifiers(), location.x, location.y, evt.getClickCount(), evt.isPopupTrigger(), evt.getButton());
    }

    protected StateEditor getAxisKey(DrawingContext context)
    {
        return modeKey == null ? null : (StateEditor)context.get(modeKey, ContextResources.STATE_EDITOR);
    }

    protected final Object modeKey;
    protected final Object axisKey;
    protected transient MouseListener mouseListener;
    protected transient MouseMotionListener motionListener;
    protected transient LocalGlassPane glassPane;
    protected transient StateEditor stateEditor;
    protected GanttTable table;
	
}
