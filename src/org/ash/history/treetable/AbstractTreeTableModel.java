/*
 *-------------------
 * The AbstractTreeTableModel.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */

package org.ash.history.treetable;

import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.TreeTableModel;

// There is no javax.swing.tree.AbstractTreeModel; There ought to be one.

/**
 * AbstractTreeTableModel provides an implementation of
 * {@link org.ash.history.treetable.TreeTableModel} as a convenient starting
 * point in defining custom data models for {@link org.jdesktop.swingx.JXTreeTable}.
 *
 * @author Ramesh Gupta
 */
public abstract class AbstractTreeTableModel implements TreeTableModel {
    /**
     * Value returned by {@link org.ash.history.treetable.TreeTableModel#getColumnClass(int) getColumnClass}
     * for the {@link org.jdesktop.swingx.JXTreeTable#isHierarchical(int) hierarchical} column.
     */
	public final static Class hierarchicalColumnClass = TreeTableModel.class;

    /**
     * Root node of the model
     */
    protected Object root;

    /**
     * Event listener list
     */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Constructs an <code>AbstractTreeTableModel</code> with a null root node
     */
    public AbstractTreeTableModel() {
        this(null);
    }

    /**
     * Constructs an <code>AbstractTreeTableModel</code> with the specified node
     * as the root node.
     *
     * @param root root node
     */
    public AbstractTreeTableModel(Object root) {
        this.root = root;
    }

    /**
     * {@inheritDoc}
     */
    public Class getColumnClass(int column) {
        // Assume that the first column will contain hierarchical nodes.
        return column == 0 ? hierarchicalColumnClass : Object.class;
    }

    /**
     * {@inheritDoc}
     */
    public String getColumnName(int column) {
        return "Column " + column; // Cheap implementation
    }

    /**
     * {@inheritDoc}
     */
    public Object getRoot() { // From the TreeNode interface
        return root;
    }

    /**
     * Returns the child of <I>parent</I> at index <I>index</I> in the parent's
     * child array.  <I>parent</I> must be a node previously obtained from
     * this data source. This should not return null if <i>index</i>
     * is a valid index for <i>parent</i> (that is <i>index</i> >= 0 &&
     * <i>index</i> < getChildCount(<i>parent</i>)).
     *
     * @param   parent  a node in the tree, obtained from this data source
     * @return  the child of <I>parent</I> at index <I>index</I>, or null if the
     * specified parent node is not a <code>TreeNode</code>.
     */
    public Object getChild(Object parent, int index) {
        // meant to be overridden
        try {
            return ((TreeNode) parent).getChildAt(index);
        }
        catch (ClassCastException ex) { // not a TreeNode?
            return null;
        }
    }

    /**
     * Returns the number of children in the specified parent node.
     *
     * @param parent node whose child count is being requested
     * @return the number of children in the specified parent node
     */
    public int getChildCount(Object parent) {
        // meant to be overridden
        try {
            return ((TreeNode) parent).getChildCount();
        }
        catch (ClassCastException ex) { // not a TreeNode?
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getColumnCount() {
        // meant to be overridden
        return 1; // Cheap (and woefully inadequate) implementation
    }

    /**
     * Returns the index of child in parent.
     * If either the parent or child is <code>null</code>, returns -1.
     * @param parent a note in the tree, obtained from this data source
     * @param child the node we are interested in
     * @return the index of the child in the parent, or -1
     *    if either the parent or the child is <code>null</code>
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null)
            return -1;

        try {
            return ((TreeNode) parent).getIndex((TreeNode) child);
        }
        catch (ClassCastException ex) { // not a TreeNode?
            // This is not called in the JTree's default mode.
            // Use a naive implementation.
            for (int i = 0; i < getChildCount(parent); i++) {
                if (getChild(parent, i).equals(child)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable(Object node, int column) {
		// RG: Fix Issue 49 -- Cell not editable, by default.
		// Subclasses might override this to return true.
        return false;
    }

    /**
     * Returns true if the specified node is a leaf node; false otherwise.
     *
     * @param node node to test
     * @return true if the specified node is a leaf node; false otherwise
     */
    public boolean isLeaf(Object node) {
        try {
            return ((TreeNode) node).isLeaf();
        }
        catch (ClassCastException ex) { // not a TreeNode?
            return getChildCount(node) == 0;
        }
    }

    /**
     * Called when value for the item identified by path has been changed.
     * If newValue signifies a truly new value the model should
     * post a <code>treeNodesChanged</code> event.
     *
     * @param path path to the node that has changed
     * @param newValue the new value from the <code>TreeCellEditor</code>
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        /**@todo Implement this javax.swing.tree.TreeModel method*/
    }

    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    public TreeModelListener[] getTreeModelListeners() {
        return listenerList.getListeners(
            TreeModelListener.class);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path,
                                        int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,
                                         int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
                                            int[] childIndices,
                                            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this model.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     *
     * You can specify the <code>listenerType</code> argument
     * with a class literal,
     * such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>DefaultTreeModel</code> <code>m</code>
     * for its tree model listeners with the following code:
     *
     * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     *          should specify an interface that descends from
     *          <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *          <code><em>Foo</em>Listener</code>s on this component,
     *          or an empty array if no such
     *          listeners have been added
     * @exception ClassCastException if <code>listenerType</code>
     *          doesn't specify a class or interface that implements
     *          <code>java.util.EventListener</code>
     *
     * @see #getTreeModelListeners
     *
     * @since 1.3
     */
    public EventListener[] getListeners(Class listenerType) {
        return listenerList.getListeners(listenerType);
    }

    // Left to be implemented in the subclass:

    /**
     * public Object getChild(Object parent, int index)
     * public int getChildCount(Object parent)
     * public int getColumnCount()
     * public String getColumnName(int column)
     * public Object getValueAt(Object node, int column)
     * public void setValueAt(Object value, Object node, int column)
     */
}
