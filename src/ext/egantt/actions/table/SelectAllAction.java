/*
 *
 */

package ext.egantt.actions.table;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.*;

// Referenced classes of package ext.egantt.actions.table:
//            AbstractTableAction

public class SelectAllAction extends AbstractTableAction
{

    public SelectAllAction(String name, Icon icon, JComponent component)
    {
        super(name, icon, component);
    }

    protected void actionPerformed(JTable table, Point location, ActionEvent event)
    {
        if(table != null)
            table.selectAll();
    }

    private static final long serialVersionUID = 0x4e9eb80f84784150L;
}
