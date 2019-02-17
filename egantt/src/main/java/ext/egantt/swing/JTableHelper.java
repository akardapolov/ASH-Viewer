package ext.egantt.swing;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class JTableHelper {
	
	
	/**
	 * Immitate the same behaviour as JXTable it is ugly but people seem to like it!:(
	 */
	public static TableModel createTableModel(final Object[][] rowData, final Object[][] columnNames) {
		
		final DefaultTableModel model = new LocalTableModel();
		
		for (int a=0; a < columnNames.length; a++) {
			for (int b=0; b < columnNames[a].length; b++) {
				model.addColumn(columnNames[a][b]);
			}
		}
		
		for (int a=0; a < rowData.length; a++) {
			model.addRow(rowData[a]);
		}
		return model;
	}

	/**
	 *  Generate the column names from the table model assuming the split between 
	 *  the column names is the last column is second table all other column in 
	 *  the first table. 
	 *  
	 *  I know that this is not very re-usable but makes a very good default for users
	 *  looking to get started.
	 */
	public static Object[][] createColumnNames(TableModel model) {
		Object[][] rowValues = new Object[2][0];
		rowValues[0] = new Object[model.getColumnCount() == 0 ? 0 : model.getColumnCount() -1];
		rowValues[1] = new Object[model.getColumnCount() == 0 ? 0 : 1];
		
		for (int a = 0; a < model.getColumnCount() -1; a++ )
			rowValues[0][a] = (Object) model.getColumnName(a);
		
		if (rowValues[1].length != 0)
			rowValues[1][0] = model.getColumnName(model.getColumnCount() -1);
		
		return rowValues;
	}
	
	protected static class LocalTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -6461426413301785033L;

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			for (int i=0; i < this.getRowCount(); i++) {
				Object value = this.getValueAt(i, columnIndex);
				if (value != null)
					return value.getClass();
			}
			return super.getColumnClass(columnIndex);
		}
	}
}