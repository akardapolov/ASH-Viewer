/*
 *-------------------
 * The DefaultTreeTableModel.java is part of ASH Viewer
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

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * DefaultTreeTableModel is a concrete implementation of <code>AbstractTreeTableModel</code>
 * and is provided purely as a convenience. Applications that use <code>JXTreeTable</code>
 * are expected to provide their own implementation of a <code>TreeTableModel</code>,
 * perhaps by extending this class.
 *
 * @author Ramesh Gupta
 */
public class DefaultTreeTableModel extends AbstractTreeTableModel {

    protected boolean asksAllowsChildren;

    public DefaultTreeTableModel() {
        this(null);
    }

    public DefaultTreeTableModel(TreeNode root) {
        this(root, false);
    }

    public DefaultTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
        super(root);
        this.asksAllowsChildren = asksAllowsChildren;
    }

    public void setRoot(TreeNode root) {
        Object oldRoot = this.root;
        this.root = root;
        if (root == null && oldRoot != null) {
            fireTreeStructureChanged(this, null);
        }
        else {
            nodeStructureChanged(root);
        }
    }

    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    public boolean asksAllowsChildren() {
        return asksAllowsChildren;
    }

    public void setAsksAllowsChildren(boolean newValue) {
        asksAllowsChildren = newValue;
    }

    public Object getValueAt(Object node, int column) {
        /**@todo Implement this org.jdesktopx.swing.treetable.TreeTableModel abstract method*/
        return node + "@column " + column;
    }

    public void setValueAt(Object value, Object node, int column) {
        /**@todo Implement this org.jdesktopx.swing.treetable.TreeTableModel abstract method*/
    }

    public TreeNode[] getPathToRoot(TreeNode node) {
        return getPathToRoot(node, 0);
    }

    protected TreeNode[] getPathToRoot(TreeNode node, int depth) {
        TreeNode[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (node == null) {
            if (depth == 0)
                return null;
            else
                retNodes = new TreeNode[depth];
        }
        else {
            depth++;
            if (node == root)
                retNodes = new TreeNode[depth];
            else
                retNodes = getPathToRoot(node.getParent(), depth);
            retNodes[retNodes.length - depth] = node;
        }
        return retNodes;
    }

    /**
     * @param node
     * @return true if the specified node is a leaf node; false otherwise
     */
    @Override
	public boolean isLeaf(Object node) {
        if (node instanceof TreeNode) {
            if (asksAllowsChildren) {
                return!((TreeNode) node).getAllowsChildren();
            }
        }
        return super.isLeaf(node);
    }

    public void reload() {
        TreeNode treeNode;
        try {
            treeNode = (TreeNode) root;
        }
        catch (ClassCastException ex) {
            return;
        }

        reload(treeNode);
    }

    public void reload(TreeNode node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Invoke this method after you've inserted some TreeNodes into
     * node.  childIndices should be the index of the new elements and
     * must be sorted in ascending order.
     */
    public void nodesWereInserted(TreeNode node, int[] childIndices) {
        if (listenerList != null && node != null && childIndices != null
            && childIndices.length > 0) {
            int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];

            for (int counter = 0; counter < cCount; counter++)
                newChildren[counter] = node.getChildAt(childIndices[counter]);
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                                  newChildren);
        }
    }

    /**
     * Invoke this method after you've removed some TreeNodes from
     * node.  childIndices should be the index of the removed elements and
     * must be sorted in ascending order. And removedChildren should be
     * the array of the children objects that were removed.
     */
    public void nodesWereRemoved(TreeNode node, int[] childIndices,
                                 Object[] removedChildren) {
        if (node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                                 removedChildren);
        }
    }

    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     */
    public void nodesChanged(TreeNode node, int[] childIndices) {
        if (node != null) {
            if (childIndices != null) {
                int cCount = childIndices.length;

                if (cCount > 0) {
                    Object[] cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++)
                        cChildren[counter] = node.getChildAt
                            (childIndices[counter]);
                    fireTreeNodesChanged(this, getPathToRoot(node),
                                         childIndices, cChildren);
                }
            }
            else if (node == getRoot()) {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of
     * node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
    public void nodeStructureChanged(TreeNode node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

	public int getHierarchicalColumn() {
		// TODO Auto-generated method stub
		return 0;
	}
}