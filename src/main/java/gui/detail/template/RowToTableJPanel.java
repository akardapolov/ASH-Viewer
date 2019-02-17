package gui.detail.template;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class RowToTableJPanel extends JPanel {

    private JTextPane textPane = null;
    private JScrollPane tableScrollPane = null;

    private JTable table = null;

    private TableSorter sorter;
    private DefaultTableModel tableModel = new DefaultTableModel();

    private Vector columns = new Vector();

    public RowToTableJPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(198, 198, 198)));

        sorter = new TableSorter(tableModel);
        table = new JXTable(sorter);
        sorter.setTableHeader(table.getTableHeader());
        table.setDefaultRenderer(Object.class, new TableCellRendererRtv());
        table.setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setCellSelectionEnabled(true);
        tableScrollPane = new JScrollPane();
        tableScrollPane.setViewportView(table);

        columns.add("Parameter");
        columns.add("Value");

        add(tableScrollPane);
    }

    public void loadNewData(Vector data)
    {
        this.clearScreen();
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(198, 198, 198)));
        tableModel.setDataVector(data, columns);
        sorter.fireTableDataChanged();
        table.validate();
        table.repaint();
        add(tableScrollPane);
        Point p = new Point(0, 0);
        JViewport port = tableScrollPane.getViewport();
        port.setViewPosition(p);
        tableScrollPane.validate();
        tableScrollPane.repaint();

        validate();
        repaint();
    }

    public void clearScreen()
    {
        removeAll();
        validate();
        repaint();
    }

}
