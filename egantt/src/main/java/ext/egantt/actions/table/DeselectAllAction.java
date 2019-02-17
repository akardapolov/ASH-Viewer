/**
 * @(#)DeselectAllAction.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.actions.table;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXTable;

/**
  *  <code>DeselectAllAction</code> Deselects all the rows in a table
  */
public class DeselectAllAction extends AbstractTableAction
{
	private static final long serialVersionUID = 5665167758065156432L;

	protected JComponent component;
	
	public DeselectAllAction(String name, Icon icon, JComponent component)
	{
		super(name, icon, component);
	}

	// _________________________________________________________________________

	protected void actionPerformed(JXTable table, Point location, ActionEvent event)
	{	
		if (table != null)
		{
			table.clearSelection();
		}
	}
}