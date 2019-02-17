package ext.egantt.actions.table;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import org.jdesktop.swingx.JXTable;

import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;

public class ClickDrawingToolAdapter implements DrawingTool {

	protected final AbstractAction action;
	protected final String actionCommand;
	protected transient GanttTable table;
	protected LocalMouseListener listener  = new LocalMouseListener();
	
	public ClickDrawingToolAdapter(AbstractAction action) {
		this (action, ClickDrawingToolAdapter.class.getName());
	}
	public ClickDrawingToolAdapter(AbstractAction action, String actionCommand) {
		this.action = action;
		this.actionCommand = actionCommand;
	}
	
	//	________________________________________________________________________
	
	public void intialize(GanttTable table) {
		
		if (this.table != null)
			terminate();
		
		if (this.action != null) {
			this.table = table;
			table.addMouseListener(listener);
		}
	}

	public void terminate() {			
		if (this.action != null) {
			table.removeMouseListener(listener);
		}
		
		this.table = null;
	}
	
	//	________________________________________________________________________
	
	public void paintComponent(Graphics g) {
		
	}
	
	//	________________________________________________________________________
	
	protected class LocalMouseListener extends MouseAdapter {
	    public void mouseClicked(MouseEvent e) 
	    {
	    	if (!(e.getComponent() instanceof JXTable))
	    		return;
	    	
	    	if (action instanceof AbstractDrawingAction)
	    	{
	    		((AbstractDrawingAction)action).actionPerformed((JXTable) e.getComponent(), e.getPoint(),
	    			new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, actionCommand));
	    	}
	    	else
	    	{
	    		action.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, actionCommand));
	    	}
	    }
	}
}