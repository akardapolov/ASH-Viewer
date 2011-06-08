/*
 *
 */

package ext.egantt.actions.table;

import com.egantt.model.drawing.DrawingState;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.*;

// Referenced classes of package ext.egantt.actions.table:
//            AbstractTableAction

public abstract class AbstractDrawingAction extends AbstractTableAction
{

    public AbstractDrawingAction(String name, Icon icon, JComponent component)
    {
        super(name, icon, component);
    }

    protected final void actionPerformed(JTable table, Point location, ActionEvent event)
    {
        if(location == null)
            return;
        int row = table.rowAtPoint(location);
        int column = table.columnAtPoint(location);
        if(row < 0 || column < 0)
            return;
        Rectangle cellRect = table.getCellRect(row, column, true);
        Point cellLocation = new Point(location);
        cellLocation.translate(-cellRect.x, -cellRect.y);
        Object value = table.getValueAt(row, column);
        if(value instanceof DrawingState)
            actionPerformed(table, (DrawingState)value, cellLocation, event);
    }

    protected abstract void actionPerformed(JTable jtable, DrawingState drawingstate, Point point, ActionEvent actionevent);

    private static final long serialVersionUID = 1L;
}
