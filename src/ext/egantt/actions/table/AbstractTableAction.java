/*
 *
 */

package ext.egantt.actions.table;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import javax.swing.*;

public abstract class AbstractTableAction extends AbstractAction
{

    public AbstractTableAction(String name, Icon icon, JComponent component)
    {
        super(name, icon);
        setComponent(component);
    }

    public void setComponent(JComponent component)
    {
        this.component = component;
    }

    public final void actionPerformed(ActionEvent event)
    {
        Component component = null;
        Point location = null;
        if(this.component != null)
            component = this.component;
        else
        if(SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, (Component)event.getSource()) == null);
        JPopupMenu parent;
        for(parent = (JPopupMenu)SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, (Component)event.getSource()); parent.getInvoker() instanceof JMenu; parent = (JPopupMenu)SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, parent.getInvoker()));
        component = (JComponent)parent.getInvoker();
        Point screenPoint = getPreferredLocation(parent);
        if(screenPoint != null)
        {
            location = screenPoint;
            SwingUtilities.convertPointFromScreen(location, component);
        }
        if(component == null)
            return;
        JTable table = (JTable)SwingUtilities.getAncestorOfClass(javax.swing.JTable.class, component);
        table = table == null ? findTable(component) : table;
        if(table != null)
            actionPerformed(table, location, event);
    }

    protected abstract void actionPerformed(JTable jtable, Point point, ActionEvent actionevent);

    protected JTable findTable(Component component)
    {
        if(component instanceof JTable)
            return (JTable)component;
        if(!(component instanceof Container))
            return null;
        Container container = (Container)component;
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            JTable result = findTable(container.getComponent(i));
            if(result != null)
                return result;
        }

        return null;
    }

    private Point getPreferredLocation(JPopupMenu menu)
    {
        try
        {
            int desiredLocationX = -1;
            int desiredLocationY = -1;
            Field fields[] = javax.swing.JPopupMenu.class.getDeclaredFields();
            for(int i = 0; i < fields.length; i++)
            {
                if("desiredLocationX".equals(fields[i].getName()))
                {
                    fields[i].setAccessible(true);
                    desiredLocationX = fields[i].getInt(menu);
                }
                if("desiredLocationY".equals(fields[i].getName()))
                {
                    fields[i].setAccessible(true);
                    desiredLocationY = fields[i].getInt(menu);
                }
            }

            return desiredLocationX == -1 || desiredLocationY == -1 ? null : new Point(desiredLocationX, desiredLocationY);
        }
        catch(IllegalAccessException ex)
        {
            return null;
        }
    }

    private static final long serialVersionUID = 0x7dcabf7f3fe75a8L;
    protected JComponent component;
}
