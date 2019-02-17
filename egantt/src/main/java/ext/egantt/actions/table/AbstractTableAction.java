/**
 * @(#) AbstractTableAction.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.actions.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.jdesktop.swingx.JXTable;
import javax.swing.SwingUtilities;

public abstract class AbstractTableAction extends AbstractAction
{
	private static final long serialVersionUID = 566516734580651432L;

	protected JComponent component;
	
	public AbstractTableAction(String name, Icon icon, JComponent component)
	{
		super(name, icon);
		
		setComponent(component);
	}
	
	// _________________________________________________________________________
	
	public void setComponent(JComponent component)
	{
		this.component = component;
	}
	
	// _________________________________________________________________________

	public final void actionPerformed(ActionEvent event)
	{
		Component component = null; 
		Point location = null;
		
		if (this.component != null) {
			component = this.component;
		}
		else if (SwingUtilities.getAncestorOfClass(JPopupMenu.class, 
				(Component) event.getSource()) != null);
		{
			JPopupMenu parent = (JPopupMenu) SwingUtilities.getAncestorOfClass(JPopupMenu.class, 
					(Component) event.getSource());
			while (parent.getInvoker() instanceof JMenu)
			{
				parent = (JPopupMenu) SwingUtilities.getAncestorOfClass(JPopupMenu.class, 
						parent.getInvoker());
			}
			component = (JComponent) parent.getInvoker();		
			Point screenPoint = getPreferredLocation(parent);
			if (screenPoint != null)
			{
				location = screenPoint;
				SwingUtilities.convertPointFromScreen(location, component);
			}
		}
		
		if (component == null)
			return;
		
		JXTable table = (JXTable) SwingUtilities.getAncestorOfClass(JXTable.class, component);
		table = table != null ? table : findTable(component) ;
		if (table != null)
			actionPerformed(table, location, event);
	}

	//	________________________________________________________________________
	
	protected abstract void actionPerformed(JXTable table, Point location, ActionEvent event);
	
	//	________________________________________________________________________
	
	protected JXTable findTable(Component component) {
		if (component instanceof JXTable)
			return (JXTable) component;
		
		if (!(component instanceof Container))
			return null;

		Container container = (Container) component;
		for (int i=0; i < container.getComponentCount(); i++) {
			JXTable result = findTable(container.getComponent(i));
			if (result != null)
				return result;
		}
		return null;
	}
	
	private Point getPreferredLocation(final JPopupMenu menu) {	
		try
		{
			int desiredLocationX = -1 ,desiredLocationY = -1;
			final Field fields[] = JPopupMenu.class.getDeclaredFields();
		    for (int i = 0; i < fields.length; ++i) {
		      if ("desiredLocationX".equals(fields[i].getName())) {
		    	  fields[i].setAccessible(true);
		    	  desiredLocationX = fields[i].getInt(menu);
		      }
		      if ("desiredLocationY".equals(fields[i].getName())) {
		    	  fields[i].setAccessible(true);
		    	  desiredLocationY = fields[i].getInt(menu);
		      }
		    }
		    return desiredLocationX != -1 && desiredLocationY != -1 
		    	? new Point(desiredLocationX, desiredLocationY) : null;
		}
		catch (IllegalAccessException ex) {
			return null;
		}
	}
}