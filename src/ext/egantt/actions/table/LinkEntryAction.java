/*
 *
 */

package ext.egantt.actions.table;

import com.egantt.model.drawing.DrawingState;
import ext.egantt.swing.GanttEntryHelper;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

// Referenced classes of package ext.egantt.actions.table:
//            AbstractDrawingAction

public class LinkEntryAction extends AbstractDrawingAction
{

    public LinkEntryAction(String name, String colorCode, GanttEntryHelper helper)
    {
        super(name, null, null);
        this.colorCode = colorCode;
        this.helper = helper;
    }

    protected void actionPerformed(JTable table, DrawingState drawing, Point location, ActionEvent event)
    {
        DrawingState current = drawing.getValueAt(location, 2, 2) == null ? null : drawing;
        if(current != null && last != null && current != last)
        {
            helper.createLinkEntry(current, last);
            table.repaint();
            last = null;
            return;
        } else
        {
            last = current;
            return;
        }
    }

    private static final long serialVersionUID = 1L;
    protected String colorCode;
    protected transient DrawingState last;
    protected GanttEntryHelper helper;
}
