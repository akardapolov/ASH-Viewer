/*
 *
 */

package ext.egantt.actions.table;

import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.AbstractAction;
import javax.swing.JTable;

// Referenced classes of package ext.egantt.actions.table:
//            AbstractDrawingAction

public class ClickDrawingToolAdapter
    implements DrawingTool
{
    protected class LocalMouseListener extends MouseAdapter
    {

        public void mouseClicked(MouseEvent e)
        {
            if(!(e.getComponent() instanceof JTable))
                return;
            if(action instanceof AbstractDrawingAction)
                ((AbstractDrawingAction)action).actionPerformed((JTable)e.getComponent(), e.getPoint(), new ActionEvent(e.getSource(), 1001, actionCommand));
            else
                action.actionPerformed(new ActionEvent(e.getSource(), 1001, actionCommand));
        }

        final ClickDrawingToolAdapter this$0;

        protected LocalMouseListener()
        {
            this$0 = ClickDrawingToolAdapter.this;
        }
    }


    public ClickDrawingToolAdapter(AbstractAction action)
    {
        this(action, ext.egantt.actions.table.ClickDrawingToolAdapter.class.getName());
    }

    public ClickDrawingToolAdapter(AbstractAction action, String actionCommand)
    {
        listener = new LocalMouseListener();
        this.action = action;
        this.actionCommand = actionCommand;
    }

    public void intialize(GanttTable table)
    {
        if(this.table != null)
            terminate();
        if(action != null)
        {
            this.table = table;
            table.addMouseListener(listener);
        }
    }

    public void terminate()
    {
        if(action != null)
            table.removeMouseListener(listener);
        table = null;
    }

    public void paintComponent(Graphics g1)
    {
    }

    protected final AbstractAction action;
    protected final String actionCommand;
    protected transient GanttTable table;
    protected LocalMouseListener listener;
}
