/*
 * @(#)BasicRepaintManager.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.component.repaint.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.egantt.swing.component.ComponentContext;
import com.egantt.swing.component.repaint.RepaintManager;

/**
 * Handles the repainting of a given set of components, this component set can
 * be repainted by invoking the #repaint() call
 */
	public abstract class BasicRepaintManager implements RepaintManager {
	protected final Set<JComponent> components = new HashSet<JComponent>();

	// _________________________________________________________________________

	public synchronized void registerComponent(JComponent component, ComponentContext context) {
		components.add(component);
	}

	public synchronized void unregisterComponent(JComponent component) {
		components.remove(component);
	}

	// _________________________________________________________________________

	/**
	 * <code>repaint</code> revalidate's the component and uses it's
	 * apropriate instantiation of javax.swing.RepaintManager to mark it
	 * completely dirty.
	 */
	public void repaint() {
		SwingUtilities.invokeLater(new LocalRunnable());
	}

	protected class LocalRunnable implements Runnable {
		public void run() {
			for (Iterator iter = components.iterator(); iter.hasNext();) {
				JComponent component = (JComponent) iter.next();

				// fix up the component size
				// component.revalidate();

				// repaint the component this is the recomended way to do it
				javax.swing.RepaintManager manager = javax.swing.RepaintManager
						.currentManager(component);

				manager.markCompletelyDirty(component);
			}
		}
	}
}
