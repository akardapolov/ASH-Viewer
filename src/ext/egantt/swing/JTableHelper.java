/*
 *
 */

package ext.egantt.swing;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class JTableHelper
{
    protected static class LocalTableModel extends DefaultTableModel
    {

        public Class getColumnClass(int columnIndex)
        {
            for(int i = 0; i < getRowCount(); i++)
            {
                Object value = getValueAt(i, columnIndex);
                if(value != null)
                    return value.getClass();
            }

            return super.getColumnClass(columnIndex);
        }

        private static final long serialVersionUID = 0xa65466e944d21e37L;

        protected LocalTableModel()
        {
        }
    }


    public JTableHelper()
    {
    }

    public static TableModel createTableModel(Object rowData[][], Object columnNames[][])
    {
        DefaultTableModel model = new LocalTableModel();
        for(int a = 0; a < columnNames.length; a++)
        {
            for(int b = 0; b < columnNames[a].length; b++)
                model.addColumn(columnNames[a][b]);
        }

        for(int a = 0; a < rowData.length; a++){
            model.addRow(rowData[a]);
        }

        return model;
    }

    public static Object[][] createColumnNames(TableModel model)
    {
        Object rowValues[][] = new Object[2][0];
        rowValues[0] = new Object[model.getColumnCount() != 0 ? model.getColumnCount() - 1 : 0];
        rowValues[1] = new Object[model.getColumnCount() != 0 ? 1 : 0];
        for(int a = 0; a < model.getColumnCount() - 1; a++)
            rowValues[0][a] = model.getColumnName(a);

        if(rowValues[1].length != 0)
            rowValues[1][0] = model.getColumnName(model.getColumnCount() - 1);
        return rowValues;
    }
}
