/**
 * @(#)SetToolAction.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.actions.table;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;

/**
  *  <code>SetToolAction</code> Sets the specified tool to the GanttTable component
  */
public class SetToolAction extends AbstractAction
{
	private static final long serialVersionUID = 5665167758065156432L;
	
	protected final DrawingTool tool;
	protected final GanttTable table;

	public SetToolAction(String name, Icon icon, DrawingTool tool, GanttTable table)
	{
		super(name, icon);
		
		this.tool = tool;
		this.table = table;
	}
	
	// _________________________________________________________________________

	public void actionPerformed(ActionEvent event)
	{	
		if (table != null)
		{
			table.setDrawingTool(tool);
		}
	}
}