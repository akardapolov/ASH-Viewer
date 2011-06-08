/**
 * @(#)AbstractScrollManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.swing.scroll.manager;

import com.egantt.swing.scroll.ScrollManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Overrides the ScrollModel interface in order to hide non applicable methods
 * the sun interface.
 */
public abstract class AbstractScrollManager implements ScrollManager
{
	protected final List <ChangeListener> listeners = new ArrayList <ChangeListener>(0);

	protected transient boolean isAdjusting;

	// _________________________________________________________________________

	public boolean getValueIsAdjusting()
	{
		return isAdjusting;
	}

	// _________________________________________________________________________

	public final void setValueIsAdjusting(boolean isAdjusting)
	{
		this.isAdjusting = isAdjusting;
	}

	// _________________________________________________________________________

	public void addChangeListener(ChangeListener listener)
	{
		listeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener)
	{
		listeners.remove(listener);
	}

	// _________________________________________________________________________

	/**
	 * Dummy method not applicable
	 */
	public final void setExtent(int n)
	{
	}

	/**
	 * Dummy method not applicable
	 */
	public final void setMinimum(int n)
	{
	}

	/**
	 * Dummy method not applicable
	 */
	public final void setMaximum(int n)
	{
	}

	/**
	 * Dummy method not applicable
	 */
	public final void setRangeProperties(int newValue, int newExtent, int newMin, int newMax, boolean adjusting)
	{
	}

	// _________________________________________________________________________

	/**
	 * Fire notification of state changes
	 */
	protected void fireChanged(ChangeEvent changeEvent)
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext();)
				((ChangeListener) iter.next()).stateChanged(changeEvent);
	}
}
