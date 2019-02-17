package ext.egantt.actions.table;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.jdesktop.swingx.JXTable;

import com.egantt.model.drawing.DrawingState;

import ext.egantt.swing.GanttEntryHelper;

public class LinkEntryAction extends AbstractDrawingAction {
	private static final long serialVersionUID = 1L;

	protected String colorCode;
	protected transient DrawingState last;
	protected GanttEntryHelper helper;

	public LinkEntryAction(String name, String colorCode, GanttEntryHelper helper) {
		super(name, null, null);
		this.colorCode = colorCode;
		this.helper = helper;
	}

	@Override
	protected void actionPerformed(JXTable table, DrawingState drawing,
			Point location, ActionEvent event) {

		DrawingState current = drawing.getValueAt(location, 2, 2) != null ? drawing : null;
		
		if (current != null && last != null && current != last) {
			
			helper.createLinkEntry(current, last);
			table.repaint();
			last = null;
			return;
		}
		last = current;
	}
}