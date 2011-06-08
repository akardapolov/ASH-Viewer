/*
 * @(#)NodeCellRenderer.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.cell.renderer.basic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.egantt.swing.cell.CellRenderer;
import com.egantt.swing.cell.CellState;

public class NodeCellRenderer extends AbstractCellRenderer implements CellRenderer
{
	private static final long serialVersionUID = -268211588941938710L;
	private static final BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE,
																				 BasicStroke.JOIN_ROUND, 0, new float[]{0,2,0,2}, 0);
	protected int depth;

	protected boolean isExpanded = false;
	protected boolean isLeaf = false;

	// __________________________________________________________________________

	/**
	 * Overrides <code>JComponent.getPreferredSize</code> to
	 * return slightly wider preferred size value.
	 */
	public Dimension getPreferredSize()
	{
		Dimension retDimension = super.getPreferredSize();
		if(retDimension != null)
			retDimension = new Dimension(retDimension.width + 3, retDimension.height);
		return retDimension;
	}

	// __________________________________________________________________________

	/**
	 * Configures the renderer based on the passed in components.
	 * The value is set from messaging the tree with
	 * <code>convertValueToText</code>, which ultimately invokes
	 * <code>toString</code> on <code>value</code>.
	 * The foreground color is set based on the selection and the icon
	 * is set based on on leaf and expanded.
	 */

	public JComponent getComponent(CellState state, JComponent parent)
	{
		JComponent component = super.getComponent(state, parent);

		Icon icon = isLeaf
			? UIManager.getIcon("Tree.leafIcon")
			: isExpanded ? UIManager.getIcon("Tree.openIcon") : UIManager.getIcon("Tree.closedIcon");

		JComponent source = (JComponent) state.getSource();
		setEnabled(source.isEnabled());

		if (isEnabled())
			setIcon(icon);
		else
			setDisabledIcon(icon);

		return component;
	}

	/**
	 * Paints the value.  The background is filled based on selected.
	 */
	public void paint(Graphics g)
	{
		int height = getHeight();
		// int width = getWidth();
		int y = (height / 2);

		g.setColor(Color.lightGray);

		Icon icon = isExpanded
			? UIManager.getIcon("Tree.expandedIcon")
			: UIManager.getIcon( "Tree.collapsedIcon");

		int iconWidth = icon.getIconWidth();
		int x = icon.getIconWidth() / 2;
		for (int i=0; i <= depth; i++)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(stroke);
			g.drawLine(x, 0, x , height);
			g.translate(iconWidth, 0);
		}

		g.drawLine(x, y, iconWidth , y);
		icon.paintIcon(this, g, 0 ,0);
		g.drawLine(x, 0, x , height);

		g.translate(iconWidth,0);
		super.paint(g);
	}

	protected void setValue(Object value)
	{
		int depth = -2; // -1 show root
		for (Node node = ((Node) value).getParentNode(); node != null; node = (Node) node.getParentNode())
			if (node instanceof Element)
				depth++;

		this.depth = depth;

		Node node = (Node) value;
		this.isLeaf = !node.hasChildNodes();
		this.isExpanded = isLeaf || ((Element)node).getAttribute("expanded").equals("true");
		super.setValue(((Element)node).getAttribute("name"));
	}

	// _________________________________________________________________________
/**
	private int getLabelStart()
	{
		Icon currentI = getIcon();
		if (currentI != null && getText() != null)
			return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
		return 0;
	} */
}
