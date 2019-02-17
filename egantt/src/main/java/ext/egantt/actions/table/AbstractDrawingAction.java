package ext.egantt.actions.table;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXTable;

import com.egantt.model.drawing.DrawingState;

public abstract class AbstractDrawingAction extends AbstractTableAction{

	private static final long serialVersionUID = 1L;

	public AbstractDrawingAction(String name, Icon icon, JComponent component) {
		super(name, icon, component);
	}
	
	//	________________________________________________________________________
	
	@Override
	protected final void actionPerformed(JXTable table, Point location, ActionEvent event) {
		if (location == null)
			return;

		int row = table.rowAtPoint(location);
		int column = table.columnAtPoint(location);
		
		if (row < 0 || column < 0)
			return;
		
		Rectangle cellRect = table.getCellRect(row, column, true);
		Point cellLocation = new Point(location);
		cellLocation.translate(-cellRect.x, -cellRect.y);
		
		Object value = table.getValueAt(row, column);
		if (value instanceof DrawingState) {
			actionPerformed(table, (DrawingState) value, cellLocation, event);
		}	
	}
	
	//	________________________________________________________________________
	
	protected abstract void actionPerformed(JXTable table, DrawingState drawing, Point location, ActionEvent event);
}