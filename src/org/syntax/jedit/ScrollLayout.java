/*
 *---------------
 * The ScrollLayout.java is part of ASH Viewer
 *---------------
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
 * Created on 02.03.2009
 *
 * Copyright (C) 2009 Alex Kardapolov
 *
 */
package org.syntax.jedit;

import java.awt.*;
import javax.swing.border.Border;
import javax.swing.JComponent;

public class ScrollLayout implements LayoutManager
{
	public static final String CENTER = "center";
	public static final String RIGHT = "right";
	public static final String LEFT = "left";
	public static final String BOTTOM = "bottom";
	public static final String TOP = "top";

	public void addLayoutComponent(String name, Component comp)
	{
		if(name.equals(CENTER))
			center = comp;
		else if(name.equals(RIGHT))
			right = comp;
		else if(name.equals(BOTTOM))
			bottom = comp;
	} 
	
	public void removeLayoutComponent(Component comp)
	{
		if(center == comp)
			center = null;
		else if(right == comp)
			right = null;
		else if(bottom == comp)
			bottom = null;
	}

	public Dimension preferredLayoutSize(Container parent)
	{
		Dimension dim = new Dimension();
		Insets insets = getInsets(parent);

		dim.width = insets.left + insets.right;
		dim.height = insets.top + insets.bottom;

		Dimension centerPref = center.getPreferredSize();
		dim.width += centerPref.width;
		dim.height += centerPref.height;
		Dimension rightPref = right.getPreferredSize();
		dim.width += rightPref.width;
		Dimension bottomPref = bottom.getPreferredSize();
		dim.height += bottomPref.height;

		return dim;
	} 

	public Dimension minimumLayoutSize(Container parent)
	{
		Dimension dim = new Dimension();
		Insets insets = getInsets(parent);

		dim.width =  insets.right;
		dim.height = insets.bottom;

		Dimension centerPref = center.getMinimumSize();
		dim.width += centerPref.width; 
		dim.height += centerPref.height;
		Dimension rightPref = right.getMinimumSize();
		dim.width += rightPref.width;
		Dimension bottomPref = bottom.getMinimumSize();
		dim.height += bottomPref.height;
		
		return dim;
	} 

	public void layoutContainer(Container parent)
	{
		Dimension size = parent.getSize();
		Insets insets = getInsets(parent);

		int ibottom = insets.bottom;
		int iright = insets.right;
		int rightWidth = right.getPreferredSize().width;
		int topHeight = 0;
		
		int bottomHeight = bottom.getPreferredSize().height;
		int centerWidth = Math.max(0,size.width 
			- rightWidth -  iright);
		int centerHeight = Math.max(0,size.height - topHeight
			- bottomHeight -  ibottom);
			
		center.setBounds(
			 0,
			topHeight,
			centerWidth,
			centerHeight);

		right.setBounds(
			  centerWidth,
			topHeight,
			rightWidth,
			centerHeight);

		bottom.setBounds(
			0,
			topHeight + centerHeight,
			Math.max(0,size.width - bottom.getHeight()
				- iright),
			bottomHeight);
		
	}

	private Component center;
	private Component right;
	private Component bottom;

	private Insets getInsets(Component parent)
	{
		Border border = ((JComponent)parent).getBorder();
		if(border == null)
			return new Insets(0,0,0,0);
		else
			return border.getBorderInsets(parent);
	} 

}