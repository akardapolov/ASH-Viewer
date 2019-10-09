/*
 *-------------------
 * The Thumbnail.java is part of ASH Viewer
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
package org.ash.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.ash.util.Options;

public class Thumbnail extends JDialog {
	
	/**
	 * Constructor
	 *  
	 * @param root0
	 */
	public Thumbnail(MainFrame root0, JPanel jpanel){
		 super(root0, Options.getInstance().getResource("ThumbnailMain.text"), true);
		 super.setResizable(true);
		 	getContentPane().add(jpanel);
		 super.pack();
		 this.setSize(600,300);

		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 Dimension frameSize = this.getSize();
		  if (frameSize.height > screenSize.height) {
		        frameSize.height = screenSize.height;
		     }
		  if (frameSize.width > screenSize.width) {
		        frameSize.width = screenSize.width;
		    }
		  this.setLocation((screenSize.width - frameSize.width)/2, 
		    				 (screenSize.height - frameSize.height)/2+150);
	}
	
}
