/**
 * @(#)SplitLayeredHolder.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.component.holder;

import java.awt.Component;

import javax.swing.BoundedRangeModel;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;

import com.egantt.model.component.ComponentList;

public class SplitLayeredHolder extends JLayeredPane{

	private static final long serialVersionUID = -3211706232794493344L;
	
	protected final SplitComponentHolder holder;
	
	public SplitLayeredHolder() {
		holder = new SplitComponentHolder();
		this.setLayout(new OverlayLayout(this));
		this.add(holder.getComponent(), JLayeredPane.DEFAULT_LAYER);
	}
	
	//	________________________________________________________________________
	
	public void setComponentList(ComponentList list)
	{
		holder.setComponentList(list);
	}

	public void setRangeModel(BoundedRangeModel model)
	{
		holder.setRangeModel(model);
	}

	// _________________________________________________________________________
	
	public void setDividerLocation(double proportionalLocation) {
		holder.setDividerLocation(proportionalLocation);
	}
	
	public void setDividerLocation(int location) {
		holder.setDividerLocation(location);
	}
	
	public void setDividerSize(int newSize) {
		holder.setDividerSize(newSize);
	}

	// _________________________________________________________________________
	

	public Component getComponent()
	{
		return this;
	}
}