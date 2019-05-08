package gui.detail.template;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRendererRtv extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable ptable, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(ptable, value != null ? value : "null", isSelected, hasFocus, row, column);

        if(value == null || value.toString().equalsIgnoreCase("null"))
        {
            c.setForeground(Color.LIGHT_GRAY);
            c.setFont(new Font(c.getFont().getName(), 2, c.getFont().getSize()));
        } else
        if(value instanceof ErrorData)
        {
            c.setForeground(Color.RED);
            c.setFont(new Font(c.getFont().getName(), 3, c.getFont().getSize()));
        } else
        {
            c.setFont(new Font(c.getFont().getName(), 0, c.getFont().getSize()));
        }
        return c;
    }
}
