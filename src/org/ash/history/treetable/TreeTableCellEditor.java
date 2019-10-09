/*
 *-------------------
 * The TreeTableCellEditor.java is part of ASH Viewer
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

import java.util.EventObject;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * An editor that can be used to edit the tree column. This extends
 * DefaultCellEditor and uses a JTextField (actually, TreeTableTextField)
 * to perform the actual editing.
 * <p>To support editing of the tree column we can not make the tree
 * editable. The reason this doesn't work is that you can not use
 * the same component for editing and renderering. The table may have
 * the need to paint cells, while a cell is being edited. If the same
 * component were used for the rendering and editing the component would
 * be moved around, and the contents would change. When editing, this
 * is undesirable, the contents of the text field must stay the same,
 * including the caret blinking, and selections persisting. For this
 * reason the editing is done via a TableCellEditor.
 * <p>Another interesting thing to be aware of is how tree positions
 * its render and editor. The render/editor is responsible for drawing the
 * icon indicating the type of node (leaf, branch...). The tree is
 * responsible for drawing any other indicators, perhaps an additional
 * +/- sign, or lines connecting the various nodes. So, the renderer
 * is positioned based on depth. On the other hand, table always makes
 * its editor fill the contents of the cell. To get the allusion
 * that the table cell editor is part of the tree, we don't want the
 * table cell editor to fill the cell bounds. We want it to be placed
 * in the same manner as tree places it editor, and have table message
 * the tree to paint any decorations the tree wants. Then, we would
 * only have to worry about the editing part. The approach taken
 * here is to determine where tree would place the editor, and to override
 * the <code>reshape</code> method in the JTextField component to
 * nudge the textfield to the location tree would place it. Since
 * JXTreeTable will paint the tree behind the editor everything should
 * just work. So, that is what we are doing here. Determining of
 * the icon position will only work if the TreeCellRenderer is
 * an instance of DefaultTreeCellRenderer. If you need custom
 * TreeCellRenderers, that don't descend from DefaultTreeCellRenderer,
 * and you want to support editing in JXTreeTable, you will have
 * to do something similiar.
 *
 * @author Scott Violet
 * @author Ramesh Gupta
 */
public class TreeTableCellEditor extends DefaultCellEditor {
    public TreeTableCellEditor(JTree tree) {
        super(new TreeTableTextField());
        if (tree == null) {
            throw new IllegalArgumentException("null tree");
        }

        this.tree = tree; // immutable
    }

    /**
     * Overriden to determine an offset that tree would place the
     * editor at. The offset is determined from the
     * <code>getRowBounds</code> JTree method, and additionaly
     * from the icon DefaultTreeCellRenderer will use.
     * <p>The offset is then set on the TreeTableTextField component
     * created in the constructor, and returned.
     */
    @Override
	public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row,
                                                 int column) {
        Component component = super.getTableCellEditorComponent(table, value,
            isSelected, row, column);
        Rectangle bounds = tree.getRowBounds(row);
        int offset = bounds.x;
        TreeCellRenderer tcr = tree.getCellRenderer();
        if (tcr instanceof DefaultTreeCellRenderer) {
            Object node = tree.getPathForRow(row).getLastPathComponent();
            Icon icon;
            if (tree.getModel().isLeaf(node))
                icon = ((DefaultTreeCellRenderer) tcr).getLeafIcon();
            else if (tree.isExpanded(row))
                icon = ((DefaultTreeCellRenderer) tcr).getOpenIcon();
            else
                icon = ((DefaultTreeCellRenderer) tcr).getClosedIcon();

            if (icon != null) {
                offset += ((DefaultTreeCellRenderer) tcr).getIconTextGap() +
                    icon.getIconWidth();
            }
        }
        ((TreeTableTextField) getComponent()).offset = offset;
        return component;
    }

    /**
     * This is overriden to forward the event to the tree. This will
     * return true if the click count >= clickCountToStart, or the event is null.
     */
    @Override
	public boolean isCellEditable(EventObject e) {
        if (e == null) {
            return true;
        }
        else if (e instanceof MouseEvent) {
            return (((MouseEvent) e).getClickCount() >= clickCountToStart);
        }

	// e is some other type of event...
        return false;
    }

    /**
     * Component used by TreeTableCellEditor. The only thing this does
     * is to override the <code>reshape</code> method, and to ALWAYS
     * make the x location be <code>offset</code>.
     */
    static class TreeTableTextField extends JTextField {
        int offset; // changed to package private instead of public

        @Override
		public void reshape(int x, int y, int width, int height) {
            // Allows precise positioning of text field in the tree cell.
            //Border border = this.getBorder(); // get this text field's border
            //Insets insets = border == null ? null : border.getBorderInsets(this);
            //int newOffset = offset - (insets == null ? 0 : insets.left);
            int newOffset = offset - getInsets().left;
            super.reshape(x + newOffset, y, width - newOffset, height);
        }
    }

    private final JTree tree; // immutable
}