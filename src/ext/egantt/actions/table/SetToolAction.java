/*
 *
 */

package ext.egantt.actions.table;

import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

public class SetToolAction extends AbstractAction
{

    public SetToolAction(String name, Icon icon, DrawingTool tool, GanttTable table)
    {
        super(name, icon);
        this.tool = tool;
        this.table = table;
    }

    public void actionPerformed(ActionEvent event)
    {
        if(table != null)
            table.setDrawingTool(tool);
    }

    private static final long serialVersionUID = 0x4e9eb80f84784150L;
    protected final DrawingTool tool;
    protected final GanttTable table;
}
